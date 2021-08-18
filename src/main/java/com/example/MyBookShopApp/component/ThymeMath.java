package com.example.MyBookShopApp.component;

import org.springframework.stereotype.Component;

@Component
public class ThymeMath {
    public int ceil(double value) {
        return (int) Math.ceil(value);
    }
}
