import blxt.qjava.qthread.delayed.AbstractDelayedObjectList;
import blxt.qjava.qthread.QThreadpool;

import java.util.List;


/**
 * 延时任务测试.
 */
public class AbstractDelayedObjectListTest extends AbstractDelayedObjectList<String> {

    public AbstractDelayedObjectListTest(){

    }


    /**
     * 数据响应
     *
     * @param valves
     */
    @Override
    public void onTimeOver(final List<String> valves){
        System.out.println("时间:" + System.currentTimeMillis() / 1000);
        System.out.println("收到数据:长度:" + valves.size());
    }


    public static void main(String[] args) throws InterruptedException {
        // 创建线程池,只需要创建1次
        QThreadpool.newInstance(5, 10, 60);

        AbstractDelayedObjectListTest test = new AbstractDelayedObjectListTest();
        test.setDelay(3000);
        test.start();
        // 这10个数据一起打印
        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            System.out.println("提交:" + i);
            test.put("数据" + i);
        }

        // 这1个数据单独打印
        Thread.sleep(4000);
        test.put("数据11");

        // 取消任务
        Thread.sleep(10000);
        System.out.println("取消任务");
        test.cancel();
    }
}
