package com.zizonhyunwoo.anysearch.elastic.request;

import org.springframework.web.multipart.MultipartFile;

public record AnyDataRequestWithFile(
        MultipartFile file
) {
}
