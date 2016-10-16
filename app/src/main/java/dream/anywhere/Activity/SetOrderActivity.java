package dream.anywhere.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;
import java.util.Random;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import dream.anywhere.Adapter.SetOrderListViewAdapter;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseActivity;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.Bean.AddressBean;
import dream.anywhere.Bean.BuyFoodBean;
import dream.anywhere.Bean.GiftBean;
import dream.anywhere.Bean.OrderBean;
import dream.anywhere.Bean.ShopBean;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/6.
 */
public class SetOrderActivity extends BaseActivity implements BaseInterface {

    //记录购买的商户
    private ShopBean shop;

    //用来保存从Application中取出的购物车信息
    private List<BuyFoodBean> buyFoodBeanList;
    //商户
    @ViewInject(R.id.act_setorder_shopNameTv)
    private TextView shopNameTv;
    //总计钱数 折扣前
    @ViewInject(R.id.act_setOrder_ZongJiTv)
    private TextView zongJiTv;
    //折扣后的价钱
    @ViewInject(R.id.act_setorder_paymoneyTv)
    private TextView payMoneyTv;
    //折扣价格
    @ViewInject(R.id.act_setOrder_YouHuiTv)
    private TextView yhMoneyTv;
    //配送费
    @ViewInject(R.id.act_setOrder_PeiSongTv)
    private TextView psMoneyTv;

    //购物车listview adapter
    private SetOrderListViewAdapter setOrderListViewAdapter;

    //购物车listview
    @ViewInject(R.id.act_setorder_foodListView)
    private ListView foodListView;


    @ViewInject(R.id.act_setOrder_address_name)
    private TextView addressNameTv;
    @ViewInject(R.id.act_setOrder_address_sex)
    private TextView addressSexTv;
    @ViewInject(R.id.act_setOrder_address_phone)
    private TextView addressPhoneTv;
    @ViewInject(R.id.act_setOrder_address_address)
    private TextView addressAddressTv;

    private AddressBean addressBean;

    //红包
    @ViewInject(R.id.act_setOrder_GiftLin)
    public static LinearLayout giftLin;

    //红包内容
    @ViewInject(R.id.act_setOrder_GiftTv)
    private TextView giftTv;

    //红包对象
    private GiftBean gift;
    //红包索引 用于操作
    private int giftPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setorder);
        shop = (ShopBean) MyApplication.getData("ClickShop", false);
        logE("SetOrder1:" + shop.toString());
        buyFoodBeanList = (List<BuyFoodBean>) MyApplication.getData("BuyFoods", false);
        InitView();
        InitData();
        InitViewOper();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        AddressBean address = (AddressBean) MyApplication.getData("selectAddress", true);
        if (address != null) {
            addressBean = address;
            addressNameTv.setText(address.getName());
            addressSexTv.setText(address.getSex());
            addressPhoneTv.setText(address.getPhone());
            addressAddressTv.setText(address.getAddress() + " " + address.getSite());
        }
        GiftBean giftBean = (GiftBean) MyApplication.getData("SetOrderGift", true);
        if (giftBean != null) {
            giftPosition = (int) MyApplication.getData("SetOrderGiftPosition", true);
            gift = giftBean;
            giftTv.setText(gift.getMoney() + "元");
            //修改优惠信息
            yhMoneyTv.setText((Double.parseDouble(yhMoneyTv.getText().toString())) + Double.parseDouble(gift.getMoney().toString()) + "");
            //修改总价
            payMoneyTv.setText((Double.parseDouble(payMoneyTv.getText().toString()) - Double.parseDouble(gift.getMoney().toString())) + "");
        }
    }

    @Override
    public void InitView() {
        ViewUtils.inject(this);
    }

    @Override
    public void InitData() {
        setOrderListViewAdapter = new SetOrderListViewAdapter(buyFoodBeanList, act);
    }

    @Override
    public void InitViewOper() {
        foodListView.setAdapter(setOrderListViewAdapter);


        int totalHeight = 0;
        for (int i = 0, len = setOrderListViewAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = setOrderListViewAdapter.getView(i, null, foodListView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight() + 10; // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = foodListView.getLayoutParams();
        params.height = totalHeight
                + (foodListView.getDividerHeight() * (foodListView.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        foodListView.setLayoutParams(params);


        //红包
        if (MyApplication.user.getGiftBeanList().size() > 0) {
            giftTv.setText("点击选择红包");
            giftTv.setTextColor(Color.parseColor("#000000"));
        }

        shopNameTv.setText(shop.getName());

        //计算总价 折扣前
        Double money = 0.0;
        for (int i = 0; i < ShopActivity.buyFoodBeanList.size(); i++) {
            money += ShopActivity.buyFoodBeanList.get(i).getFood().getFoodPrice() * ShopActivity.buyFoodBeanList.get(i).getSum();
        }

        zongJiTv.setText(money.toString());


        //折扣后价格
        Double payMoney = money;
        //折扣价格
        if (money >= 60) {
            payMoney = money - 20;
        } else if (money >= 35) {
            payMoney = money - 15;
        } else if (money >= 25) {
            payMoney = money - 12;
        }

        //优惠价格
        Double yhMoney = (money - payMoney);
        yhMoneyTv.setText(yhMoney.toString());

        //配送价格
        Integer psMoney = shop.getPeisongfei();
        psMoneyTv.setText(psMoney.toString());

        payMoney += psMoney;
        payMoneyTv.setText(payMoney.toString());


    }

    @OnClick(R.id.act_setorder_backLinearLayout)
    public void onBackClick(View v) {
        MyApplication.getData("BuyFoods", true);
        finish();
    }

    //点击红包
    @OnClick(R.id.act_setOrder_GiftLin)
    public void OnGiftClick(View v) {
        startAct(SelectGiftActivity.class);
    }

    //点击地址
    @OnClick(R.id.act_setOrder_addressLin)
    public void onAddressClick(View v) {
        startAct(SelectAddressActivity.class);
    }

    //提交订单
    @OnClick(R.id.act_setorder_submitTv)
    private void onSubmitClick(View v) {
        if (addressBean == null) {
            toastS("请选择收货地址");
            return;
        }


        OrderBean order = new OrderBean();
        order.setAddress(addressBean);
        order.setBuyFoodBeanList(buyFoodBeanList);
        order.setUser(MyApplication.user);

        order.setShop(shop);
        order.setShopObjectId(shop.getObjectId());

        logE("SetOrder2:" + shop.toString());
        logE("SetOrder3:" + order.getShopObjectId().toString());
        order.setTotalPrice(Double.parseDouble(zongJiTv.getText().toString()));
        order.setOffPrice(Double.parseDouble(yhMoneyTv.getText().toString()));
        order.setSendPrice(Double.parseDouble(psMoneyTv.getText().toString()));
        order.setPayPrice(Double.parseDouble(payMoneyTv.getText().toString()));
        order.setPay(false);
        order.setReceive(false);

//        ShopBean ss = new ShopBean();
//        ss.setSaleCount(shop.getSaleCount() + 1);
//        ss.update(shop.getObjectId(), new UpdateListener() {
//            @Override
//            public void done(BmobException e) {
//
//            }
//        });

        Random r = new Random();
        String num = "";
        for (int i = 0; i < 16; i++) {

            int mint = r.nextInt(62 + 50);
            if (mint < 10) {
                num += mint;
            } else if (mint < 36) {//10 -- 35      小写字母a 是 97   大写字母A65
                //转换大写字母
                mint += 55;
                num += ((char) mint);
            } else if (mint < 62) {
                //36-61
                //转换大写字母
                mint += 61;
                num += ((char) mint);
            } else {
                mint = mint % 10;
                num += mint;
            }

        }
        order.setOrderNum(num);
        order.setPj(false);

        order.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    if (gift != null) {
                        //使用红包
                        MyApplication.user.getGiftBeanList().get(giftPosition).setUsed(true);
                        MyApplication.user.update(MyApplication.user.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                //清空缓存
                                MyApplication.getData("ClickShop", true);
                                MyApplication.getData("BuyFoods", true);
                                toastS("恭喜您，下单成功！");
                                finish();
                                ShopActivity.activity.finish();
                                HomeActivity.vp.setCurrentItem(1);
                            }
                        });
                    } else {
                        //未使用红包
                        //清空缓存
                        MyApplication.getData("ClickShop", true);
                        MyApplication.getData("BuyFoods", true);
                        toastS("恭喜您，下单成功！");
                        finish();
                        ShopActivity.activity.finish();
                        HomeActivity.vp.setCurrentItem(1);
                    }
                } else {
                    toastS("下单失败");
                }
            }
        });
    }
}
