package ConcreteImpl;

import Utils.Util;
import container.MessageEnvelope;

import java.io.Serializable;
import java.util.Arrays;
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
                System.out.println("Msg的put操作被阻塞...");
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
        //todo 实现一个延迟add的效果
        if (!Util.checkoutNotNull(messageEnvelope)) return false;

        return false;
    }

    @Override
    public MessageEnvelope take() throws InterruptedException {
        ReentrantLock lock = this.lock;
        lock.lockInterruptibly();//阻塞 取元素
        try {
            while (count == 0) {
                System.out.println("取Msg被阻塞住了");
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
        MessageEnvelope msg = null;
        for (int i = 0; i < capcity; i++) {
            if (array[i] != null) {
                //pop the head of queue
                msg = array[i];
                array[i] = null;//置空
                break;
            }
        }
        count--;
        notFull.signal();
        return msg;
    }

    //快速在原来的array基础上调整元素,空出位置给后来的元素
    public boolean adjustElements() {
        if (count >= capcity) {
            return false;
        }
        //浪费了空间 尝试不额外的添加内存
        MessageEnvelope[] copy = new MessageEnvelope[capcity];
        int pointer = 0;
        for (MessageEnvelope msg : array) {
            if (msg != null) {
                copy[pointer] = msg;
                ++pointer;
            }
        }

        if (pointer == count) {
            return true;
        }//检测一下是否调整完全
        return false;
    }

    //压缩array
    public boolean adjustElement(MessageEnvelope[] array) {
        if (count >= capcity) {
            return false;
        }
        System.out.println("Before:" + Arrays.toString(array));
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                for (int j = i + 1; j < array.length; j++) {
                    if (array[j] != null) {
                        array[i] = array[j];
                        array[j] = null;
                        break;
                    }
                }
            }
        }
        System.out.println("After:" + Arrays.toString(array));
        return true;
    }

    @Override
    public MessageEnvelope poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public int remainingCapacity() {
        return capcity - count;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index == -1) {
            return false;
        }
        array[index] = null;
        count--;
        return true;
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
        MessageEnvelope msg = (MessageEnvelope) o;
        for (MessageEnvelope temp : array) {
            if (temp == msg) {
                return true;
            }
        }
        return false;
    }

    private int indexOf(Object o) {
        if (!contains(o)) {
            return -1;
        }
        for (int i = 0; i < capcity; i++) {
            if (array[i] == o) {
                return i;
            }
        }
        return -1;
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
