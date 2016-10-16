package dream.anywhere.Fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import dream.anywhere.Adapter.HomePageGridViewAdapter;
import dream.anywhere.Adapter.HomePageShopListViewAdapter;
import dream.anywhere.Adapter.HomePageViewPagerAdapter;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseFragment;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.Bean.ShopBean;
import dream.anywhere.R;
import dream.anywhere.Utils.ShopUtils;

/**
 * Created by SKYMAC on 16/8/30.
 */
public class HomeFragment extends BaseFragment implements BaseInterface {

    private ViewPager titleVp;
    private HomePageViewPagerAdapter adapter;
    private GridView typeGrid;
    private HomePageGridViewAdapter gridViewAdapter;
    //数据源
    private int[] typeImgResIds;
    private String[] typeTexts;

    CountDownTimer timer;

    private ListView ListView;

    private List<ShopBean> shopBeanList;
    private HomePageShopListViewAdapter ListViewAdapter;

    @Override
    protected void init() {
        InitView();
        InitData();
        InitViewOper();
    }

    @Override
    protected View initContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_homepage, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleVp = (ViewPager) view.findViewById(R.id.fragment_homepage_titleviewpager);
        adapter = new HomePageViewPagerAdapter(act);
        titleVp.setAdapter(adapter);
        typeGrid = (GridView) view.findViewById(R.id.fragment_homepage_typeGrid);
        typeImgResIds = new int[]{R.drawable.meishi, R.drawable.chaoshi, R.drawable.xianguogou, R.drawable.xianhuadangao, R.drawable.nengliangxican, R.drawable.rihanliaoli, R.drawable.zhajilingshi, R.drawable.haixianshaokao};
        typeTexts = new String[]{"美食", "超市", "鲜果购", "鲜花蛋糕", "能量西餐", "日韩料理", "炸鸡零食", "海鲜烧烤"};
        gridViewAdapter = new HomePageGridViewAdapter(typeImgResIds, typeTexts, act);
        typeGrid.setAdapter(gridViewAdapter);
        ListView = (ListView) view.findViewById(R.id.fragment_homepage_listview);

//        //模拟数据
//        List<ShopBean> shopBeanList = new ArrayList<>();
//        shopBeanList.add(new ShopBean());
//        shopBeanList.add(new ShopBean());
//        shopBeanList.add(new ShopBean());
//        shopBeanList.add(new ShopBean());
//        shopBeanList.add(new ShopBean());
//        shopBeanList.add(new ShopBean());
//        shopBeanList.add(new ShopBean());
//        shopBeanList.add(new ShopBean());
        shopBeanList = MyApplication.shops;
        if (shopBeanList == null) {
            ShopUtils.findShop(10, 0, null, new FindListener() {
                @Override
                public void done(List list, BmobException e) {
                    if (e == null) {
                        shopBeanList = list;
                    }
                }

                @Override
                public void done(Object o, Object o2) {

                }
            });
        }
        ListViewAdapter = new HomePageShopListViewAdapter(shopBeanList, act);
        ListView.setAdapter(ListViewAdapter);

        //测试动态扩展ListView
        int totalHeight = 0;
        for (int i = 0, len = ListViewAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = ListViewAdapter.getView(i, null, ListView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight() + 10; // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = ListView.getLayoutParams();
        params.height = totalHeight
                + (ListView.getDividerHeight() * (ListView.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        ListView.setLayoutParams(params);


        if (timer == null) {
            timer = new CountDownTimer(10000000, 5000) {
                @Override
                public void onTick(long l) {
                    if (titleVp.getCurrentItem() == 0) {
                        titleVp.setCurrentItem(1);
                    } else {
                        titleVp.setCurrentItem(0);
                    }
                }

                @Override
                public void onFinish() {

                }
            };
        }
        timer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void InitView() {
    }

    @Override
    public void InitData() {
    }

    @Override
    public void InitViewOper() {
    }
}
