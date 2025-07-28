package com.zizonhyunwoo.anysearch.anyData.request;

import jakarta.validation.constraints.NotEmpty;

public record AnyDataInsertRequest (

        String id,
        @NotEmpty
        String type,
        @NotEmpty
        String title,
        @NotEmpty
        String description,
        @NotEmpty
        String addInfo,
        @NotEmpty
        String addDetail,
        Boolean isActive
)
{

}


