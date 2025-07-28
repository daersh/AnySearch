package com.zizonhyunwoo.anysearch.batch;

import com.zizonhyunwoo.anysearch.batch.process.AnyDataProcessor;
import com.zizonhyunwoo.anysearch.anyData.domain.AnyData;
import com.zizonhyunwoo.anysearch.user.domain.UserInfo;
import com.zizonhyunwoo.anysearch.elastic.index.AnyDataDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AnyDataBatchTest {
    UserInfo userInfo = new UserInfo(
            "name",
            "password",
            "email",
            "nickname",
            "ROLE_ADMIN"
    );

    // 1. processor 테스트
    @Autowired
    AnyDataProcessor anyDataProcessor;

    @Test
    public void AnyData_파싱_테스트(){
        String keys="addInfo1†addInfo2†addInfo3";
        String values="addDetail1†addDetail2†addDetail3";
        String delimiter = "†";

        int count = keys.length();
        List<String> keyList = Arrays.asList(keys.split(delimiter));
        List<String> valueList = Arrays.asList(values.split(delimiter));

        Map<String, String> data = new HashMap<>();
        for (int i = 0; i < count; i++) {
            try {
                data.put(keyList.get(i), valueList.get(i));
            }catch (Exception e){
                break;
            }
        }

        assertNotNull(data.get("addInfo1"));
        assertNotNull(data.get("addInfo2"));
        assertNotNull(data.get("addInfo3"));
        assertEquals("addDetail1", data.get("addInfo1"));
        assertEquals("addDetail2", data.get("addInfo2"));
        assertEquals("addDetail3", data.get("addInfo3"));
    }

    @Test
    public void AnyData_변환_테스트() {
        AnyData anyData = new AnyData(
                UUID.randomUUID(),
                "type",
                "title",
                "description",
                "addInfo1†addInfo2†addInfo3",
                "addDetail1†addDetail2†addDetail3",
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                userInfo
        );

        AnyDataDocument res =anyDataProcessor.process(anyData);

        assertNotNull(res);
        assertEquals(res.getId(), anyData.getId().toString());
        assertEquals(res.getTitle(), anyData.getTitle());
        assertEquals(res.getDescription(), anyData.getDescription());

        Map<String,String> addInfos = res.getAdditionalFields();
        assertNotNull(addInfos.get("addInfo1"));
        assertNotNull(addInfos.get("addInfo2"));
        assertNotNull(addInfos.get("addInfo3"));
        assertEquals("addDetail1", addInfos.get("addInfo1"));
        assertEquals("addDetail2", addInfos.get("addInfo2"));
        assertEquals("addDetail3", addInfos.get("addInfo3"));
    }

    // 2. writer 테스트
    @Test
    public void AnyData_writer_테스트(){

    }
}
