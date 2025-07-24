package com.zizonhyunwoo.anysearch.service.impl;

import com.zizonhyunwoo.anysearch.request.AnyDataRequestWithFile;
import com.zizonhyunwoo.anysearch.dao.AnyDataRepository;
import com.zizonhyunwoo.anysearch.domain.AnyData;
import com.zizonhyunwoo.anysearch.domain.UserInfo;
import com.zizonhyunwoo.anysearch.elastic.index.AnyDataDocument;
import com.zizonhyunwoo.anysearch.request.AnyDataInsertRequest;
import com.zizonhyunwoo.anysearch.response.AnyDataResponse;
import com.zizonhyunwoo.anysearch.response.UserInfoResponse;
import com.zizonhyunwoo.anysearch.service.AnyDataService;
import com.zizonhyunwoo.anysearch.util.ElasticsearchFileIndexer;
import com.zizonhyunwoo.anysearch.util.ElasticsearchIndexer;
import com.zizonhyunwoo.anysearch.util.ParsingUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnyDataServiceImpl implements AnyDataService {

    private final AnyDataRepository anyDataRepository;
    private final ElasticsearchIndexer elasticsearchIndexer;
    private final ElasticsearchFileIndexer elasticsearchFileIndexer;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public AnyDataResponse insert(UserInfo userInfo, AnyDataInsertRequest anyData) {

        boolean flag = anyDataRepository.existsByType(anyData.type());
        AnyData res= anyDataRepository.save(new AnyData(userInfo,anyData));


        if (flag) {// 새로운 타입이 아니라면 추가

            Map<String,String> addFieldList = ParsingUtil.parseData(res.getAddInfo(),res.getAddDetail());

            AnyDataDocument doc = AnyDataDocument.builder()
                    .id(res.getId().toString())
                    .type(res.getType())
                    .title(res.getTitle())
                    .description(res.getDescription())
                    .additionalFields(addFieldList)
                    .createdAt(res.getCreatedAt())
                    .updatedAt(res.getUpdatedAt())
                    .isActive(res.getIsActive())
                    .userId(res.getUserInfo().getEmail())
                    .autoCompletion(res.getTitle())
                    .build();
            elasticsearchIndexer.saveData(res.getType(), doc);
        }

        return getAnyDataResponse(res);
    }

    @Override
    public AnyDataResponse read(String id) {

        return anyDataRepository.findById(UUID.fromString(id)).stream()
                .map(AnyDataServiceImpl::getAnyDataResponse)
                .toList().get(0);
    }

    @Override
    public List<AnyDataResponse> findAll(int page, int size) {
        // userid 불일치 처리 필요
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        return anyDataRepository.findAll(pageRequest).stream().map(AnyDataServiceImpl::getAnyDataResponse).toList();
    }

    private static AnyDataResponse getAnyDataResponse(AnyData data) {
        return new AnyDataResponse(
                data.getId().toString(),
                data.getType(), data.getTitle(), data.getDescription(), data.getAddInfo(),
                data.getAddDetail(), data.getCreatedAt(), data.getUpdatedAt(), data.getIsActive(),
                new UserInfoResponse(data.getUserInfo().getName(), data.getUserInfo().getEmail(), data.getUserInfo().getNickname()
                ));
    }

    @Override
    @Transactional
    public AnyDataResponse update(UserInfo userInfo, AnyDataInsertRequest anyData) {
        System.out.println("anyData = " + anyData);
        return getAnyDataResponse(anyDataRepository.save(new AnyData(userInfo,anyData)));
    }

    @Override
    @Transactional
    public void delete(UserInfo userInfo, UUID id) {
        anyDataRepository.deleteByIdAndUserInfo_Id(id,userInfo.getId());
    }

    @Override
//    @Transactional
    public List<AnyDataResponse> insertAll(UserInfo userInfo, List<AnyDataInsertRequest> anyDataList) {
        List<AnyDataResponse> anyDatas = new ArrayList<>();
        System.out.println("anyData = " + anyDataList);
//        anyDataList.forEach(anyDataInsertRequest ->
//                anyDatas.add(insert(userInfo,anyDataInsertRequest)));


            jdbcTemplate.batchUpdate(
                "INSERT INTO any_data(any_data_id, add_detail, add_info, created_at, description, is_active, title, type, updated_at, user_id)" +
                        " VALUES(?,?,?,?,?,?,?,?,?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        AnyData res = new AnyData(userInfo, anyDataList.get(i));
                        System.out.println("res = " + res);
                        ps.setObject(1, UUID.randomUUID());
                        ps.setString(2, res.getAddDetail());
                        ps.setString(3, res.getAddInfo());
                        ps.setObject(4, res.getCreatedAt());
                        ps.setString(5, res.getDescription());
                        ps.setBoolean(6, res.getIsActive());
                        ps.setString(7, res.getTitle());
                        ps.setString(8, res.getType());
                        ps.setObject(9, res.getUpdatedAt());
                        ps.setObject(10, res.getUserInfo().getId());

                    }

                    @Override
                    public int getBatchSize() {
                        return anyDataList.size();
                    }
                }
            );

        return anyDatas;
    }

    @Override
    public List<String> getDataType() {
        PageRequest pageRequest = PageRequest.of(0, Integer.MAX_VALUE);
        return anyDataRepository.findType(pageRequest).toList();
    }

    @Override
    public void insertFile(MultipartFile file) {
        Base64.Encoder encoder = Base64.getEncoder();
        try {
            String fileName = file.getOriginalFilename();
            byte[] data = encoder.encode(file.getBytes());
            elasticsearchFileIndexer.saveFile(fileName,data);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }


}
