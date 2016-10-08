import ConcreteImpl.DelayObj;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;

/**
 * Created by zhangxiaang on 16/10/8.
 */
public class DelayQueueDemo {
    public static void main(String[] args) {
        DelayObj obj1 = new DelayObj("Hello,World", 3000);
        DelayObj obj2 = new DelayObj("Hello,Aaron", 1000);
        DelayObj obj11 = new DelayObj("Hello,World2", 3000);
        DelayObj obj3 = new DelayObj("Hello,ZhangXiang", 2000);
        BlockingQueue<DelayObj> queue = new DelayQueue<DelayObj>();
        try {
            //如队列的顺序是无所谓的 出队列都是按照周期来着的
            queue.put(obj1);
            queue.put(obj2);
            queue.put(obj3);
            queue.put(obj11);

//            DelayObj obj = queue.take();
            System.out.println(queue.take().toString());
//            DelayObj obj4 = queue.take();
            System.out.println(queue.take().toString());
//            DelayObj obj5 = queue.take();
            System.out.println(queue.take().toString());
            System.out.println(queue.take().toString());
            //如果在空容器中取元素的画会造成阻塞状态
//            System.out.println(queue.take().toString());


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
