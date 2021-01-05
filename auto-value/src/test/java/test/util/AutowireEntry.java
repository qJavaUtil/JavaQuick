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
    @Autowired
    public AppConfiguration2 appConfiguration2;

    @Override
    public String toString() {
        return "AutowireEntry{" +
                "  \r\n appConfiguration2=" + appConfiguration2 +
                ", \r\n appConfiguration=" + appConfiguration +
                '}';
    }
}
