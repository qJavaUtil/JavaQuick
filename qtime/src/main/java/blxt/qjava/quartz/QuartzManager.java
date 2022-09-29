package blxt.qjava.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.HashMap;
import java.util.Map;

public class QuartzManager {
    static String JOB_GROUP_NAME = "默认";
    static String TRIGGER_NAME = "默认";
    static String TRIGGER_GROUP_NAME = "默认";

    static Map<String, String> jobMaps = new HashMap<>();

    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    /**
     * @param jobName          任务名
     * @param jobClass         任务
     * @param cron             时间设置，参考quartz说明文档
     * @Description: 添加一个定时任务
     */
    public static void addJob(String jobName, Class jobClass, String cron) {
        addJob(jobName, JOB_GROUP_NAME, TRIGGER_NAME, TRIGGER_GROUP_NAME, jobClass, cron);
    }

    /**
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass         任务
     * @param cron             时间设置，参考quartz说明文档
     * @Description: 添加一个定时任务
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void addJob(String jobName, String jobGroupName,
                              String triggerName, String triggerGroupName, Class jobClass, String cron) {

        // 任务名，任务组，任务执行类
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
        // 触发器
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
        // 触发器名,触发器组
        triggerBuilder.withIdentity(triggerName, triggerGroupName);
        triggerBuilder.startNow();
        // 触发器时间设定
        triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
        // 创建Trigger对象
        CronTrigger trigger = (CronTrigger) triggerBuilder.build();
        try {
            jobMaps.put(jobName, "on");
            // 启动调度器
            addJob(jobDetail, trigger);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass         任务
     * @param cron             时间设置，参考quartz说明文档
     * @Description: 添加一个定时任务
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void addJob(String jobName, String jobGroupName,
                              String triggerName, String triggerGroupName,
                              Job jobClass, String cron) {

        // 任务名，任务组，任务执行类
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();

        // 触发器
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
        // 触发器名,触发器组
        triggerBuilder.withIdentity(triggerName, triggerGroupName);
        triggerBuilder.startNow();
        // 触发器时间设定
        triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
        // 创建Trigger对象
        CronTrigger trigger = (CronTrigger) triggerBuilder.build();

        try {
            jobMaps.put(jobName, "on");
            // 启动调度器
            addJob(jobDetail, trigger);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加并启动调度
     *
     * @param jobDetail
     * @param trigger
     */
    static void addJob(JobDetail jobDetail, CronTrigger trigger) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            // 调度容器设置JobDetail和Trigger
            sched.scheduleJob(jobDetail, trigger);

            // 启动
            if (!sched.isShutdown()) {
                sched.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param jobName
     * @param jobGroupName
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param cron             时间设置，参考quartz说明文档
     * @Description: 修改一个任务的触发时间
     */
    public static void modifyJobTime(String jobName,
                                     String jobGroupName, String triggerName, String triggerGroupName, String cron) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }

            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) {
                /** 方式一 ：调用 rescheduleJob 开始 */
                // 触发器
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                // 触发器名,触发器组
                triggerBuilder.withIdentity(triggerName, triggerGroupName);
                triggerBuilder.startNow();
                // 触发器时间设定
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
                // 创建Trigger对象
                trigger = (CronTrigger) triggerBuilder.build();
                // 方式一 ：修改一个任务的触发时间
                sched.rescheduleJob(triggerKey, trigger);
                /** 方式一 ：调用 rescheduleJob 结束 */

                /** 方式二：先删除，然后在创建一个新的Job  */
                //JobDetail jobDetail = sched.getJobDetail(JobKey.jobKey(jobName, jobGroupName));
                //Class<? extends Job> jobClass = jobDetail.getJobClass();
                //removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
                //addJob(jobName, jobGroupName, triggerName, triggerGroupName, jobClass, cron);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     * @Description: 移除一个任务
     */
    public static void removeJob(String jobName, String jobGroupName,
                                 String triggerName, String triggerGroupName) {
        try {
            jobMaps.put(jobName, "off");
            Scheduler sched = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            sched.pauseTrigger(triggerKey);// 停止触发器
            sched.unscheduleJob(triggerKey);// 移除触发器
            sched.deleteJob(JobKey.jobKey(jobName, jobGroupName));// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断任务是否存在
     *
     * @param jobName
     * @return
     */
    public static boolean isExist(String jobName) {
        return jobMaps.get(jobName) != null;
    }

    /**
     * @Description:启动所有定时任务
     */
    public static void startJobs() {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            sched.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:关闭所有定时任务
     */
    public static void shutdownJobs() {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            if (!sched.isShutdown()) {
                sched.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    public static void main(String[] args) throws Exception {
//
//        String JOB_NAME = "动态任务调度";
//        String TRIGGER_NAME = "动态任务触发器";
//        String JOB_GROUP_NAME = "XLXXCC_JOB_GROUP";
//        String TRIGGER_GROUP_NAME = "XLXXCC_JOB_GROUP";
//
//        try {
//            System.out.println("【系统启动】开始(每1秒输出一次)...");
//            QuartzManager.addJob(JOB_NAME, JOB_GROUP_NAME, TRIGGER_NAME, TRIGGER_GROUP_NAME, MJob.class, "0/1 * * * * ?");
//
//            Thread.sleep(5000);
//            System.out.println("【修改时间】开始(每5秒输出一次)...");
//            QuartzManager.modifyJobTime(JOB_NAME, JOB_GROUP_NAME, TRIGGER_NAME, TRIGGER_GROUP_NAME, "0/5 * * * * ?");
//
//            Thread.sleep(6000);
//            System.out.println("【移除定时】开始...");
//            QuartzManager.removeJob(JOB_NAME, JOB_GROUP_NAME, TRIGGER_NAME, TRIGGER_GROUP_NAME);
//            System.out.println("【移除定时】成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}