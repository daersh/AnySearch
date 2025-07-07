package com.zizonhyunwoo.anysearch.service.impl;

import com.zizonhyunwoo.anysearch.dao.AnyDataRepository;
import com.zizonhyunwoo.anysearch.domain.AnyData;
import com.zizonhyunwoo.anysearch.domain.UserInfo;
import com.zizonhyunwoo.anysearch.request.AnyDataInsertRequest;
import com.zizonhyunwoo.anysearch.response.AnyDataResponse;
import com.zizonhyunwoo.anysearch.response.UserInfoResponse;
import com.zizonhyunwoo.anysearch.service.AnyDataService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnyDataServiceImpl implements AnyDataService {

    private final AnyDataRepository anyDataRepository;

    @Override
    @Transactional
    public AnyDataResponse insert(UserInfo userInfo, AnyDataInsertRequest anyData) {

        return getAnyDataResponse(anyDataRepository.save(new AnyData(userInfo,anyData)));
    }

    @Override
    public AnyDataResponse read(String id) {

        return anyDataRepository.findById(UUID.fromString(id)).stream()
                .map(AnyDataServiceImpl::getAnyDataResponse)
                .toList().get(0);
    }

    private static AnyDataResponse getAnyDataResponse(AnyData data) {
        return new AnyDataResponse(
                data.getType(), data.getTitle(), data.getDescription(), data.getAddInfo(),
                data.getAddDetail(), data.getCreatedAt(), data.getUpdatedAt(), data.getIsActive(),
                new UserInfoResponse(data.getUserInfo().getName(), data.getUserInfo().getEmail(), data.getUserInfo().getNickname()
                ));
    }

    @Override
    @Transactional
    public AnyDataResponse update(UserInfo userInfo, AnyDataInsertRequest anyData) {
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

}
