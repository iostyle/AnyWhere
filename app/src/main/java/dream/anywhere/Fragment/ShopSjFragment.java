package dream.anywhere.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseFragment;
import dream.anywhere.Bean.ShopBean;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/5.
 */
public class ShopSjFragment extends BaseFragment {

    private ShopBean shop;
    //    @ViewInject(R.id.fragment_shop_phoneTv)
    private TextView phoneTv;
    //    @ViewInject(R.id.fragment_shop_address)
    private TextView addressTv;
    //    @ViewInject(R.id.fragment_shop_timeTv)
    private TextView timeTV;
    //    @ViewInject(R.id.fragment_shop_sendTv)
    private TextView sendTv;

    @Override
    protected void init() {
        ViewUtils.inject(act);
    }

    @Override
    protected View initContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_shop_shangjia, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        phoneTv = (TextView) view.findViewById(R.id.fragment_shop_phoneTv);
        addressTv = (TextView) view.findViewById(R.id.fragment_shop_address);
        timeTV = (TextView) view.findViewById(R.id.fragment_shop_timeTv);
        sendTv = (TextView) view.findViewById(R.id.fragment_shop_sendTv);
        shop = (ShopBean) MyApplication.getData("ClickShop", false);
        if (shop != null) {
            phoneTv.setText(shop.getPhone());
            addressTv.setText(shop.getAddress());
            timeTV.setText(shop.getPeisongshijian() + "");
            if (shop.getFlag().contains("美团专送")) {
                sendTv.setText("美团");
            } else {
                sendTv.setText("商家");
            }
        }
    }
}
