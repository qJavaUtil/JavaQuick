
## qtime 模块
* 时间工具
* 基于Quartz的定时器 


### 定时器实现 

* 1. 内部方法定时器, 全局使用同一个对象，支持框架内实例化  
```java
@Component
public class TimeTask{

    int i = 0;

    @Scheduled(cron="0/1 * * * * ?")
    public void taks1(){
        System.out.println("taks1: " + new Date() + " " +  i++);
    }

    @Scheduled(cron="0/5 * * * * ?")
    public void taks2(){
        System.out.println("taks2: " + new Date() + " " +  i++);
    }

}

```

* 2. Job任务调度，不支持框架内其他注解， 每次任务，都是一个新实例 
```java
@Scheduled(cron="0/1 * * * * ?")
public class TimeTask2 implements Job {

    int i = 0;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("TimeTask2: " + new Date() + i++);
    }
}

```    
