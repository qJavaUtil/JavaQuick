package test.util;

import blxt.qjava.autovalue.inter.Component;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/6 15:59
 */
@Component()
public class Bean2 {
    String name = "Bean2";

    @Override
    public String toString() {
        return "Bean1{" +
                "name='" + name + '\'' +
                '}';
    }
}
