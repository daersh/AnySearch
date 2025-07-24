package com.zizonhyunwoo.anysearch.request;

import org.springframework.web.multipart.MultipartFile;

public record AnyDataRequestWithFile(
        MultipartFile file
) {
}
