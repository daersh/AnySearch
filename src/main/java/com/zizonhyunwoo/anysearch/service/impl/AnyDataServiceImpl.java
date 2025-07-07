package com.zizonhyunwoo.anysearch.service.impl;

import com.zizonhyunwoo.anysearch.dao.AnyDataRepository;
import com.zizonhyunwoo.anysearch.domain.AnyData;
import com.zizonhyunwoo.anysearch.domain.UserInfo;
import com.zizonhyunwoo.anysearch.request.AnyDataInsertRequest;
import com.zizonhyunwoo.anysearch.service.AnyDataService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnyDataServiceImpl implements AnyDataService {

    private final AnyDataRepository anyDataRepository;

    @Override
    @Transactional
    public AnyData insert(UserInfo userInfo, AnyDataInsertRequest anyData) {

        return anyDataRepository.save(new AnyData(userInfo,anyData));
    }

    @Override
    public AnyData read(UUID id) {

        return anyDataRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public AnyData update(UserInfo userInfo, AnyDataInsertRequest anyData) {
        return anyDataRepository.save(new AnyData(userInfo,anyData));
    }

    @Override
    @Transactional
    public void delete(UserInfo userInfo, UUID id) {
        anyDataRepository.deleteByIdAndUserInfo_Id(id,userInfo.getId());
    }

    @Override
    @Transactional
    public List<AnyData> insertAll(UserInfo userInfo, List<AnyDataInsertRequest> anyDataList) {
        List<AnyData> anyDatas = new ArrayList<>();

        anyDataList.forEach(anyDataInsertRequest ->
                anyDatas.add(insert(userInfo,anyDataInsertRequest)));

        return anyDatas;
    }

}
