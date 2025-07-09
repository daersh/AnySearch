package com.zizonhyunwoo.anysearch.response;

import com.zizonhyunwoo.anysearch.domain.AnyData;

import java.time.LocalDateTime;

public record AnyDataResponse (
        String id,
        String type,
        String title,
        String description,
        String addInfo,
        String addDetail,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean isActive,
        UserInfoResponse userInfo
){
}
