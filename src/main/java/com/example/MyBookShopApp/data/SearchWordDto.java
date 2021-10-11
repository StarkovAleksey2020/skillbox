package com.example.MyBookShopApp.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchWordDto {

    private String example;

    public SearchWordDto(String example) {
        this.example = example;
    }

    public SearchWordDto() {
    }


}
