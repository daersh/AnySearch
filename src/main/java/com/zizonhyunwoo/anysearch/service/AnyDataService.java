package com.zizonhyunwoo.anysearch.service;

import com.zizonhyunwoo.anysearch.domain.AnyData;
import com.zizonhyunwoo.anysearch.domain.UserInfo;
import com.zizonhyunwoo.anysearch.request.AnyDataInsertRequest;

import java.util.List;
import java.util.UUID;

public interface AnyDataService {
    public AnyData insert(UserInfo userInfo, AnyDataInsertRequest anyData);

    AnyData read(UUID id);

    AnyData update(UserInfo userInfo, AnyDataInsertRequest anyData);

    void delete(UserInfo userInfo, UUID id);

    List<AnyData> insertAll(UserInfo userInfo, List<AnyDataInsertRequest> anyDataList);
}
