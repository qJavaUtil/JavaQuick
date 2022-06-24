import blxt.qjava.qthread.QThreadpool;
import blxt.qjava.qthread.delayed.AbstractDelayedObject;


/**
 * 延时任务测试
 */
public class AbstractDelayedObjecTest extends AbstractDelayedObject<Integer> {

    public AbstractDelayedObjecTest(){

    }


    /**
     * 数据响应
     *
     * @param object
     */
    @Override
    public Integer onTimeOver(final Integer object){
        System.out.println("结果:" + object);
        // 打印结果,然后清空
        return 0;
    }

    @Override
    public Integer onUpdateValue(Integer old, Integer newValue) {
        // 这里对数据进行累加
        return old + newValue;
    }


    public static void main(String[] args) throws InterruptedException {
        // 创建线程池,只需要创建1次
        QThreadpool.newInstance(5, 10, 60);

        AbstractDelayedObjecTest test = new AbstractDelayedObjecTest();
        // 设置初始值
        test.setValue(0);
        // 设置定时
        test.setDelay(3000);
        test.start();
        // 这10个数据一起打印
        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            System.out.println("提交:" + i);
            test.put( i);
        }

        // 这1个数据单独打印
        Thread.sleep(4000);
        // 这个是自定义的数据处理接口
        test.put(11, (oldValue, newValue) -> {
            System.out.println("old:" + oldValue);
            System.out.println("newValue:" + newValue);
            return 1011;
        });

        // 取消任务
        Thread.sleep(10000);
        System.out.println("取消任务");
        test.cancel();
    }
}
