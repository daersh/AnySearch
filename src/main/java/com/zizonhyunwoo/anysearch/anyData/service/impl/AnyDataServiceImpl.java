package com.zizonhyunwoo.anysearch.anyData.service.impl;

import com.zizonhyunwoo.anysearch.anyData.dao.AnyDataRepository;
import com.zizonhyunwoo.anysearch.anyData.domain.AnyData;
import com.zizonhyunwoo.anysearch.user.domain.UserInfo;
import com.zizonhyunwoo.anysearch.elastic.index.AnyDataDocument;
import com.zizonhyunwoo.anysearch.anyData.request.AnyDataInsertRequest;
import com.zizonhyunwoo.anysearch.anyData.response.AnyDataResponse;
import com.zizonhyunwoo.anysearch.user.response.UserInfoResponse;
import com.zizonhyunwoo.anysearch.anyData.service.AnyDataService;
import com.zizonhyunwoo.anysearch.elastic.util.ElasticsearchFileIndexer;
import com.zizonhyunwoo.anysearch.elastic.util.ElasticsearchIndexer;
import com.zizonhyunwoo.anysearch.common.util.ParsingUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
        try {
            String fileName = file.getOriginalFilename();
            String charset = detectCharset(file);
            System.out.println("charset = " + charset);
            if (charset == null) charset = "UTF-8";

            String content = new String(file.getBytes(), charset);

            byte[] utf8Bytes = content.getBytes(StandardCharsets.UTF_8);

            elasticsearchFileIndexer.saveFile(fileName, utf8Bytes);
        } catch (Exception e) {
            log.error("파일 저장 실패: {}", e.getMessage(), e);
        }
    }


    private String detectCharset(MultipartFile file) {
        try {
            byte[] fileBytes = file.getBytes();

            UniversalDetector detector = new UniversalDetector(null);
            detector.handleData(fileBytes, 0, fileBytes.length);
            detector.dataEnd();

            String detectedCharset = detector.getDetectedCharset();
            detector.reset();

            return detectedCharset;
        } catch (IOException e) {
            log.error("인코딩 감지 중 오류 발생", e);
            return null;
        }
    }

}
