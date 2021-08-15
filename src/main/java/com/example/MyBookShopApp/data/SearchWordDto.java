package com.example.MyBookShopApp.data;

import lombok.Data;

@Data
public class SearchWordDto {

    private String example;

    public SearchWordDto(String example) {
        this.example = example;
    }

    public SearchWordDto() {
    }


}
