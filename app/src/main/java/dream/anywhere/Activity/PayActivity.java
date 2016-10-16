package dream.anywhere.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import c.b.BP;
import c.b.PListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseActivity;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.Bean.UserBean;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/4.
 */
public class PayActivity extends BaseActivity implements BaseInterface {

    @ViewInject(R.id.act_chongzhi_moneyEt)
    private EditText money;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chongzhi);
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

    }

    @Override
    public void InitViewOper() {

    }

    @OnClick(R.id.act_chongzhi_backImg)
    public void onBackClick(View v) {
        finish();
    }

    @OnClick(R.id.act_chongzhi_zhifubao)
    public void OnZFBClick(View v) {
        pay(true);
    }

    @OnClick(R.id.act_chongzhi_weixin)
    public void OnWXClick(View v) {
        pay(false);
    }

    private void ChongZhi() {
        String APPID = "d444aa5b7c5a79de48199781aac392f6";
        int PLUGINVERSION = 7;
    }

    void pay(final boolean alipayOrWechatPay) {
        showDialog("正在获取订单...");
        final String name = "美团外卖钱包充值";

        BP.pay(name, "美团外卖钱包充值", getPrice(), alipayOrWechatPay, new PListener() {

            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {
                Toast.makeText(act, "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT)
                        .show();
                hideDialog();
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                UserBean u = new UserBean();
                u.setMoney(MyApplication.user.getMoney() + getPrice());
                u.update(MyApplication.user.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        MyApplication.user= BmobUser.getCurrentUser(UserBean.class);
                        Toast.makeText(act, "支付成功!", Toast.LENGTH_SHORT).show();
                        hideDialog();
                        finish();
                    }
                });
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
//                order.setText(orderId);
//                tv.append(name + "'s orderid is " + orderId + "\n\n");
                showDialog("获取订单成功!请等待跳转到支付页面~");
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {

                // 当code为-2,意味着用户中断了操作
                // code为-3意味着没有安装BmobPlugin插件
                if (code == -3) {
                    Toast.makeText(
                            act,
                            "监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付",
                            Toast.LENGTH_SHORT).show();
                    installBmobPayPlugin("bp.db");
                } else {
                    Toast.makeText(act, "支付中断!", Toast.LENGTH_SHORT)
                            .show();
                }
//                tv.append(name + "'s pay status is fail, error code is \n"
//                        + code + " ,reason is " + reason + "\n\n");
                hideDialog();
            }
        });
    }

    double getPrice() {
        double price = 0.02;
        try {
            price = Double.parseDouble(this.money.getText().toString());
        } catch (NumberFormatException e) {
        }
        return price;
    }

    void showDialog(String message) {
        try {
            if (dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setCancelable(true);
            }
            dialog.setMessage(message);
            dialog.show();
        } catch (Exception e) {
            // 在其他线程调用dialog会报错
        }
    }

    void hideDialog() {
        if (dialog != null && dialog.isShowing())
            try {
                dialog.dismiss();
            } catch (Exception e) {
            }
    }

    void installBmobPayPlugin(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fileName + ".apk");
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
