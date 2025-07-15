package com.zizonhyunwoo.anysearch.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// 구분자 처리용
@Slf4j
public class ParsingUtil {

    public static Map<String, String> parseData(String addInfo, String addDetail) {

        String delimiter = "†";

        int count = addInfo.split(delimiter).length;
        List<String> keys = Arrays.asList(addInfo.split(delimiter));
        List<String> values = Arrays.asList(addDetail.split(delimiter));
        Map<String, String> data = new HashMap<>();
        for (int i = 0; i < count; i++) {
            try {
                data.put(keys.get(i), values.get(i));
            }catch (Exception e){
                log.error(e.getMessage());
                break;
            }
        }
        return data;
    }
}
