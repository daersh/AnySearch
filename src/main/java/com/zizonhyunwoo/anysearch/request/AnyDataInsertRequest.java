package com.zizonhyunwoo.anysearch.request;

import jakarta.validation.constraints.NotEmpty;

public record AnyDataInsertRequest (
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
        @NotEmpty
        Boolean isActive
)
{

}


