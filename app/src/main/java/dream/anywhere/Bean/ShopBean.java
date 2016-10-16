package dream.anywhere.Bean;

import com.baidu.mapapi.model.LatLng;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;


/**
 * Created by SKYMAC on 16/9/2.
 */
public class ShopBean extends BmobObject {
    //店名
    private String name;
    //销量
    private Integer saleCount;

    //联系电话
    private String phone;

    //店面图
    private BmobFile logoImg;
    //起送价
    private Integer qisongjia;
    //配送费
    private Integer peisongfei;

//    public ShopBean() {
//        super();
//    }
//
//    public ShopBean(String name, Integer saleCount, BmobFile logoImg, Integer qisongjia, Integer peisongfei, Integer peisongshijian, List<String> flag, List<String> type, LatLng location, String address) {
//        super();
//        this.name = name;
//        this.saleCount = saleCount;
//        this.logoImg = logoImg;
//        this.qisongjia = qisongjia;
//        this.peisongfei = peisongfei;
//        this.peisongshijian = peisongshijian;
//        this.flag = flag;
//        this.type = type;
//        this.location = location;
//        this.address = address;
//    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSaleCount() {
        if (saleCount == null) {
            saleCount = 0;
        }
        return saleCount;
    }

    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
    }

    public BmobFile getLogoImg() {
        return logoImg;
    }

    public void setLogoImg(BmobFile logoImg) {
        this.logoImg = logoImg;
    }

    public Integer getQisongjia() {
        return qisongjia;
    }

    public void setQisongjia(Integer qisongjia) {
        this.qisongjia = qisongjia;
    }

    public Integer getPeisongfei() {
        return peisongfei;
    }

    public void setPeisongfei(Integer peisongfei) {
        this.peisongfei = peisongfei;
    }

    public Integer getPeisongshijian() {
        return peisongshijian;
    }

    public void setPeisongshijian(Integer peisongshijian) {
        this.peisongshijian = peisongshijian;
    }

    public List<String> getFlag() {
        return flag;
    }

    public void setFlag(List<String> flag) {
        this.flag = flag;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    //配送时间
    private Integer peisongshijian;
    //标记 比如专送、新店什么的
    private List<String> flag;
    //类别 用来模糊查询
    private List<String> type;

    //地址经纬 用来计算距离
    private LatLng location;

    //地址信息
    private String address;

    public LatLng getLocation() {

        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    //    private BmobGeoPoint gpsAdd;

//    public BmobGeoPoint getGpsAdd() {
//        return gpsAdd;
//    }
//
//    public void setGpsAdd(BmobGeoPoint gpsAdd) {
//        this.gpsAdd = gpsAdd;
//    }


    @Override
    public String toString() {
        return "ShopBean{" +
                "name='" + name + '\'' +
                ", saleCount=" + saleCount +
                ", logoImg=" + logoImg +
                ", qisongjia=" + qisongjia +
                ", peisongfei=" + peisongfei +
                ", peisongshijian=" + peisongshijian +
                ", flag=" + flag +
                ", type=" + type +
                ", location=" + location +
                ", address='" + address + '\'' +
                '}';
    }
}
