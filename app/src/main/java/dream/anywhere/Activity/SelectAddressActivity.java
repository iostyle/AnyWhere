package dream.anywhere.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

import dream.anywhere.Adapter.AddressListViewAdapter;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseActivity;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.Bean.AddressBean;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/7.
 */
public class SelectAddressActivity extends BaseActivity implements BaseInterface {

    public static SelectAddressActivity activity;

    @ViewInject(R.id.act_address_listview)
    private ListView listView;

    private List<AddressBean> addressBeanList;
    public static AddressListViewAdapter addressListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_address);
        InitView();
        InitData();
        InitViewOper();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (MyApplication.user.getAddressBeanList() != null) {
            addressBeanList = MyApplication.user.getAddressBeanList();
            addressListViewAdapter = new AddressListViewAdapter(addressBeanList, act, true);
            listView.setAdapter(addressListViewAdapter);
        }
    }


    @Override
    public void InitView() {
        ViewUtils.inject(act);
        activity = this;
    }

    @Override
    public void InitData() {
        if (MyApplication.user.getAddressBeanList() != null) {
            addressBeanList = MyApplication.user.getAddressBeanList();
        } else {
            addressBeanList = new ArrayList<>();
        }
        addressListViewAdapter = new AddressListViewAdapter(addressBeanList, act, true);
    }

    @Override
    public void InitViewOper() {
        listView.setAdapter(addressListViewAdapter);
    }

    //添加收货地址
    @OnClick(R.id.act_address_addLin)
    public void onAddClick(View v) {
        startAct(AddressAddActivity.class);
    }

    //点击返回
    @OnClick(R.id.act_address_backLinearLayout)
    public void onBackClick(View v){
        finish();
    }
}
