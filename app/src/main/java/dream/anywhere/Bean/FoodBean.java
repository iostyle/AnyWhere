package dream.anywhere.Bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by SKYMAC on 16/9/5.
 * <p/>
 * 这个类只作为商品展示 不作为订单
 */
public class FoodBean extends BmobObject {
    private ShopBean shop;
    private String foodName;
    private Double foodPrice;
    private BmobFile foodImage;
    private Integer sales;

    //类别 用来展示分类 由商家自定义 如盖饭 水饺 烧烤
    private String type;

    @Override
    public String toString() {
        return "FoodBean{" +
                "shop=" + shop +
                ", foodName='" + foodName + '\'' +
                ", foodPrice=" + foodPrice +
                ", foodImage=" + foodImage +
                ", sales=" + sales +
                ", type='" + type + '\'' +
                '}';
    }

    public Integer getSales() {
        if (sales == null) {
            sales = 0;
        }
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public ShopBean getShop() {
        return shop;
    }

    public void setShop(ShopBean shop) {
        this.shop = shop;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(Double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public BmobFile getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(BmobFile foodImage) {
        this.foodImage = foodImage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
