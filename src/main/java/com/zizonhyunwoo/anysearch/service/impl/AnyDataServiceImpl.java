package com.zizonhyunwoo.anysearch.service.impl;

import com.zizonhyunwoo.anysearch.dao.AnyDataRepository;
import com.zizonhyunwoo.anysearch.domain.AnyData;
import com.zizonhyunwoo.anysearch.domain.UserInfo;
import com.zizonhyunwoo.anysearch.elastic.index.AnyDataDocument;
import com.zizonhyunwoo.anysearch.request.AnyDataInsertRequest;
import com.zizonhyunwoo.anysearch.response.AnyDataResponse;
import com.zizonhyunwoo.anysearch.response.UserInfoResponse;
import com.zizonhyunwoo.anysearch.service.AnyDataService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AnyDataServiceImpl implements AnyDataService {

    private final AnyDataRepository anyDataRepository;
    private final ElasticsearchTemplate elasticsearchTemplate;

    @Override
    @Transactional
    public AnyDataResponse insert(UserInfo userInfo, AnyDataInsertRequest anyData) {

        boolean flag = anyDataRepository.existsByType(anyData.type());
        AnyData res= anyDataRepository.save(new AnyData(userInfo,anyData));

        System.out.println("flag = " + flag);
        if (flag) {// 새로운 타입이 아니라멵 추가잉~

            String dynamicIndexName = "anydata_" + res.getType().toLowerCase(Locale.KOREA);
            IndexCoordinates dynamicIndexCoordinates = IndexCoordinates.of(dynamicIndexName);

            Map<String,String> addFieldList = parseData(res.getAddInfo(),res.getAddDetail());
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
                    .build();
            System.out.println("doc = " + doc);
            elasticsearchTemplate.save(doc,dynamicIndexCoordinates);
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
    @Transactional
    public List<AnyDataResponse> insertAll(UserInfo userInfo, List<AnyDataInsertRequest> anyDataList) {
        List<AnyDataResponse> anyDatas = new ArrayList<>();

        anyDataList.forEach(anyDataInsertRequest ->
                anyDatas.add(insert(userInfo,anyDataInsertRequest)));

        return anyDatas;
    }

    @Override
    public List<String> getDataType() {
        return anyDataRepository.findType();
    }


    private Map<String, String> parseData(String addInfo, String addDetail) {

        String delimiter = "†";

        int count = addInfo.split(delimiter).length;
        List<String> keys = Arrays.asList(addInfo.split(delimiter));
        List<String> values = Arrays.asList(addDetail.split(delimiter));
        Map<String, String> data = new HashMap<>();
        for (int i = 0; i < count; i++) {
            try {
                data.put(keys.get(i), values.get(i));
            }catch (Exception e){
                break;
            }
        }
        return data;
    }
}
