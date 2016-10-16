package dream.anywhere.Activity;

import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import dream.anywhere.Base.BaseActivity;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/5.
 */
public class MoreActivity extends BaseActivity implements BaseInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_more);
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

    }

    @Override
    public void InitViewOper() {

    }

    //点击返回
    @OnClick(R.id.act_more_backLinearLayout)
    public void onBackClick(View v){
        finish();
    }

    //点击更多
    @OnClick(R.id.act_more_shopLin)
    public void onShopClick(View v){
        startAct(ShopSystemActivity.class);
    }
}
