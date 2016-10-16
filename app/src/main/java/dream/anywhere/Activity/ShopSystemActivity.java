package dream.anywhere.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import dream.anywhere.Adapter.HomePageShopListViewAdapter;
import dream.anywhere.Adapter.ShopSystemListViewAdapter;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseActivity;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.Bean.ShopBean;
import dream.anywhere.R;
import dream.anywhere.Utils.ShopUtils;

/**
 * Created by SKYMAC on 16/9/5.
 */
public class ShopSystemActivity extends BaseActivity implements BaseInterface {

    @ViewInject(R.id.act_shopsystem_listview)
    private ListView listView;

    private ShopSystemListViewAdapter adapter;

    private List<ShopBean> shopBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_shopsystem);
        InitView();
        InitData();
        InitViewOper();
    }

    @Override
    public void InitView() {
        ViewUtils.inject(this);
    }

    @Override
    public void InitData() {
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
        adapter = new ShopSystemListViewAdapter(shopBeanList, act);

    }

    @Override
    public void InitViewOper() {
        listView.setAdapter(adapter);
    }

    @OnClick(R.id.act_shopsystem_backLinearLayout)
    public void onBackClick(View v){
        finish();
    }
}
