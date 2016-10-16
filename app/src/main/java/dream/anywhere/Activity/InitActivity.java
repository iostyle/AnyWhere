package dream.anywhere.Activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.bmob.v3.BmobUser;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseActivity;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.Bean.UserBean;
import dream.anywhere.R;

public class InitActivity extends BaseActivity implements BaseInterface {

    //隐藏动画
    private Animation animHide;
    //显示动画
    private Animation animShow;

    @ViewInject(R.id.act_init_img1)
    private ImageView img1;
    @ViewInject(R.id.act_init_img2)
    private ImageView img2;
    @ViewInject(R.id.act_init_tv)
    private TextView tv;
    @ViewInject(R.id.act_init_RealtiveLayout1)
    private RelativeLayout rel1;
    @ViewInject(R.id.act_init_RealtiveLayout2)
    private RelativeLayout rel2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logI("InitActivity ---> onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_init);
        ViewUtils.inject(this);
        InitView();
        InitData();
        InitViewOper();
    }

    @Override
    protected void onStart() {
        logI("InitActivity ---> onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        logI("InitActivity ---> onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        logI("InitActivity ---> onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        logI("InitActivity ---> onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        logI("InitActivity ---> onDestroy");
        super.onDestroy();
    }

    @Override
    public void InitView() {
        animHide = AnimationUtils.loadAnimation(act, R.anim.anim_init_alpha_hide);
        animShow = AnimationUtils.loadAnimation(act, R.anim.anim_init_alpha_show);
        //第一张图片开始加载时 开始加载数据
        animHide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //初始化数据 如验证登陆、刷新视图的操作
                rel2.setVisibility(View.VISIBLE);

                //获取本地存储的用户名密码 验证登陆
//                SharedPreferences sharedPreferences = getSharedPreferences("User",MODE_PRIVATE);
//                String uname = sharedPreferences.getString("uname","");
//                String upass = sharedPreferences.getString("upass","");
//                UserBean.loginByAccount(uname, upass, new LogInListener<UserBean>() {
//                    @Override
//                    public void done(UserBean userBean, BmobException e) {
//                        if(e==null){
//                            MyApplication.user = userBean;
//                        }else{
//                            logI("InitActivity ---> 本地没有用户");
//                        }
//                    }
//                });
                /**
                 * 优化 Bmob提供了 登陆用户的缓存
                 */
                UserBean user = BmobUser.getCurrentUser(UserBean.class);
                if (user != null) {
                    MyApplication.user = user;
                } else {
                    MyApplication.user = null;
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //第二张图片结束加载时 关闭
        animShow.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        startAct(HomeActivity.class);
                        finish();
                    }
                }).start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void InitData() {

    }

    @Override
    public void InitViewOper() {
        CountDownTimer timer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                rel1.startAnimation(animHide);
                rel2.startAnimation(animShow);
            }
        };
        timer.start();
//        rel1.startAnimation(animHide);
//        rel2.startAnimation(animShow);
    }
}
