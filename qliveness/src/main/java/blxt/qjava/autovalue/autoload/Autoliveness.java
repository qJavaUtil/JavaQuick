package blxt.qjava.autovalue.autoload;

import blxt.qjava.autovalue.inter.Liveness;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.qliveness.Register;

@AutoLoadFactory(name="Autoliveness", annotation = Liveness.class, priority = 40)
public class Autoliveness extends AutoLoadBase{

    @Override
    public <T> T inject(Class<?> object) throws Exception {
        Liveness liveness = object.getAnnotation(Liveness.class);
        Register register = new Register();
        register.setArl(liveness.url());
        register.setAppTage(liveness.tag());
        register.setCron(liveness.cron());
        register.setSleep(liveness.delay());
        register.register();
        return null;
    }
}
