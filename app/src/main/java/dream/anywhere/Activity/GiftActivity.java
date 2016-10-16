package dream.anywhere.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

import dream.anywhere.Adapter.GiftListViewAdapter;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseActivity;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.Bean.GiftBean;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/11.
 */
public class GiftActivity extends BaseActivity implements BaseInterface {

    @ViewInject(R.id.act_gift_listview)
    private ListView listView;
    //红包List对象
    private List<GiftBean> giftBeanList;
    //填充adapter
    private GiftListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_gift);
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
        giftBeanList = MyApplication.user.getGiftBeanList();
        adapter = new GiftListViewAdapter(giftBeanList, act);
    }

    @Override
    public void InitViewOper() {
        listView.setAdapter(adapter);
    }

    //点击返回
    @OnClick(R.id.act_gift_backImg)
    public void onBackClick(View v) {
        finish();
    }
}
