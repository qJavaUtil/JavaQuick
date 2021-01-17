package blxt.qjava.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.reflect.Method;

/**
 * 自定义任务，可以执行对象的内的方法
 */
public class QJob implements Job {
    Object bean;
    Method method;

    public QJob(Object bean, Method method){
        this.bean = bean;
        this.method = method;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            method.invoke(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
