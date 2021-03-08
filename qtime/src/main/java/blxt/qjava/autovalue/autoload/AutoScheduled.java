package blxt.qjava.autovalue.autoload;

import blxt.qjava.autovalue.inter.Component;
import blxt.qjava.autovalue.inter.Scheduled;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.autovalue.util.ObjectValue;
import blxt.qjava.quartz.QJob;
import blxt.qjava.quartz.QuartzManager;
import blxt.qjava.utils.check.CheckUtils;

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

            String job_name = object.getName() + "." +  method.getName();

            String group = valuename.group();
            String trigger_group = object.getPackage().getName();
            String cron = valuename.cron();
            String zone = valuename.zone();
            long fixedDelay = valuename.fixedDelay();
            String fixedDelayString = valuename.fixedDelayString();
            long fixedRate = valuename.fixedRate();
            String fixedRateString= valuename.fixedRateString();
            long initialDelay= valuename.initialDelay();
            String initialDelayString= valuename.initialDelayString();
            if (cron.startsWith("$")){
                cron = cron.substring(1);
                cron = (String) ObjectValue.getObjectValue(bean, cron, String.class);
            }

            if(CheckUtils.isEmpty(group)){
                group = object.getPackage().getName();
            }
            if(CheckUtils.isEmpty(trigger_group)){
                trigger_group = object.getPackage().getName();
            }

            QuartzManager.addJob(job_name, group, job_name, trigger_group,
                    new QJob(bean, method),  cron);

        }

        return bean;
    }


}
