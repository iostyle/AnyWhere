package dream.anywhere.Bean;

/**
 * Created by SKYMAC on 16/9/11.
 */
public class GiftBean {
    private Integer money;
    //false代表可用 true 代表用过
    private boolean isUsed;

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }
}
