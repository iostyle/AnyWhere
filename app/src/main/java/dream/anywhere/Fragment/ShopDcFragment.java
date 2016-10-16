package dream.anywhere.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import dream.anywhere.Adapter.ShopDcListViewAdapter;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseFragment;
import dream.anywhere.Bean.FoodBean;
import dream.anywhere.Bean.ShopBean;
import dream.anywhere.R;
import dream.anywhere.Utils.ShopUtils;

/**
 * Created by SKYMAC on 16/9/5.
 */
public class ShopDcFragment extends BaseFragment {

    private ListView listView;
    private ShopDcListViewAdapter adapter;
    private List<FoodBean> foodBeanList;

    @Override
    protected void init() {
    }

    @Override
    protected View initContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_shop_diancai, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.fragment_shop_diancai_listview);
//        foodBeanList = new ArrayList<>();
//        foodBeanList.add(new FoodBean());
//        foodBeanList.add(new FoodBean());
//        foodBeanList.add(new FoodBean());
//        foodBeanList.add(new FoodBean());
//        foodBeanList.add(new FoodBean());
//        foodBeanList.add(new FoodBean());
//        foodBeanList.add(new FoodBean());
//        foodBeanList.add(new FoodBean());
//        foodBeanList.add(new FoodBean());
//        ShopUtils.findShop(99, 0, null, new FindListener() {
//            @Override
//            public void done(List list, BmobException e) {
//                if (e == null) {
//                    foodBeanList = list;
//                    adapter = new ShopDcListViewAdapter(foodBeanList, act);
//                    listView.setAdapter(adapter);
//                } else {
//
//                }
//            }
//        });
        BmobQuery<FoodBean> query = new BmobQuery<>();
        query.setLimit(99);
        query.order("-createAt");
        query.addWhereEqualTo("shop", MyApplication.getData("ClickShop", false));
        query.findObjects(new FindListener<FoodBean>() {
            @Override
            public void done(List<FoodBean> list, BmobException e) {
                if (e == null) {
                    //如果有菜品就添加
                    if(list!=null) {
                        foodBeanList = list;
                    }else{
                        foodBeanList = new ArrayList<FoodBean>();
                    }
                    adapter = new ShopDcListViewAdapter(foodBeanList, act);
                    listView.setAdapter(adapter);
                } else {

                }
            }
        });

    }
}
