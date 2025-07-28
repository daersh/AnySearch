package com.zizonhyunwoo.anysearch.anyData.service;

import com.zizonhyunwoo.anysearch.user.domain.UserInfo;
import com.zizonhyunwoo.anysearch.anyData.request.AnyDataInsertRequest;
import com.zizonhyunwoo.anysearch.anyData.response.AnyDataResponse;
import org.springframework.web.multipart.MultipartFile;

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

    void insertFile(MultipartFile request);
}
