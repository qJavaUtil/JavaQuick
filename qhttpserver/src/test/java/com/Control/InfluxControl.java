package com.Control;

import blxt.qjava.autovalue.inter.GetMapping;
import blxt.qjava.autovalue.inter.RequestMapping;

@RequestMapping(path="/data")
public class InfluxControl {


    @GetMapping(value = "/query")
    public String query(String sqlStr){
        System.out.println("查询" + sqlStr);
        return sqlStr;
    }
}
