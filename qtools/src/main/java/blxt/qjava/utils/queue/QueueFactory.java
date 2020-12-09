package blxt.qjava.utils.queue;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 队列工厂, 一个无界指针LinkedBlockingQueue工具
 * @Author: Zhang.Jialei
 * @Date: 2020/12/9 16:47
 */
public class QueueFactory<E> implements Runnable{
    LinkedBlockingQueue<E> queue = new LinkedBlockingQueue<>();

    OnQueueListener<E> onQueueListener = null;


    @Override
    public void run() {
        boolean isRun = true;
        while(isRun){
            E obj = null;

            try {
                obj = queue.take();
            } catch (InterruptedException e) {
                if(onQueueListener != null){
                    isRun = onQueueListener.onQueueOutError(queue);
                }
                e.printStackTrace();
            }

            if(onQueueListener != null && obj != null){
                isRun = onQueueListener.onQueueIn(obj);
            }

        }
    }

    public LinkedBlockingQueue<E> getQueue() {
        return queue;
    }

    public void setOnQueueListener(OnQueueListener<E> onQueueListener) {
        this.onQueueListener = onQueueListener;
    }

    /**
     * 队列监听
     * @param <E>
     */
    public interface OnQueueListener<E>{
        /** 如果这里为false, 则停止监听回调 */
        boolean onQueueIn(E bean);
        /** 如果这里为false, 则停止监听回调 */
        boolean onQueueOutError(LinkedBlockingQueue obj);
    }


}
