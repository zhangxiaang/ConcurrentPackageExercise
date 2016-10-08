### TARGET:

* To learn the usage of java.utils.concurrent package
* familiar with the proper usage cases..

* CyclicBarrier的使用场景: 当需要多个线程在没有满足某个条件之前需要等待直到条件满足后
才可以继续的执行;典型的案例就是多个线程各自计算各自的数据在各自的数据计算完成之刻等待其他
的线程都计算完成后计算总共的数据(合并计算结果)。

* BlockingQueue:首先实现了这个接口的队列都是线程安全的,多线程的enqueue/dequeue都是
没有问题的;
    * 最重要的就是掌握ReentriantLock的lock()和lockInterruptibly()的区别:前者遇到
    条件不满足的话直接
    
* DelayQueue: 取出一个元素就使线程进入死循环中，如果条件(notEmpty+timeOut)不满足
就释放刚刚持有的headElement引用,如果head元素的delay值小于等于0就直接poll出来返回给调用
者,否则的话就生成一个leader线程,在等待了delay时长后直接取出来返回即可.
* 有个问题:为什么我put的顺序是按照delay值从长到短压入队列的但是在take操作的时候反而queue
已经是按照delay值从小到大的顺序拍好了的呢? 这个queue是一个priorityQueue,在压入队列的时候
已经根据delay拍好正确的顺序了。原因是啥呢?－－> delayObj实现了Delayed的compareTo函数，就是根据
expiryTime拍好顺序的。