import ConcreteImpl.MyBlockingQueue;
import container.MessageEnvelope;

/**
 * Created by zhangxiaang on 16/9/27.
 */
public class BlockingQueueDemo {

    public static void main(String[] args) {
        MyBlockingQueue queue = new MyBlockingQueue(20);

        //todo 模拟一个多线程写入msg
        PutRunnable runnable = new PutRunnable(queue, 2, "Aaron");
//        for (int i = 0; i < 30; i++) {//模拟put阻塞操作
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(runnable, i + "");//id属性
            thread.start();
        }

        //todo 模拟多线程读出msg
        GetRunnable getRunnable = new GetRunnable(queue);
//        for (int i = 0; i < 4; i++) {//模拟阻塞读取
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(getRunnable, i + "");//id属性
            thread.start();
        }

        //test adjustElement()
        MessageEnvelope[] array = new MessageEnvelope[10];
        array[3] = new MessageEnvelope(3, "Ar");
        array[4] = new MessageEnvelope(4, "ron");
        array[6] = new MessageEnvelope(6, "haha");
        array[8] = new MessageEnvelope(8, "e");
        queue.adjustElement(array);
    }

    static class GetRunnable implements Runnable {
        MyBlockingQueue q;

        public GetRunnable(MyBlockingQueue q) {
            this.q = q;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 3; i++) {
                    MessageEnvelope msg = q.take();
                    System.out.println("取出的信封的信息为:" + msg.toString());
                }
            } catch (InterruptedException e) {
//                e.addSuppressed(e);//addSuppressed()是干嘛的?
                e.printStackTrace();
            }

        }
    }

    static class PutRunnable implements Runnable {
        MyBlockingQueue q;
        int id;
        String name;

        public PutRunnable(MyBlockingQueue q, int id, String name) {
            this.q = q;
            this.id = id;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                q.put(new MessageEnvelope((int) (Math.random() * 1000), this.name));
                q.put(new MessageEnvelope((int) (Math.random() * 1000), this.name));
                q.put(new MessageEnvelope((int) (Math.random() * 1000), this.name));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}