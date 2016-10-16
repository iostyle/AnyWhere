package dream.anywhere.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseActivity;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.Bean.UserBean;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/1.
 */
public class SettingActivity extends BaseActivity implements BaseInterface {

    public static SettingActivity settingAct;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting);
        InitView();
        InitData();
        InitViewOper();
    }

    @Override
    public void InitView() {
        ViewUtils.inject(act);
        settingAct = this;
    }

    @Override
    public void InitData() {

    }

    @Override
    public void InitViewOper() {

    }

    //返回按钮点击
    @OnClick(R.id.act_setting_backLinearLayout)
    public void onBackClick(View v) {
        finish();
    }

    //注销登陆按钮点击
    @OnClick(R.id.act_setting_logoutBut)
    public void onLogOutClick(View v) {
        MyApplication.user = null;
        // Bmob提供的 清空用户缓存的方法
        BmobUser.logOut();
//        SharedPreferences sharedPreferences = SettingActivity.this.getSharedPreferences("User", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.remove("uname");
//        editor.remove("upass");
//        editor.commit();
        toastL("注销成功");
        finish();
    }

    //修改昵称
    @OnClick(R.id.act_setting_nickNameLin)
    public void onSetNickNameClick(View v) {
        startAct(SettingNickNameActivity.class);
    }

    //修改密码
    @OnClick(R.id.act_setting_passwordLin)
    public void onSetPasswordClick(View v) {
        startAct(SettingPasswordActivity.class);
    }

    //点击修改头像
    @OnClick(R.id.act_setting_userIconLin)
    public void onSetUserIconClick(View view) {
        showSetUserIconDialog();
    }

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
            HomeActivity.userIconImg.setImageBitmap(bitmap);
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
                                if(e==null){
                                    toastS("图片上传成功！");
                                    //重新获取用户
                                    UserBean user = BmobUser.getCurrentUser(UserBean.class);
                                    MyApplication.user = user;
                                    File oldFile = new File(MyApplication.file, "/" + MyApplication.user.getObjectId() + "/" + MyApplication.user.getObjectId() + ".jpeg");
                                    if(oldFile.exists()){
                                        oldFile.delete();
                                    }
                                }else{
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            // 输出的图片的URI
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 返回格式 intent.putExtra("scale", true);// 去黑边 intent.putExtra("scaleUpIfNeeded", true);
            // 去黑边
            startActivityForResult(intent, 2); // activity result
        }
    }
}
