package test.util;

import blxt.qjava.autovalue.inter.Autowired;
import blxt.qjava.autovalue.inter.Component;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/6 15:59
 */
@Component()
public class Bean1 {
    String name = "Bean1";

    @Autowired
    private Bean2 bean2;

    public Bean1(){

    }

    @Override
    public String toString() {
        return "Bean1{" +
                "name='" + name + '\'' +
                ", bean2=" + bean2.toString() +
                '}';
    }
}
