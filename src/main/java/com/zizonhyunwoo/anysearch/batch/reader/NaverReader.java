package com.zizonhyunwoo.anysearch.batch.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class NaverReader implements ItemReader<String> {

    private final ObjectMapper objectMapper;

    @Value("${naver.id}")
    private String id;
    @Value("${naver.key}")
    private String key;
    @Value("${naver.url}")
    private String url;
    private Boolean flag = false;
    private List<String> names = Arrays.asList("갤럭시","애플","스마트폰","smartphone");
    private int cnt =0;

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (cnt==3) {
            cnt=0;
            return null;
        }
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("query", "갤럭시")
                .queryParam("display", "10")
                .queryParam("start", "1")
                .queryParam("sort", "sim")
                .queryParam("exclude", "used:rental:cbshop");

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Naver-Client-Id",id);
        headers.add("X-Naver-Client-Secret",key);


        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        System.out.println("uriBuilder = " + uriBuilder.toUriString());
        ResponseEntity<String> result = restTemplate.exchange(
//                uriBuilder.toUriString(),
                "https://openapi.naver.com/v1/search/shop.json?query="+names.get(cnt++)+"&display=100&start=1&sort=sim&exclude=used:rental:cbshop",
                HttpMethod.GET,
                requestEntity,
                String.class);

        System.out.println("result = " + result);

        return result.getBody();
    }
}
