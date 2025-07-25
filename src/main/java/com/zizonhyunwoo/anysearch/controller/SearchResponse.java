package com.zizonhyunwoo.anysearch.controller;

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
