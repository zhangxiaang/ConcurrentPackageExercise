import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by zhangxiaang on 16/9/27.
 */
public class CyclicBarrierDemo {
    private static CyclicBarrier myBarrier;
    private static int parties = 5;
    private static int total_val = 0;

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

        myBarrier.reset();//体现出跟Latch的不同,也就是可以重新使用

        //模拟多线程计算数据
//        myBarrier = new CyclicBarrier(parties);
//        Counter counter = new Counter(myBarrier, 10);
//        for (int i = 0; i < parties; i++) {
//            Thread thread = new Thread(counter);
//            thread.start();
//        }
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

    static class Counter implements Runnable {
        CyclicBarrier barrier;
        int counts;

        public Counter(CyclicBarrier cyclicBarrier, int number) {
            this.barrier = cyclicBarrier;
            this.counts = number;
        }

        @Override
        public void run() {
            try {
                int result = count(counts);
                System.out.println("当前线程:" + Thread.currentThread().getName() + " 单独计算的总值为:" + result);
                barrier.await();//在counter线程没有完成计算的work前即时自己完成了计算也应该wait
                //todo 合并计算结果
                total_val += result;
                System.out.println("满足条件后的线程" + Thread.currentThread().getName() + "把自己的值添加到totalVal,此时的totalVal=" + total_val);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

        }

        private int count(int counts) {
            int result = 0;
            for (int i = 1; i <= (counts + (int) Thread.currentThread().getId()); i++) {
                result += i;
            }
            return result;
        }
    }
}
