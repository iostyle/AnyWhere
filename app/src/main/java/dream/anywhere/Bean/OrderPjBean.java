package dream.anywhere.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by SKYMAC on 16/9/9.
 */
public class OrderPjBean extends BmobObject {
    private String orderNum;
    private String text;
    private Float star;
    private UserBean user;
    //是否为优质评价
    private Boolean isGood;
    //商户id
    private String shopObjectId;

    public String getShopObjectId() {
        return shopObjectId;
    }

    public void setShopObjectId(String shopObjectId) {
        this.shopObjectId = shopObjectId;
    }

    public Boolean getGood() {
        return isGood;
    }

    public void setGood(Boolean good) {
        isGood = good;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Float getStar() {
        return star;
    }

    public void setStar(Float star) {
        this.star = star;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}
