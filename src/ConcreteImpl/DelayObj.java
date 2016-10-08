package ConcreteImpl;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangxiaang on 16/10/8.
 * 一个标准的可延迟对象
 */
public class DelayObj implements Delayed {
    private String content;
    private long expiryTime;
    private long diff = 0l;

    public DelayObj(String content, long expiryTime) {
        this.content = content;
        this.expiryTime = expiryTime + System.currentTimeMillis();
    }


    public String getContent() {
        return content;
    }

    public long getExpiryTime() {
        return expiryTime - System.currentTimeMillis();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        //什么是diff?
        diff = expiryTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (this.expiryTime < ((DelayObj) o).expiryTime) {
            return -1;
        }
        if (this.expiryTime > ((DelayObj) o).expiryTime) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "DelayObj{" +
                "content='" + content + '\'' +
                ", expiryTime=" + expiryTime +
                ", diff=" + diff +
                '}';
    }
}
