package ConcreteImpl;

import container.MessageEnvelope;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhangxiaang on 16/9/27.
 */
public class MyBlockingQueue implements BlockingQueue<MessageEnvelope>, Serializable {
    //todo 参考arrayBlockingQueue实现自定义的queue
    //如何使用ReentryLock?
    private static final long serialVersionUID = -817911612352898426L;
    public static final int DEFAULT_CAPCITY = 20;
    private ReentrantLock lock;
    private Condition notEmpty;//empty阻塞条件
    private Condition notFull;//full阻塞条件
    private int capcity;
    private MessageEnvelope[] array;
    private int count;//个数

    public MyBlockingQueue() {
        this(DEFAULT_CAPCITY);
    }

    public MyBlockingQueue(int capcity) {
        this.capcity = capcity;
        this.lock = new ReentrantLock(false);
        this.notEmpty = lock.newCondition();
        this.notFull = lock.newCondition();
        this.array = new MessageEnvelope[this.capcity];
    }

//    public MyBlockingQueue(int capcity, Collection<MessageEnvelope> collection) {
//        if (collection.size() > capcity) {
//            throw new IndexOutOfBoundsException("容积过小");
//        }
//        for (MessageEnvelope msg : collection) {
//            //insert into array
//        }
//    }

    @Override
    public boolean add(MessageEnvelope messageEnvelope) {
        if (offer(messageEnvelope)) {//非阻塞
            return true;
        } else
            throw new IllegalStateException("队列满员了");
    }

    @Override
    public boolean offer(MessageEnvelope messageEnvelope) {
        return false;
    }

    @Override
    public MessageEnvelope remove() {
        MessageEnvelope msg;
        if ((msg = poll()) != null) {//非阻塞
            return msg;
        } else
            throw new IllegalStateException("队列啥都没");
    }

    @Override
    public MessageEnvelope poll() {
        return null;
    }

    @Override
    public MessageEnvelope element() {
        return null;
    }

    @Override
    public MessageEnvelope peek() {
        return null;
    }

    @Override
    public void put(MessageEnvelope messageEnvelope) throws InterruptedException {
        ReentrantLock lock = this.lock;//阻塞 存元素
        lock.lockInterruptibly();
        try {
            while (count >= capcity) {
                notFull.await();
            }
            enqueue(messageEnvelope);
        } finally {
            System.out.println("现在的长度为:" + size());
            lock.unlock();
        }
    }

    //todo 阻塞 存放元素
    private void enqueue(MessageEnvelope messageEnvelope) {
        array[count] = messageEnvelope;
        count++;
        //进队列后唤醒一个notEmpty的信号
        notEmpty.signal();
    }


    @Override
    public boolean offer(MessageEnvelope messageEnvelope, long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public MessageEnvelope take() throws InterruptedException {
        ReentrantLock lock = this.lock;
        lock.lockInterruptibly();//阻塞 取元素
        try {
            while (count == 0) {
                notEmpty.await();
            }
            return dequeue();
        } finally {
            lock.unlock();
        }
    }

    //阻塞取元素
    private MessageEnvelope dequeue() {
//        final MessageEnvelope[] copy = this.array;//为何要操作副本呢?
        MessageEnvelope msg = array[count - 1];
        array[count - 1] = null;//置空
        count--;
        notFull.signal();
        return msg;
    }

    @Override
    public MessageEnvelope poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public int remainingCapacity() {
        return 0;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends MessageEnvelope> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<MessageEnvelope> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public int drainTo(Collection<? super MessageEnvelope> c) {
        return 0;
    }

    @Override
    public int drainTo(Collection<? super MessageEnvelope> c, int maxElements) {
        return 0;
    }
}
