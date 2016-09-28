package container;

/**
 * Created by zhangxiaang on 16/9/27.
 */
public class MessageEnvelope {
    //假装只有一个Id和一个name属性
    int id;
    String name;

    public MessageEnvelope(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "MessageEnvelope{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
