package com.zizonhyunwoo.anysearch.service.impl;

import com.zizonhyunwoo.anysearch.dao.AnyDataRepository;
import com.zizonhyunwoo.anysearch.domain.AnyData;
import com.zizonhyunwoo.anysearch.domain.UserInfo;
import com.zizonhyunwoo.anysearch.request.AnyDataInsertRequest;
import com.zizonhyunwoo.anysearch.service.AnyDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnyDataServiceImpl implements AnyDataService {

    private final AnyDataRepository anyDataRepository;

    @Override
    public AnyData insert(UserInfo userInfo, AnyDataInsertRequest anyData) {

        return anyDataRepository.save(new AnyData(userInfo,anyData));
    }

    @Override
    public AnyData read(UUID id) {

        return anyDataRepository.findById(id).orElse(null);
    }

    @Override
    public AnyData update(UserInfo userInfo, AnyDataInsertRequest anyData) {
        return anyDataRepository.save(new AnyData(userInfo,anyData));
    }

    @Override
    public void delete(UserInfo userInfo, UUID id) {
        anyDataRepository.deleteByIdAndUserInfo_Id(id,userInfo.getId());
    }

}
