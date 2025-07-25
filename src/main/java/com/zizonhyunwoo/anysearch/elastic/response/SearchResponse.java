package com.zizonhyunwoo.anysearch.elastic.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {
    Long count;
    List<?> docs;
}
