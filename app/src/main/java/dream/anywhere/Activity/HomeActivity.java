package dream.anywhere.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import dream.anywhere.Adapter.HomeViewPagerAdapter;
import dream.anywhere.Adapter.OrderListViewAdapter;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseActivity;
import dream.anywhere.Base.BaseFragment;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.Bean.GiftBean;
import dream.anywhere.Bean.OrderBean;
import dream.anywhere.Bean.UserBean;
import dream.anywhere.Fragment.HomeFragment;
import dream.anywhere.Fragment.MineFragment;
import dream.anywhere.Fragment.OrderFragment;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/8/30.
 */
public class HomeActivity extends BaseActivity implements BaseInterface, View.OnClickListener {

    //viewpager
    @ViewInject(R.id.act_home_viewpager)
    public static ViewPager vp;
    //viewpager adapter
    public static HomeViewPagerAdapter adapter;
    //viewpager数据源
    public static List<BaseFragment> fragments;

    //标签页图片
    @ViewInject(R.id.act_home_homepageImageView)
    private ImageView homeImg;
    @ViewInject(R.id.act_home_orderImageView)
    private ImageView orderImg;
    @ViewInject(R.id.act_home_mineImageView)
    private ImageView mineImg;

    private LinearLayout[] linearLayouts = new LinearLayout[3];
    //存放LinearLayout的id
    private int[] linResIds;

    //用来存放按钮对象
    private ImageView[] butImgs;
    //用来存放按钮图片索引 点击状态
    private int[] butOnResIds;
    //未点击状态
    private int[] butOffResIds;

    //登陆前的订单页面
    @ViewInject(R.id.fragment_order_relative_before)
    private RelativeLayout relativeOrderBefore;

    //登陆后的订单页面
    @ViewInject(R.id.fragment_order_relative_after)
    private RelativeLayout relativeOrderAfter;

    //订单页面 9月7日
    @ViewInject(R.id.fragment_order_relative_listview_Lin)
    private RelativeLayout relativeLayoutOrder;

    //订单listview
    @ViewInject(R.id.fragment_order_relative_listview_Lin_listview)
    private ListView orderListView;

    //个人界面的 登陆/注册 登陆后切换为用户名
    @ViewInject(R.id.fragment_mine_userName)
    private TextView mineUserNameTv;

    //个人界面 个人信息栏
    @ViewInject(R.id.fragment_mine_userRelativeLayout)
    private RelativeLayout mineUserRelative;

    //加载个人信息时 弹出加载框
//    public static ProgressDialog progressDialog;

    public static HomeActivity homeAct;

    //个人页面的头像
    @ViewInject(R.id.fragment_mine_userIcon)
    public static ImageView userIconImg;

    //订单listview adapter
    public static OrderListViewAdapter orderListViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logI("HomeActivity ---> onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home);
        InitView();
        InitData();
        InitViewOper();
    }

    @Override
    protected void onStart() {
        logI("HomeActivity ---> onStart");
        super.onStart();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                LoginFlushViewUtils.flushView();
//            }
//        }).start();

//        vp.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        logI("HomeActivity ---> onResume");
        super.onResume();
        if (MyApplication.user != null) {
//            Flush();
        }
//        LoginFlushViewUtils.flushView();
//        vp.getAdapter().notifyDataSetChanged();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                LoginFlushViewUtils.flushView();
//            }
//        });
    }

    @Override
    protected void onPause() {
        logI("HomeActivity ---> onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        logI("HomeActivity ---> onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        logI("HomeActivity ---> onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        logI("HomeActivity ---> onRestart");
        super.onRestart();
        /**
         * 我希望在这里处理当注册成功后 或 登陆成功后
         * 返回HomeActivity 对各个Fragment进行刷新操作
         */

//        LoginFlushViewUtils.flushView();
        if (MyApplication.user != null) {
            Flush();
        } else {
            //退出登陆
            userIconImg.setImageResource(R.drawable.defaultusericon);
            mineUserNameTv.setText("登陆/注册");
            relativeOrderBefore.setVisibility(View.VISIBLE);
            relativeOrderAfter.setVisibility(View.INVISIBLE);
            relativeLayoutOrder.setVisibility(View.INVISIBLE);
            userIconImg.setClickable(false);
        }

        if (MyApplication.user != null) {
            BmobQuery<OrderBean> query = new BmobQuery<>();
            query.setLimit(10);
            //添加查询条件
            query.order("-createdAt");
            query.addWhereEqualTo("user", new BmobPointer(MyApplication.user));
            query.findObjects(new FindListener<OrderBean>() {
                @Override
                public void done(List<OrderBean> list, BmobException e) {
                    if (list.size() > 0) {
                        logI(list.toString());
                        relativeOrderBefore.setVisibility(View.INVISIBLE);
                        relativeOrderAfter.setVisibility(View.INVISIBLE);
                        relativeLayoutOrder.setVisibility(View.VISIBLE);
                        orderListViewAdapter = new OrderListViewAdapter(list, act);
                        orderListView.setAdapter(orderListViewAdapter);
//                        Log.i("sky", list.get(0).getShop().getName());
                    } else {
                        relativeOrderBefore.setVisibility(View.INVISIBLE);
                        relativeOrderAfter.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

    }

    @Override
    public void InitView() {
        ViewUtils.inject(this);
        homeAct = this;
        logI("HomeActivity ---> ViewUtils.inject(this)");
//        if (progressDialog == null) {
//            progressDialog = createProgressDialog(null, "正在加载", false);
//        }
//        progressDialog.show();
    }

    @Override
    public void InitData() {
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new OrderFragment());
        fragments.add(new MineFragment());
        adapter = new HomeViewPagerAdapter(getSupportFragmentManager(), fragments);
        butImgs = new ImageView[]{homeImg, orderImg, mineImg};
        linResIds = new int[]{R.id.act_home_homepageLinearLayout, R.id.act_home_orderLinearLayout, R.id.act_home_mineLinearLayout};
        butOnResIds = new int[]{R.drawable.home_homepage_sel, R.drawable.home_order_sel, R.drawable.home_mine_sel};
        butOffResIds = new int[]{R.drawable.home_homepage_nor, R.drawable.home_order_nor, R.drawable.home_mine_nor};


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
                //刷新按钮图片
                updateButtonImg(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        for (int i = 0; i < 3; i++) {
            linearLayouts[i] = findLin(linResIds[i]);
            linearLayouts[i].setOnClickListener(this);
        }


        if (MyApplication.user != null) {
            BmobQuery<OrderBean> query = new BmobQuery<>();
            query.setLimit(10);
            //添加查询条件
            query.order("-createdAt");
            query.addWhereEqualTo("user", new BmobPointer(MyApplication.user));
            query.findObjects(new FindListener<OrderBean>() {
                @Override
                public void done(List<OrderBean> list, BmobException e) {
                    relativeOrderBefore.setVisibility(View.INVISIBLE);
                    relativeOrderAfter.setVisibility(View.VISIBLE);
                    if (list.size() > 0) {
                        Flush();
                        logI(list.toString());
                        relativeOrderBefore.setVisibility(View.INVISIBLE);
                        relativeOrderAfter.setVisibility(View.INVISIBLE);
                        relativeLayoutOrder.setVisibility(View.VISIBLE);
                        orderListViewAdapter = new OrderListViewAdapter(list, act);
                        orderListView.setAdapter(orderListViewAdapter);
//                        Log.i("sky", list.get(0).getShop().getName());
                    } else {
                        Flush();
                    }
                }
            });
        }


        /**
         * 根据登陆状态 刷新视图
         */
//        if (MyApplication.user != null) {
//        LoginFlushViewUtils.flushView();
//            adapter.notifyDataSetChanged();
//        }
    }


    //动态更改按钮图片
    public void updateButtonImg(int index) {
        for (int i = 0; i < 3; i++) {
            if (i == index) {
                butImgs[i].setImageResource(butOnResIds[i]);
            } else {
                butImgs[i].setImageResource(butOffResIds[i]);
            }
        }
    }

    //点击按钮执行ViewPager跳转以及 按钮图片的刷新
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.act_home_homepageLinearLayout: {
                updateButtonImg(0);
                vp.setCurrentItem(0);
                break;
            }
            case R.id.act_home_orderLinearLayout: {
                updateButtonImg(1);
                vp.setCurrentItem(1);
                break;
            }
            case R.id.act_home_mineLinearLayout: {
                updateButtonImg(2);
                vp.setCurrentItem(2);
                break;
            }
        }
    }

    //跳转注册页面
    @OnClick(R.id.fragment_order_but)
    public void onRegClick(View v) {
        startAct(LoginActivity.class);
    }

    //个人页面 个人头像、文本 布局 点击跳转登陆 或 ...
    @OnClick(R.id.fragment_mine_userRelativeLayout)
    public void onMineUserClick(View v) {
        startAct(LoginActivity.class);
    }

    //个人设置
    @OnClick(R.id.fragment_mine_setting)
    public void onSettingClick(View v) {
        //待优化 跳转设置界面
        if (MyApplication.user == null) {
            toastS("请您先登陆");
        } else {
            startAct(SettingActivity.class);
        }
    }

    //点击返回键
    private boolean backFlag = false;
    CountDownTimer timer;

    @Override
    public void onBackPressed() {
        if (backFlag) {
            timer.cancel();
            System.exit(0);
        }
        toastS("再次点击退出~");
        backFlag = true;
        timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                backFlag = false;
            }
        };
        timer.start();
    }

    //个人信息页面控件
    @OnClick(R.id.fragment_mine_shangjiaruzhuLin)
    public void onShangjiaClick(View v) {
        startAct(NewShopActivity.class);
    }

    //点击用户头像的操作
    @OnClick(R.id.fragment_mine_userIcon)
    public void onUserIconClick(View v) {
        showSetUserIconDialog();
    }

    private AlertDialog dialog;

    //上传图片弹出AlertDialog选择
    public void showSetUserIconDialog() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(act).setTitle("设置头像").setMessage("请选择图片来源")
                    .setPositiveButton("相册", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            // 设置内容类型
                            intent.setType("image/*");
                            // 剪裁
                            intent.putExtra("crop", "circleCrop");
                            // 裁剪比例
                            intent.putExtra("aspectX", 1);
                            intent.putExtra("aspectY", 1);
                            intent.putExtra("outputX", 250);
                            intent.putExtra("outputY", 250);
                            // 去黑边
                            intent.putExtra("scale", true);
                            File file = new File(MyApplication.file, "/userIcon" + ".jpeg");
                            Log.i("SKY", "相册保存路径：" + file.toString());
                            if (file.exists()) {
                                file.delete();
                            }
                            intent.putExtra("output", Uri.fromFile(file.getAbsoluteFile()));
                            startActivityForResult(intent, 0);
                        }
                    }).setNegativeButton("拍照", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.i("IMG", "拍照");
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            //拍照缓存的图片不是用来上传的 用来裁剪使用
                            File file = new File(MyApplication.file, "/userIconCache" + ".jpeg");
                            Log.i("SKY", "拍照缓存路径：" + file.toString());
                            if (file.exists()) {
                                file.delete();
                            }
                            intent.putExtra("output", Uri.fromFile(file.getAbsoluteFile()));
                            startActivityForResult(intent, 1);
                        }
                    }).create();
        }
        dialog.show();
    }


    //当返回结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File file = new File(MyApplication.file, "/userIcon" + ".jpeg");
        Log.i("SKY", "验证路径：" + file.toString());
        if (requestCode == 0 || requestCode == 2) {
            if (!file.exists()) {
                return;
            }
            Log.i("IMG", "request 0 || 2 进入");
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            //添加图片
            userIconImg.setImageBitmap(bitmap);
            //将图片上传至服务器
            final BmobFile bmobFile = new BmobFile(file);
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        UserBean u = new UserBean();
                        u.setUserIcon(bmobFile);
                        u.update(MyApplication.user.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    toastS("图片上传成功！");
                                    //重新获取用户
                                    UserBean user = BmobUser.getCurrentUser(UserBean.class);
                                    MyApplication.user = user;
                                    File oldFile = new File(MyApplication.file, "/" + MyApplication.user.getObjectId() + "/" + MyApplication.user.getObjectId() + ".jpeg");
                                    if (oldFile.exists()) {
                                        oldFile.delete();
                                    }
                                    //二级缓存头像
                                    final File file = new File(MyApplication.file, "/" + MyApplication.user.getObjectId() + "/" + MyApplication.user.getObjectId() + ".jpeg");
                                    Bitmap bit = BitmapFactory.decodeFile(file.getAbsolutePath());
                                    if (bit == null) {
                                        //如果用户设置了头像的话
                                        if (MyApplication.user.getUserIcon() != null) {
                                            MyApplication.user.getUserIcon().download(file.getAbsoluteFile(), new DownloadFileListener() {
                                                @Override
                                                public void done(String s, BmobException e) {
                                                    if (e == null) {
                                                        //bit设置不了final
                                                        final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                                        Log.i("MyFile", "======BitmapFactory.decodeFile(file.getAbsolutePath():=====" + file.getAbsoluteFile());
                                                        //第二步 设置图片
                                                        userIconImg.setImageBitmap(bitmap);
                                                        //第三部 缓存到内存中
//                    cache.put(str32, bitmap);
                                                    }
                                                }

                                                @Override
                                                public void onProgress(Integer integer, long l) {

                                                }
                                            });
                                        }
                                    } else {
                                        userIconImg.setImageBitmap(bit);
                                    }

                                } else {
                                    toastS("图片上传失败!");
                                }
                            }
                        });

                    } else {
                        toastS("图片上传失败，请重试！");
                    }
                }
            });

        } else if (requestCode == 1) {
            File fileCache = new File(MyApplication.file, "/userIconCache" + ".jpeg");
            Log.i("SKY", "拍照缓存路径：" + fileCache.toString());
            Log.i("IMG", "request 1 进入");
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(Uri.fromFile(fileCache), "image/*"); // 要裁剪的图片URI
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            // aspectX：aspectY 裁剪比例
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 250);
            intent.putExtra("outputY", 250);
            // 输出图片大小 intent.putExtra("outputY", 1024);
            intent.putExtra("return-data", false);
            // 是否以bitmap方式返回，缩略图可设为true，大图一定要设为false，返回URI
            intent.putExtra("noFaceDetection", true);
            // 去黑边
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            // 输出的图片的URI
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 返回格式 intent.putExtra("scale", true);// 去黑边 intent.putExtra("scaleUpIfNeeded", true);
            // 去黑边
            startActivityForResult(intent, 2); // activity result
        }
    }

    //钱包
    @OnClick(R.id.fragment_mine_walletLin)
    public void onWalletClick(View v) {
        if (MyApplication.user == null) {
            toastS("请您先登陆！");
            return;
        }
        startAct(WalletActivity.class);
    }

    //更多
    @OnClick(R.id.fragment_mine_moreLin)
    public void onMoreClick(View v) {
        startAct(MoreActivity.class);
    }

    //客服
    @OnClick(R.id.fragment_mine_KeFuLin)
    public void onKeFuClick(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://wpa.qq.com/msgrd?v=3&uin=406910111&site=qq&menu=yes"));
//        intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
        startActivity(intent);
    }

    //地址
    @OnClick(R.id.fragment_mine_addressLin)
    public void onAddressClick(View v) {
        if (MyApplication.user == null) {
            toastS("请您先登陆！");
            return;
        }
        startAct(AddressActivity.class);
    }

    //收藏
    @OnClick(R.id.fragment_mine_myShouCangLin)
    public void onShouCangClick(View v) {
        if (MyApplication.user == null) {
            toastS("请您先登陆！");
            return;
        }
        startAct(MyShouCangActivity.class);
    }

    //评价
    @OnClick(R.id.fragment_mine_myPjLin)
    public void onMyPjClick(View v) {
        if (MyApplication.user == null) {
            toastS("请您先登陆！");
            return;
        }
        startAct(MyPjActivity.class);
    }

    public void Flush() {
//        relativeOrderBefore.setVisibility(View.INVISIBLE);
//        relativeOrderAfter.setVisibility(View.VISIBLE);
        if (MyApplication.user.getNickName() == null) {
            mineUserNameTv.setText("未设置昵称");
        } else {
            mineUserNameTv.setText(MyApplication.user.getNickName());
        }
        mineUserRelative.setClickable(false);
        userIconImg.setClickable(true);

        //二级缓存头像
        final File file = new File(MyApplication.file, "/" + MyApplication.user.getObjectId() + "/" + MyApplication.user.getObjectId() + ".jpeg");
        Bitmap bit = BitmapFactory.decodeFile(file.getAbsolutePath());
        if (bit == null) {
            //如果用户设置了头像的话
            if (MyApplication.user.getUserIcon() != null) {
                MyApplication.user.getUserIcon().download(file.getAbsoluteFile(), new DownloadFileListener() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            //bit设置不了final
                            final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            Log.i("MyFile", "======BitmapFactory.decodeFile(file.getAbsolutePath():=====" + file.getAbsoluteFile());
                            //第二步 设置图片
                            userIconImg.setImageBitmap(bitmap);
                            //第三部 缓存到内存中
//                    cache.put(str32, bitmap);
                        }
                    }

                    @Override
                    public void onProgress(Integer integer, long l) {

                    }
                });
            }
        } else {
            userIconImg.setImageBitmap(bit);
        }


        vp.getAdapter().notifyDataSetChanged();
    }

    //点击有礼
    @OnClick(R.id.fragment_mine_giftLin)
    public void onGiftClick(View v) {
        if (MyApplication.user == null) {
            toastS("请您先登陆！");
            return;
        }
        List<GiftBean> giftBeanList = new ArrayList<>();
        giftBeanList = MyApplication.user.getGiftBeanList();
        final GiftBean gift = new GiftBean();
        if (new Random().nextBoolean()) {
            gift.setMoney(5);
        } else {
            gift.setMoney(10);
        }
        gift.setUsed(false);
        giftBeanList.add(gift);
        UserBean u = new UserBean();
        u.setGiftBeanList(giftBeanList);
        u.update(MyApplication.user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    toastL("恭喜您获得一张价值" + gift.getMoney() + "元的美团红包");
                }
            }
        });
    }

    //美团红包
    @OnClick(R.id.fragment_mine_myGiftLin)
    public void onMyGiftClick(View v) {
        if (MyApplication.user == null) {
            toastS("请您先登陆！");
            return;
        }
        startAct(GiftActivity.class);
    }
}
