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
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(runnable, i + "");//id属性
            thread.start();
        }

        //todo 模拟多线程读出msg
        GetRunnable getRunnable = new GetRunnable(queue);
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(getRunnable, i + "");//id属性
            thread.start();
        }
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