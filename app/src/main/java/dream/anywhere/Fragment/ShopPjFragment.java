package dream.anywhere.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import dream.anywhere.Adapter.ShopPjListViewAdapter;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseFragment;
import dream.anywhere.Bean.OrderPjBean;
import dream.anywhere.Bean.ShopBean;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/5.
 */
public class ShopPjFragment extends BaseFragment {

    private ListView listView;
    private List<OrderPjBean> orderPjBeanList;
    private ShopPjListViewAdapter adapter;

    @Override
    protected void init() {
        ViewUtils.inject(act);
    }

    @Override
    protected View initContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_shop_pingjia, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.fragment_shop_pingjia_listview);

        ShopBean shop = (ShopBean) MyApplication.getData("ClickShop", false);
        BmobQuery<OrderPjBean> query = new BmobQuery<OrderPjBean>();
        query.setLimit(20);
        //添加查询条件
        query.order("-createdAt");
        query.addWhereEqualTo("shopObjectId", shop.getObjectId());
        query.findObjects(new FindListener<OrderPjBean>() {
            @Override
            public void done(List<OrderPjBean> list, BmobException e) {
                orderPjBeanList = list;
                adapter = new ShopPjListViewAdapter(orderPjBeanList, act);
                listView.setAdapter(adapter);
            }
        });
    }
}
