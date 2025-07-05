package com.zizonhyunwoo.anysearch.domain;

import com.zizonhyunwoo.anysearch.dao.AnyDataRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestPropertySource(locations = "classpath:application_test.properties")
public class AnyDataTest {


    @Autowired
    AnyDataRepository anyDataRepository;
    AnyData insertData =new AnyData(
            null,
            "type",
            "title",
            "description",
            "addInfo",
            "addDetail",
            LocalDateTime.now(),
            LocalDateTime.now(),
            false
    );

    @Test
    @DisplayName("데이터 삽입 테스트") // Changed DisplayName for clarity
    public void 데이터_삽입_테스트() {

        insertData = anyDataRepository.save(insertData);

        assertNotNull(insertData);
        assertNotNull(insertData.getId());

    }

    @Test
    public void 데이터조회테스트(){
        insertData = anyDataRepository.save(insertData);

        AnyData anyData = anyDataRepository.findById(insertData.getId()).orElse(null);
        assertNotNull(anyData);
    }

    @Test
    public void 데이터삭제(){
        insertData = anyDataRepository.save(insertData);
        AnyData anyData = anyDataRepository.findById(insertData.getId()).orElse(null);
        assertNotNull(anyData);

        anyDataRepository.delete(anyData);

        anyData = anyDataRepository.findById(insertData.getId()).orElse(null);
        assertNull(anyData);
    }

}