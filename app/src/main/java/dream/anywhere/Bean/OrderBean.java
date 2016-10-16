package dream.anywhere.Bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by SKYMAC on 16/9/6.
 */
public class OrderBean extends BmobObject {
    //收货信息
    private AddressBean address;
    //购物车信息
    private List<BuyFoodBean> buyFoodBeanList;
    //购买用户
    private UserBean user;
    //商家
    private ShopBean shop;
    private String shopObjectId;
    //总价
    private Double totalPrice;
    //优惠
    private Double offPrice;
    //配送费
    private Double sendPrice;
    //实付款
    private Double payPrice;
    //是否完成支付
    private Boolean isPay;
    //是否确认收货
    private Boolean isReceive;
    //是否已经评价
    private Boolean isPj;

    public Boolean getPj() {
        return isPj;
    }

    public void setPj(Boolean pj) {
        isPj = pj;
    }

    //订单号码
    private String orderNum;

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
    //    public OrderBean() {
//        super();
//    }
//
//    public OrderBean(AddressBean address, List<BuyFoodBean> buyFoodBeanList, UserBean user, ShopBean shop, Double totalPrice, Double offPrice, Double sendPrice, Double payPrice, Boolean isPay, Boolean isReceive) {
//        super();
//        this.address = address;
//        this.buyFoodBeanList = buyFoodBeanList;
//        this.user = user;
//        this.shop = shop;
//        this.totalPrice = totalPrice;
//        this.offPrice = offPrice;
//        this.sendPrice = sendPrice;
//        this.payPrice = payPrice;
//        this.isPay = isPay;
//        this.isReceive = isReceive;
//    }


    public AddressBean getAddress() {
        return address;
    }

    public void setAddress(AddressBean address) {
        this.address = address;
    }

    public ShopBean getShop() {
        return shop;
    }

    public void setShop(ShopBean shop) {
        this.shop = shop;
    }


    public String getShopObjectId() {
        return shopObjectId;
    }

    public void setShopObjectId(String shopObjectId) {
        this.shopObjectId = shopObjectId;
    }

    public Double getOffPrice() {
        return offPrice;
    }

    public void setOffPrice(Double offPrice) {
        this.offPrice = offPrice;
    }

    public Double getSendPrice() {
        return sendPrice;
    }

    public void setSendPrice(Double sendPrice) {
        this.sendPrice = sendPrice;
    }

    public Double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Double payPrice) {
        this.payPrice = payPrice;
    }

    public Boolean getReceive() {
        return isReceive;
    }

    public void setReceive(Boolean receive) {
        isReceive = receive;
    }

    public List<BuyFoodBean> getBuyFoodBeanList() {
        if (isPay == null) {
            isPay = false;
        }
        return buyFoodBeanList;
    }

    public void setBuyFoodBeanList(List<BuyFoodBean> buyFoodBeanList) {
        this.buyFoodBeanList = buyFoodBeanList;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public Boolean getPay() {
        return isPay;
    }

    public void setPay(Boolean pay) {
        isPay = pay;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

//    //商户名
//    private String shopName;
//    //配送时间
//    private Integer sendTime;
//    //联系电话
//    private String shopPhone;
//    //商家地址
//    private String shopAddress;
//    //标记
//    private List<String> flag;
//    //商户ID 用来查找本地缓存地址
//    private String shopObjectId;
//    //logo
//    private BmobFile shopLogo;
//
//

    @Override
    public String toString() {
        return "OrderBean{" +
                "address=" + address +
                ", buyFoodBeanList=" + buyFoodBeanList +
                ", user=" + user +
                ", shopObjectId='" + shopObjectId + '\'' +
                ", totalPrice=" + totalPrice +
                ", offPrice=" + offPrice +
                ", sendPrice=" + sendPrice +
                ", payPrice=" + payPrice +
                ", isPay=" + isPay +
                ", isReceive=" + isReceive +
                '}';
    }
}
