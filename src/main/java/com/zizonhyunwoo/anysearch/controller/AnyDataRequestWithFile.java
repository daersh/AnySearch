package com.zizonhyunwoo.anysearch.controller;

import org.springframework.web.multipart.MultipartFile;

public record AnyDataRequestWithFile(
        MultipartFile file
) {
}
