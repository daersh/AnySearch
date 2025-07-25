package com.zizonhyunwoo.anysearch.batch.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NaverWriter implements ItemWriter<List<NaverData>> {


    @Override
    public void write(Chunk<? extends List<NaverData>> chunk) throws Exception {
        for (List<NaverData> data: chunk) {
            System.out.println("data = " + data);
        }
    }
}
