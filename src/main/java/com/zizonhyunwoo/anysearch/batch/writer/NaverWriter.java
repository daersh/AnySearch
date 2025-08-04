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

    List<String> stopwords = Arrays.asList("대여","기변","기기변경","통신사","렌탈","kt","KT","lg","LG","sk","SK","미개봉","중고","새상품");

    private boolean checkData(NaverData naverData) {
        for (String stopword :  stopwords)
            if (naverData.getTitle().contains(stopword))
                return true;

        return !naverData.getCategory2().equals("휴대폰");
    }
}
