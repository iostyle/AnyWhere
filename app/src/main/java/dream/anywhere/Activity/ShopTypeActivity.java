package dream.anywhere.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import dream.anywhere.Adapter.HomePageShopListViewAdapter;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseActivity;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.Bean.ShopBean;
import dream.anywhere.R;
import dream.anywhere.Utils.ShopUtils;
import dream.anywhere.View.XListView;

/**
 * Created by SKYMAC on 16/9/8.
 */
public class ShopTypeActivity extends BaseActivity implements BaseInterface {

    //要展示的商户类别
    private String type;

    private List<ShopBean> shopBeanList;

    @ViewInject(R.id.act_shop_type_xlistview)
    private XListView xListView;

    private HomePageShopListViewAdapter adapter;

    @ViewInject(R.id.act_shop_typeTv)
    private TextView typeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_shop_type);
        type = (String) MyApplication.getData("ClickType", true);
        Log.e("ccc", type);
        InitView();
        InitData();
        InitViewOper();
    }

    @Override
    public void InitView() {
        ViewUtils.inject(act);
    }

    @Override
    public void InitData() {
        ShopUtils.findShop(5, 1, type, new MyFindListener());
    }

    class MyFindListener extends FindListener<ShopBean> {
        @Override
        public void done(List list, BmobException e) {
            //这是一个异步的
            if (e == null) {
                Log.e("ccc", list.toString());
                shopBeanList = list;
                adapter = new HomePageShopListViewAdapter(shopBeanList, act);
                xListView.setAdapter(adapter);
                xListView.setPullLoadEnable(true);
                xListView.setXListViewListener(new XListView.IXListViewListener() {
                    @Override
                    public void onRefresh() {
                        //下拉刷新
                        ShopUtils.findShop(5, 1, type, new MyFindListener());
                        xListView.stopRefresh();
                    }

                    @Override
                    public void onLoadMore() {
                        //加载更多
                        BmobQuery<ShopBean> query = new BmobQuery<ShopBean>();
                        query.setLimit(5);
                        query.order("-createdAt");
                        query.addWhereContains("type", type);
                        query.setSkip(shopBeanList.size());
                        //执行查询方法
                        query.findObjects(new FindListener<ShopBean>() {
                            @Override
                            public void done(List<ShopBean> list, BmobException e) {
                                if (e == null) {
                                    xListView.stopLoadMore();
                                    list.addAll(0, shopBeanList);
                                    adapter = new HomePageShopListViewAdapter(list, act);
                                    xListView.setAdapter(adapter);
                                }
                            }
                        });
                    }
                });
            } else {
                logI(e.toString());
                e.printStackTrace();
            }
        }

    }

    @Override
    public void InitViewOper() {


        typeTv.setText(type);
    }

    @OnClick(R.id.act_shop_backImg)
    public void onBackClick(View v) {
        finish();
    }
}
