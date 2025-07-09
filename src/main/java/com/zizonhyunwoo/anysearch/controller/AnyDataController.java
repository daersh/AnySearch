package com.zizonhyunwoo.anysearch.controller;

import com.zizonhyunwoo.anysearch.domain.UserInfo;
import com.zizonhyunwoo.anysearch.request.AnyDataInsertRequest;
import com.zizonhyunwoo.anysearch.response.AnyDataResponse;
import com.zizonhyunwoo.anysearch.service.AnyDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/any_data")
@RequiredArgsConstructor
public class AnyDataController {

    private final AnyDataService anyDataService;

    @PostMapping
    public AnyDataResponse insert(@AuthenticationPrincipal UserInfo userInfo, @RequestBody@Validated AnyDataInsertRequest anyData) {
        return anyDataService.insert(userInfo, anyData);
    }

    @PostMapping("/list")
    public List<AnyDataResponse> list(@AuthenticationPrincipal UserInfo userInfo, @RequestBody List<AnyDataInsertRequest> anyDataList) {
        log.info("userInfo={}", userInfo);
        return anyDataService.insertAll(userInfo,anyDataList);
    }

    @GetMapping
    public AnyDataResponse findById(@AuthenticationPrincipal UserInfo userInfo, @RequestParam String id) {
        log.info("userInfo={}", userInfo);
        return anyDataService.read(id);
    }

    @GetMapping("/list")
    public List<AnyDataResponse> list(@AuthenticationPrincipal UserInfo userInfo, @RequestParam int page, @RequestParam int size) {
        return anyDataService.findAll(page,size);
    }

    @PutMapping
    public AnyDataResponse update(@AuthenticationPrincipal UserInfo userInfo, @RequestBody AnyDataInsertRequest anyData) {
        log.info("AnyData={}", anyData);
        return anyDataService.update(userInfo,anyData);
    }

    @DeleteMapping
    public void delete(@AuthenticationPrincipal UserInfo userInfo, @RequestParam UUID id) {
        anyDataService.delete(userInfo, id);
    }

}
