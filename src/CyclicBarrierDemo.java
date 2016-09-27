import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by zhangxiaang on 16/9/27.
 */
public class CyclicBarrierDemo {
    private static CyclicBarrier myBarrier;
    private static int parties = 5;

    public static void main(String[] args) {
        myBarrier = new CyclicBarrier(parties, new Runnable() {
            @Override
            public void run() {
                System.out.println("所有的线程都已经执行到barrier这里来,开始执行任务...执行者为:" + Thread.currentThread().getName());
            }
        });

//        myBarrier = new CyclicBarrier(parties);
        MyRunnable runnable = new MyRunnable(myBarrier);

        for (int i = 0; i < parties; i++) {
            Thread thread = new Thread(runnable);
            thread.start();
        }

//        myBarrier.reset();//体现出跟Latch的不同
    }

    static class MyRunnable implements Runnable {
        CyclicBarrier barrier;

        public MyRunnable(CyclicBarrier cyclicBarrier) {
            this.barrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + "正在等待的状态中...");
//                barrier.await(3, TimeUnit.SECONDS);
                barrier.await();
                System.out.println(Thread.currentThread().getName() + "取消阻塞状态,继续执行任务...");
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
