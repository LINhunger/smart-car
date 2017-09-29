package entity;

/**
 * Created by 小排骨 on 2017/9/21.
 */
public class Command {

    private long carId;//小车id
    private String content;//控制指令

    public long getCarId() {
        return carId;
    }

    public void setCarId(long carId) {
        this.carId = carId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
