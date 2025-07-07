package com.zizonhyunwoo.anysearch.controller;

import com.zizonhyunwoo.anysearch.domain.AnyData;
import com.zizonhyunwoo.anysearch.domain.UserInfo;
import com.zizonhyunwoo.anysearch.request.AnyDataInsertRequest;
import com.zizonhyunwoo.anysearch.service.AnyDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/any_data")
@RequiredArgsConstructor
public class AnyDataController {

    private final AnyDataService anyDataService;

    @PostMapping
    public AnyData insert(@AuthenticationPrincipal UserInfo userInfo, @RequestBody@Validated AnyDataInsertRequest anyData) {
        return anyDataService.insert(userInfo, anyData);
    }

    @PostMapping("/list")
    public List<AnyData> list(@AuthenticationPrincipal UserInfo userInfo, @RequestBody List<AnyDataInsertRequest> anyDataList) {
        log.info("userInfo={}", userInfo);
        return anyDataService.insertAll(userInfo,anyDataList);
    }

    @GetMapping
    public AnyData findAll(@AuthenticationPrincipal UserInfo userInfo, @RequestParam UUID id) {
        return anyDataService.read(id);
    }

    @PutMapping
    public AnyData update(@AuthenticationPrincipal UserInfo userInfo, @RequestBody AnyDataInsertRequest anyData) {
        return anyDataService.update(userInfo,anyData);
    }

    @DeleteMapping
    public void delete(@AuthenticationPrincipal UserInfo userInfo, @RequestParam UUID id) {
        anyDataService.delete(userInfo, id);
    }

}
