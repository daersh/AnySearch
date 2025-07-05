package com.zizonhyunwoo.anysearch.service.impl;

import com.zizonhyunwoo.anysearch.dao.AnyDataRepository;
import com.zizonhyunwoo.anysearch.domain.AnyData;
import com.zizonhyunwoo.anysearch.service.AnyDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnyDataServiceImpl extends AnyDataService {

    private final AnyDataRepository anyDataRepository;

    public AnyData create(AnyData anyData) {

        return anyDataRepository.save(anyData);
    }

    public AnyData read(UUID id) {

        return anyDataRepository.findById(id).orElse(null);
    }

    public AnyData update(AnyData anyData) {
        return anyDataRepository.save(anyData);
    }

    public void delete(UUID id) {
        anyDataRepository.deleteById(id);
    }

}
