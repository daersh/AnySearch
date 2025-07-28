package com.zizonhyunwoo.anysearch.anyData.response;

import com.zizonhyunwoo.anysearch.user.response.UserInfoResponse;

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
