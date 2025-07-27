package com.zizonhyunwoo.anysearch.batch.writer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.beans.Encoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (flag) {
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
                "https://openapi.naver.com/v1/search/shop.json?query="+"삼성"+"&display=10&start=1&sort=sim&exclude=used:rental:cbshop",
                HttpMethod.GET,
                requestEntity,
                String.class);

        System.out.println("result = " + result);
        flag = true;

        return result.getBody();
    }
}
