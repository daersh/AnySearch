package com.zizonhyunwoo.anysearch.controller;

import com.zizonhyunwoo.anysearch.domain.AnyData;
import com.zizonhyunwoo.anysearch.domain.UserInfo;
import com.zizonhyunwoo.anysearch.request.AnyDataInsertRequest;
import com.zizonhyunwoo.anysearch.service.AnyDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/any_data")
@RequiredArgsConstructor
public class AnyDataController {

    private final AnyDataService anyDataService;

    @PostMapping
    public AnyData insert(@AuthenticationPrincipal UserInfo userInfo, @RequestBody AnyDataInsertRequest anyData) {
        return anyDataService.insert(userInfo, anyData);
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
