package blxt.qjava.autovalue.autoload;

import blxt.qjava.autovalue.inter.Component;
import blxt.qjava.autovalue.inter.Scheduled;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.quartz.QJob;
import blxt.qjava.quartz.QuartzManager;
import java.lang.reflect.Method;

@AutoLoadFactory(name="AutoScheduled", annotation = Component.class, priority = 20)
public class AutoScheduled extends AutoLoadBase {

    @Override
    public Object inject(Class<?> object) throws Exception {
        // 自动实现QUdpServer
        Object bean = ObjectPool.getObject(object);

        // 获取f对象对应类中的所有属性域
        Method[] methods = bean.getClass().getMethods();
        for(Method method : methods) {
            Scheduled valuename = method.getAnnotation(Scheduled.class);
            if (valuename == null) {
                continue;
            }

            String JOB_NAME = object.getName() + "." +  method.getName();
            String JOB_GROUP_NAME = object.getPackage().getName();
            String TRIGGER_GROUP_NAME = object.getPackage().getName();
            System.out.println("定时器:" + JOB_NAME);
            QuartzManager.addJob(JOB_NAME, JOB_GROUP_NAME, JOB_NAME, TRIGGER_GROUP_NAME,
                    new QJob(bean, method),
                    valuename.cron());

            String cron = valuename.cron();
            String zone = valuename.zone();
            long fixedDelay = valuename.fixedDelay();
            String fixedDelayString = valuename.fixedDelayString();
            long fixedRate = valuename.fixedRate();
            String fixedRateString= valuename.fixedRateString();
            long initialDelay= valuename.initialDelay();
            String initialDelayString= valuename.initialDelayString();

        }

        return bean;
    }


}
