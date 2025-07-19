package com.zizonhyunwoo.anysearch.service;

import com.zizonhyunwoo.anysearch.controller.AnyDataRequestWithFile;
import com.zizonhyunwoo.anysearch.domain.UserInfo;
import com.zizonhyunwoo.anysearch.request.AnyDataInsertRequest;
import com.zizonhyunwoo.anysearch.response.AnyDataResponse;

import java.util.List;
import java.util.UUID;

public interface AnyDataService {
    AnyDataResponse insert(UserInfo userInfo, AnyDataInsertRequest anyData);

    AnyDataResponse read(String id);

    List<AnyDataResponse> findAll(int page, int size);

    AnyDataResponse update(UserInfo userInfo, AnyDataInsertRequest anyData);

    void delete(UserInfo userInfo, UUID id);

    List<AnyDataResponse> insertAll(UserInfo userInfo, List<AnyDataInsertRequest> anyDataList);

    List<String> getDataType();

    void insertFile(AnyDataRequestWithFile request);
}
