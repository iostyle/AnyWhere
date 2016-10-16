package dream.anywhere.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseActivity;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.Bean.OrderBean;
import dream.anywhere.Bean.OrderPjBean;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/9.
 */
public class OrderPjActivity extends BaseActivity implements BaseInterface {

    @ViewInject(R.id.act_order_pj_Et)
    private EditText editText;
    @ViewInject(R.id.act_order_pj_RatingBar)
    private RatingBar ratingBar;

    //订单信息
    private OrderBean order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_order_pj);
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
        order = (OrderBean) MyApplication.getData("PjOrder", true);
    }

    @Override
    public void InitViewOper() {

    }

    @OnClick(R.id.act_order_pj_backImg)
    public void onBackClick(View v) {
        finish();
    }

    //提交评价
    @OnClick(R.id.act_order_pj_submitTv)
    public void onSubmitClick(View v) {
        String text = editText.getText().toString();
        if (text.length() < 5) {
            toastS("评价内容过短");
            return;
        }
//        toastS(ratingBar.getRating()+"");
        OrderPjBean pj = new OrderPjBean();
        pj.setOrderNum(order.getOrderNum());
        pj.setText(text);
        pj.setStar(ratingBar.getRating());
        pj.setUser(MyApplication.user);
        pj.setGood(false);
        pj.setShopObjectId(order.getShopObjectId());

        //保存评价
        pj.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    order.setPj(true);
                    //更新订单
                    order.update(order.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            toastS("评价成功");

                            /**
                             * 这里可以添加一个获得红包的途径
                             */


                            finish();

                        }
                    });
                }
            }
        });
    }
}
