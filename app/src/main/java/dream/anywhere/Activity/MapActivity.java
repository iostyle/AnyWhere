package dream.anywhere.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.List;

import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseActivity;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.R;
import dream.anywhere.map.PoiOverlay;


/**
 * Created by SKYMAC on 16/8/24.
 */
public class MapActivity extends BaseActivity implements BaseInterface {

    private MapView mMapView;
    private EditText editText;
    private PoiSearch mPoiSearch;
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_map);
        InitView();
        InitData();
        InitViewOper();
    }

    //地图查询结果集
    List<PoiInfo> allPoi;

    //点击查询按钮
    public void onSearchClick(View v) {
        String text = editText.getText().toString().trim();
        OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
            public void onGetPoiResult(PoiResult result) {
                allPoi = result.getAllPoi();
                //获取POI检索结果
                mBaiduMap.clear();
                //创建PoiOverlay
                PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
                //设置overlay可以处理标注点击事件
                mBaiduMap.setOnMarkerClickListener(overlay);
                //设置PoiOverlay数据
                overlay.setData(result);
                //添加PoiOverlay到地图中
                overlay.addToMap();
                overlay.zoomToSpan();
                return;
            }

            public void onGetPoiDetailResult(PoiDetailResult result) {
                //获取Place详情页检索结果
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        };
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city("北京")
                .keyword(text)
                .pageNum(0));
    }

    private class MyPoiOverlay extends PoiOverlay {
        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poiInfo = allPoi.get(index);
//            String address = poiInfo.city + ",地址：" + poiInfo.address;
            showInfoWindow(poiInfo);

            return true;
        }
    }

    private void showInfoWindow(final PoiInfo poiInfo) {
        View view = getLayoutInflater().inflate(R.layout.map_popup, null);
        {
            TextView title = (TextView) view.findViewById(R.id.popup_title);
            title.setText(poiInfo.name);
            TextView content = (TextView) view.findViewById(R.id.popup_content);
            content.setText("城市：" + poiInfo.city + "\n\r地址：" + poiInfo.address + "\n\r电话" + poiInfo.phoneNum);
            Button butY = (Button) view.findViewById(R.id.popup_y);
            Button butN = (Button) view.findViewById(R.id.popup_n);
            butN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //当用户点击取消，隐藏弹出窗口
                    mBaiduMap.hideInfoWindow();
                }
            });
            butY.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyApplication.putData("poiInfo", poiInfo);
                    finish();
                }
            });
        }
        LatLng pt = poiInfo.location;
        InfoWindow mInfoWindow = new InfoWindow(view, pt, -47);
        mBaiduMap.showInfoWindow(mInfoWindow);
    }


    @Override
    public void InitView() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        editText = (EditText) findViewById(R.id.act_map_et);
        mPoiSearch = PoiSearch.newInstance();
        mBaiduMap = mMapView.getMap();
    }

    @Override
    public void InitData() {

    }

    @Override
    public void InitViewOper() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        if (mPoiSearch != null) {
            mPoiSearch.destroy();
            mPoiSearch = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}

