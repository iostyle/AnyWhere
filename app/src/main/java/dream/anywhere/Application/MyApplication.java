package dream.anywhere.Application;

import android.app.Application;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import c.b.BP;
import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import dream.anywhere.Bean.ShopBean;
import dream.anywhere.Bean.UserBean;
import dream.anywhere.Utils.ShopUtils;

/**
 * Created by SKYMAC on 16/8/30.
 */
public class MyApplication extends Application {
    //百度Gf05MYVmaH4lOQ7TfXZ88f5Nilj9xSMS

    //用来存储数据
    private static LinkedHashMap<String, Object> datas = new LinkedHashMap<>();
    //用来存放当前登陆的用户
    public static UserBean user = null;

    //图片缓存路径
    public static File file;

    public static List<ShopBean> shops;

    private static findShopsListener listener;

    public static void findShops() {
        ShopUtils.findShop(10, 0, null, new FindListener<ShopBean>() {
            @Override
            public void done(List<ShopBean> list, BmobException e) {
                if (e == null) {
                    MyApplication.shops = list;
                    if (MyApplication.listener != null) {
                        MyApplication.listener.findFinish();
                    }
                }
            }
        });
    }

    public static void setFindListener(findShopsListener listener) {
        MyApplication.listener = listener;
    }

    public interface findShopsListener {
        void findFinish();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BmobSMS.initialize(this, "d444aa5b7c5a79de48199781aac392f6");
        Bmob.initialize(this, "d444aa5b7c5a79de48199781aac392f6");
        BP.init(this,"d444aa5b7c5a79de48199781aac392f6");
        SDKInitializer.initialize(getApplicationContext());
        initBaiDuLBS();
        initFile();
        shops = new ArrayList<>();
        findShops();
    }

    public static Object putData(String key, Object value) {
        return datas.put(key, value);
    }

    public static Object getData(String key, Boolean isDelete) {
        if (isDelete) {
            return datas.remove(key);
        } else {
            return datas.get(key);
        }
    }

    private LocationClient mLocationClient;

    public static BDLocation bdLocation;

    private void initBaiDuLBS() {
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                MyApplication.bdLocation = bdLocation;
                Log.i("MAP", "Application.location:" + bdLocation.getLongitude() + "," + bdLocation.getLatitude());
                Log.i("MAP", bdLocation.getLocType() + "");
            }
        });
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 3000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    private void initFile() {
        file = new File("sdcard/imageCache");
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
