package dream.anywhere.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
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

/**
 * Created by SKYMAC on 16/9/11.
 */
public class MyShouCangActivity extends BaseActivity implements BaseInterface {

    private List<ShopBean> shopBeanList;
    @ViewInject(R.id.act_my_shoucang_listview)
    private ListView listView;
    private HomePageShopListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_my_shoucang);
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
        shopBeanList = new ArrayList<>();
        //对shoobeanlist赋值
        for (int i = 0; i < MyApplication.user.getShopObjectList().size(); i++) {
            BmobQuery<ShopBean> query = new BmobQuery<ShopBean>();
            query.addWhereEqualTo("objectId", MyApplication.user.getShopObjectList().get(i));
            query.findObjects(new FindListener<ShopBean>() {
                @Override
                public void done(List<ShopBean> list, BmobException e) {
                    shopBeanList.add(list.get(0));
                    if (shopBeanList.size() == MyApplication.user.getShopObjectList().size()) {
                        adapter = new HomePageShopListViewAdapter(shopBeanList, act);
                        listView.setAdapter(adapter);
                    }
                }
            });
        }
    }

    @Override
    public void InitViewOper() {

    }

    @OnClick(R.id.act_my_shoucang_backImg)
    public void onBackClick(View v) {
        finish();
    }
}
