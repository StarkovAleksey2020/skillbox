package com.example.MyBookShopApp.component;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;

@Component
@Api("Library of service math functions")
public class ThymeMath {
    @ApiOperation("Rounding function")
    public int ceil(double value, double discount) {
        return (int) Math.ceil(value * 100 / (100 - discount));
    }
}
