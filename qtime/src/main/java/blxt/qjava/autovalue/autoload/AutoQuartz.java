package blxt.qjava.autovalue.autoload;

import blxt.qjava.autovalue.inter.Scheduled;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.quartz.QuartzManager;
import org.quartz.Job;

@AutoLoadFactory(name="AutoQuartz", annotation = Scheduled.class, priority = 20)
public class AutoQuartz extends AutoLoadBase {
    @Override
    public  <T> T inject(Class<?> object) throws Exception {

        Scheduled scheduled = object.getAnnotation(Scheduled.class);

        if (!Job.class.isAssignableFrom(object)) {
            throw new IllegalArgumentException(
                    object.getName() +   "拥有Scheduled注解的类,需要实现org.org.quartz.Job接口.");
        }

        String JOB_GROUP_NAME = object.getPackage().getName();
        String TRIGGER_GROUP_NAME = object.getPackage().getName();

        QuartzManager.addJob(object.getName(), JOB_GROUP_NAME,
                object.getName(), TRIGGER_GROUP_NAME,
                object,
                scheduled.cron());
        return null;
    }

}
