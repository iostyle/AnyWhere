package dream.anywhere.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UpdateListener;
import dream.anywhere.Adapter.ShopActViewPagerAdapter;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseActivity;
import dream.anywhere.Base.BaseFragment;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.Bean.BuyFoodBean;
import dream.anywhere.Bean.ShopBean;
import dream.anywhere.Bean.UserBean;
import dream.anywhere.Fragment.ShopDcFragment;
import dream.anywhere.Fragment.ShopPjFragment;
import dream.anywhere.Fragment.ShopSjFragment;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/5.
 */
public class ShopActivity extends BaseActivity implements BaseInterface {

    @ViewInject(R.id.act_shop_viewpager)
    private ViewPager vp;
    private ShopActViewPagerAdapter adapter;
    private List<BaseFragment> fragments;

    @ViewInject(R.id.act_shop_diancaiTv)
    private TextView DcTv;
    @ViewInject(R.id.act_shop_pingjiaTv)
    private TextView PjTv;
    @ViewInject(R.id.act_shop_shangjiaTv)
    private TextView SjTv;
    @ViewInject(R.id.act_shop_diancaiView)
    private View DcView;
    @ViewInject(R.id.act_shop_pingjiaView)
    private View PjView;
    @ViewInject(R.id.act_shop_shangjiaView)
    private View SjView;

    private View[] views;

    @ViewInject(R.id.act_shop_shopName)
    private TextView shopNameTv;
    @ViewInject(R.id.act_shop_shopImg)
    private ImageView shopImg;

    private ShopBean shop;
    //配送费
    @ViewInject(R.id.act_shop_peisongfeiTv)
    private TextView psMoneyTv;
    //总价TextView
    @ViewInject(R.id.act_shop_moneyTv)
    public static TextView moneyTv;
    //总价
    public static Double money = 0.0;

    //用来存放购买的商品  购物车
    public static List<BuyFoodBean> buyFoodBeanList = new ArrayList<>();

    @ViewInject(R.id.act_shop_butTv)
    public static TextView submitTv;

    public static ShopActivity activity;

    //收藏
    @ViewInject(R.id.act_shop_shoucangTv)
    private TextView shouCangTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_shop);
        shop = (ShopBean) MyApplication.getData("ClickShop", false);
        InitView();
        InitData();
        InitViewOper();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //初始化数据
//        money = 0.0;
//        buyFoodBeanList = new ArrayList<>();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //判断是否已收藏
        if (MyApplication.user.getShopObjectList().contains(shop.getObjectId())) {
            shouCangTv.setText("已收藏");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void InitView() {
        ViewUtils.inject(this);
        activity = this;
        psMoneyTv.setText(shop.getPeisongfei().toString());
        moneyTv.setText(money.toString());
    }

    @Override
    public void InitData() {
        fragments = new ArrayList<>();
        fragments.add(new ShopDcFragment());
        fragments.add(new ShopPjFragment());
        fragments.add(new ShopSjFragment());
        adapter = new ShopActViewPagerAdapter(getSupportFragmentManager(), fragments);

        TextView[] textViews = new TextView[]{DcTv, PjTv, SjTv};
        views = new View[]{DcView, PjView, SjView};

        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    flush(view);
                }
            });
        }

        shopNameTv.setText(shop.getName());
        //二级缓存商家图片
        final File file = new File(MyApplication.file, "/" + shop.getObjectId() + "/" + shop.getObjectId() + ".jpeg");
        Bitmap bit = BitmapFactory.decodeFile(file.getAbsolutePath());
        if (bit == null) {
            BmobFile bFile = shop.getLogoImg();
            bFile.download(file.getAbsoluteFile(), new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        //bit设置不了final
                        final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        Log.i("MyFile", "======BitmapFactory.decodeFile(file.getAbsolutePath():=====" + file.getAbsoluteFile());
                        //第二步 设置图片
                        shopImg.setImageBitmap(bitmap);
                        //第三部 缓存到内存中
//                    cache.put(str32, bitmap);
                    }
                }

                @Override
                public void onProgress(Integer integer, long l) {

                }
            });
        } else {
            shopImg.setImageBitmap(bit);
        }
    }

    @Override
    public void InitViewOper() {
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(3);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                flush2(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //判断是否已收藏
        if (MyApplication.user != null) {
            if (MyApplication.user.getShopObjectList().contains(shop.getObjectId())) {
                shouCangTv.setText("已收藏");
            }
        }
    }

    //点击刷新
    private void flush(View view) {
        int position = 0;
        switch (view.getId()) {
            case R.id.act_shop_diancaiTv:
                position = 0;
                break;
            case R.id.act_shop_pingjiaTv:
                position = 1;
                break;
            case R.id.act_shop_shangjiaTv:
                position = 2;
                break;
        }
        vp.setCurrentItem(position);
        for (int i = 0; i < 3; i++) {
            if (i == position) {
                views[i].setVisibility(View.VISIBLE);
            } else {
                views[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    //滑动刷新
    private void flush2(int position) {
        for (int i = 0; i < 3; i++) {
            if (i == position) {
                views[i].setVisibility(View.VISIBLE);
            } else {
                views[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    //点击返回
    @OnClick(R.id.act_shop_backImg)
    public void onBackClick(View v) {
        showBackDialog();
    }

    @Override
    public void onBackPressed() {
        showBackDialog();
    }

    private AlertDialog backDialog;

    private void showBackDialog() {
        if (backDialog == null) {
            backDialog = new AlertDialog.Builder(this).setTitle("提示").setMessage("确定离开吗？这将清空您的购物车。").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    money = 0.0;
                    buyFoodBeanList = new ArrayList<>();
                    finish();
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    backDialog.dismiss();
                }
            }).create();
        }
        backDialog.show();
    }

    //点击下单
    @OnClick(R.id.act_shop_butTv)
    public void setOrderClick(View v) {
        MyApplication.putData("BuyFoods", buyFoodBeanList);
        startAct(SetOrderActivity.class);
    }

    @OnClick(R.id.act_shop_shoucangTv)
    public void onShouCangClick(View v) {
        if (shouCangTv.getText().equals("点击收藏")) {
            MyApplication.user.getShopObjectList().add(shop.getObjectId());
            MyApplication.user.update(MyApplication.user.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        MyApplication.user = BmobUser.getCurrentUser(UserBean.class);
                        shouCangTv.setText("已收藏");
                        toastS("收藏成功");
                    }
                }
            });
        } else {
            MyApplication.user.getShopObjectList().remove(shop.getObjectId());
            MyApplication.user.update(MyApplication.user.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        MyApplication.user = BmobUser.getCurrentUser(UserBean.class);
                        shouCangTv.setText("点击收藏");
                        toastS("取消收藏");
                    }
                }
            });
        }
    }
}
