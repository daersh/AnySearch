package com.zizonhyunwoo.anysearch.batch.writer;

import com.zizonhyunwoo.anysearch.elastic.index.AnyDataDocument;
import com.zizonhyunwoo.anysearch.elastic.index.NaverData;
import com.zizonhyunwoo.anysearch.elastic.util.ElasticsearchIndexer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class NaverWriter implements ItemWriter<List<NaverData>> {

    private final ElasticsearchIndexer elasticsearchIndexer;

    @Override
    public void write(Chunk<? extends List<NaverData>> chunk) throws Exception {
        for (List<NaverData> data: chunk) {
            System.out.println("data = " + data);
            Map<String, List<AnyDataDocument>> map = new HashMap<>();
            List<AnyDataDocument> list = new ArrayList<>();

            for (NaverData naverData : data) {
                if (checkData(naverData))
                    continue;
                AnyDataDocument anyDataDocument = new AnyDataDocument(naverData);
                // 타입 초기화
                if(!map.containsKey(anyDataDocument.getType())) {
                    map.put(anyDataDocument.getType(), new ArrayList<>());
                }
                // 데이터 저장
                map.get(anyDataDocument.getType().toLowerCase(Locale.KOREA)).add(anyDataDocument);
                list.add(anyDataDocument);
            }

            map.forEach(elasticsearchIndexer::saveData);
            elasticsearchIndexer.saveData("anydata", list);
        }
    }

    private boolean checkData(NaverData naverData) {
        return !naverData.getCategory2().equals("휴대폰") ||
                naverData.getTitle().contains("대여")||
                naverData.getTitle().contains("기변")||
                naverData.getTitle().contains("기기변경")||
                naverData.getTitle().contains("통신사")||
                naverData.getTitle().contains("렌탈")||
                naverData.getTitle().contains("kt")||
                naverData.getTitle().contains("lg")||
                naverData.getTitle().contains("sk")||
                naverData.getTitle().contains("KT")||
                naverData.getTitle().contains("LG")||
                naverData.getTitle().contains("SK");
    }
}
