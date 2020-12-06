package test.util;

import blxt.qjava.autovalue.inter.Autowired;
import blxt.qjava.autovalue.inter.Component;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/5 22:41
 */
@Component()
public class AutowireEntry {

    @Autowired
    public AppConfiguration appConfiguration;

    @Override
    public String toString() {
        return "AutowireEntry{" +
                "appConfiguration=" + appConfiguration +
                '}';
    }
}
