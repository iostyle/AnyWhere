package dream.anywhere.Bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by SKYMAC on 16/8/31.
 */
public class UserBean extends BmobUser {

    private String nickName;

    private BmobFile userIcon;

    public BmobFile getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(BmobFile userIcon) {
        this.userIcon = userIcon;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    private Double money;

    public Double getMoney() {
        if (money == null) {
            money = 0.0;
        }
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    //收货地址
    private List<AddressBean> addressBeanList;

    public List<AddressBean> getAddressBeanList() {
        return addressBeanList;
    }

    public void setAddressBeanList(List<AddressBean> addressBeanList) {
        this.addressBeanList = addressBeanList;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "nickName='" + nickName + '\'' +
                ", userIcon=" + userIcon +
                ", money=" + money +
                ", addressBeanList=" + addressBeanList +
                '}';
    }

    //收藏的店铺
    private List<String> shopObjectList;

    public List<String> getShopObjectList() {
        if (shopObjectList == null) {
            shopObjectList = new ArrayList<String>();
        }
        return shopObjectList;
    }

    public void setShopObjectList(List<String> shopObjectList) {
        this.shopObjectList = shopObjectList;
    }

    //红包
    private List<GiftBean> giftBeanList;

    public List<GiftBean> getGiftBeanList() {
        if (giftBeanList == null) {
            giftBeanList = new ArrayList<>();
        }
        return giftBeanList;
    }

    public void setGiftBeanList(List<GiftBean> giftBeanList) {
        this.giftBeanList = giftBeanList;
    }
}
