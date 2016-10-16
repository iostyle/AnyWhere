package dream.anywhere.Activity;

import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import dream.anywhere.Adapter.MyPjxListViewAdapter;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseActivity;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.Bean.OrderPjBean;
import dream.anywhere.Bean.ShopBean;
import dream.anywhere.R;
import dream.anywhere.View.XListView;

/**
 * Created by SKYMAC on 16/9/10.
 */
public class MyPjActivity extends BaseActivity implements BaseInterface {

    @ViewInject(R.id.act_my_pj_xlistview)
    private XListView xListView;

    //所有我的评价
    private List<OrderPjBean> orderPjBeanList;
    private MyPjxListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_my_pj);
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
        BmobQuery<OrderPjBean> query = new BmobQuery<OrderPjBean>();
        query.setLimit(5);
        //添加查询条件
        query.order("-createdAt");
        query.addWhereEqualTo("user", MyApplication.user);
        query.findObjects(new FindListener<OrderPjBean>() {
            @Override
            public void done(List<OrderPjBean> list, BmobException e) {
                orderPjBeanList = list;
                adapter = new MyPjxListViewAdapter(orderPjBeanList, act);
                xListView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void InitViewOper() {
//        xListView.setEnabled(true);
        xListView.setPullLoadEnable(true);
        xListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                BmobQuery<OrderPjBean> query = new BmobQuery<OrderPjBean>();
                query.setLimit(5);
                //添加查询条件
                query.order("-createdAt");
                query.addWhereEqualTo("user", MyApplication.user);
                query.findObjects(new FindListener<OrderPjBean>() {
                    @Override
                    public void done(List<OrderPjBean> list, BmobException e) {
                        xListView.stopRefresh();
                        orderPjBeanList = list;
                        adapter = new MyPjxListViewAdapter(orderPjBeanList, act);
                        xListView.setAdapter(adapter);
                    }
                });
            }

            @Override
            public void onLoadMore() {
                BmobQuery<OrderPjBean> query = new BmobQuery<OrderPjBean>();
                query.setLimit(5);
                //添加查询条件
                query.order("-createdAt");
                query.addWhereEqualTo("user", MyApplication.user);
                query.setSkip(orderPjBeanList.size());
                query.findObjects(new FindListener<OrderPjBean>() {
                    @Override
                    public void done(List<OrderPjBean> list, BmobException e) {
                        xListView.stopLoadMore();
                        list.addAll(0, orderPjBeanList);
                        orderPjBeanList = list;
                        adapter = new MyPjxListViewAdapter(orderPjBeanList, act);
                        xListView.setAdapter(adapter);
                    }
                });
            }
        });
    }

    @OnClick(R.id.act_my_pj_backImg)
    public void onBackClick(View v) {
        finish();
    }
}
