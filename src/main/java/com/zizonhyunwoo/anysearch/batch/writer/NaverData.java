package com.zizonhyunwoo.anysearch.batch.writer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class NaverData {
    String title;
    String link;
    String image;
    String lprice;
    String hprice;
    String mallName;
    String productId;
    String productType;
    String brand;
    String maker;
    String category1;
    String category2;
    String category3;
    String category4;
}
