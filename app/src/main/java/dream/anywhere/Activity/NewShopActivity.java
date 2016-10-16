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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseActivity;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.Bean.ShopBean;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/3.
 */
public class NewShopActivity extends BaseActivity implements BaseInterface {

    @ViewInject(R.id.act_newshop_backLinearLayout)
    private LinearLayout backLin;
    @ViewInject(R.id.act_newshop_zhuansongcheckbox_yes)
    private CheckBox zsYes;
    @ViewInject(R.id.act_newshop_zhuansongcheckbox_no)
    private CheckBox zsNo;
    @ViewInject(R.id.act_newshop_checkbox_meishi)
    private CheckBox meishi;
    @ViewInject(R.id.act_newshop_checkbox_chaoshi)
    private CheckBox chaoshi;
    @ViewInject(R.id.act_newshop_checkbox_xianguogou)
    private CheckBox xianguogou;
    @ViewInject(R.id.act_newshop_checkbox_xianhuadangao)
    private CheckBox xianhuadangao;
    @ViewInject(R.id.act_newshop_checkbox_nengliangxican)
    private CheckBox nengliangxican;
    @ViewInject(R.id.act_newshop_checkbox_rihanliaoli)
    private CheckBox rihanliaoli;
    @ViewInject(R.id.act_newshop_checkbox_zhajilingshi)
    private CheckBox zhajilingshi;
    @ViewInject(R.id.act_newshop_checkbox_haixianshaokao)
    private CheckBox haixianshaokao;

    @ViewInject(R.id.act_newshop_name)
    private EditText nameEt;
    @ViewInject(R.id.act_newshop_phone)
    private EditText phoneEt;
    @ViewInject(R.id.act_newshop_qisongjia)
    private EditText qisongjiaEt;
    @ViewInject(R.id.act_newshop_peisongfei)
    private EditText peisongfeiEt;
    @ViewInject(R.id.act_newshop_peisongshijian)
    private EditText peisongshijianEt;
    @ViewInject(R.id.act_newshop_logoImg)
    private ImageView logoImg;

    //用来记录店面所经营的类别 可多选
    private List<String> types;

    private AlertDialog dialog;

    //用来保存已经上传的Logo的BmobFile
    private BmobFile logoBmobFile;

    //百度地图获取来的信息
    private PoiInfo poiInfo;
    //经纬度
    private LatLng location;
    //地址信息
    private String address;

    @ViewInject(R.id.act_newshop_addressTv)
    private TextView addressTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_newshop);
        InitView();
        InitData();
        InitViewOper();
    }

    //从定位跳转回来
    @Override
    protected void onRestart() {
        super.onRestart();
        poiInfo = (PoiInfo) MyApplication.getData("poiInfo", true);
        if (poiInfo != null) {
            location = poiInfo.location;
            address = poiInfo.address;
            addressTv.setText(address);
        }
    }

    @Override
    public void InitView() {
        ViewUtils.inject(act);
    }

    @Override
    public void InitData() {
        types = new ArrayList<>();
    }

    @Override
    public void InitViewOper() {
        zsYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    zsNo.setChecked(false);
                } else {
                    zsNo.setChecked(true);
                }
            }
        });
        zsNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    zsYes.setChecked(false);
                } else {
                    zsYes.setChecked(true);
                }
            }
        });
    }

    @OnClick(R.id.act_newshop_backLinearLayout)
    public void onBackClick(View v) {
        finish();
    }

    @OnClick(R.id.act_newshop_submitBut)
    public void OnSubmitClick(View v) {
        String nameStr = nameEt.getText().toString().trim();
        if (nameStr.equals("")) {
            toastS("请输入商户名");
            return;
        }
        String phoneStr = phoneEt.getText().toString().trim();
        if (!phoneStr.matches("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$")) {
            toastS("电话号码格式有误，请重新输入。");
            return;
        }
        String qisongjiaStr = qisongjiaEt.getText().toString().trim();
        if (qisongjiaStr.equals("")) {
            toastS("请输入起送价");
            return;
        }
        Integer qisongjia = Integer.parseInt(qisongjiaStr);
        String peisongfeiStr = peisongfeiEt.getText().toString().trim();
        if (peisongfeiStr.equals("")) {
            toastS("请输入配送费");
            return;
        }
        Integer peisongfei = Integer.parseInt(peisongfeiStr);
        String peisongshijianStr = peisongshijianEt.getText().toString().trim();
        if (peisongshijianStr.equals("")) {
            toastS("请输入配送时间");
            return;
        }
        Integer peisongshijian = Integer.parseInt(peisongshijianStr);
        if (location == null) {
            toastS("请选择商户地址！");
            return;
        }

        if (logoBmobFile == null) {
            toastS("请上传商户Logo");
            return;
        }

        if (meishi.isChecked()) {
            types.add("美食");
        }
        if (chaoshi.isChecked()) {
            types.add("超市");
        }
        if (xianguogou.isChecked()) {
            types.add("鲜果购");
        }
        if (xianhuadangao.isChecked()) {
            types.add("鲜花蛋糕");
        }
        if (nengliangxican.isChecked()) {
            types.add("能量西餐");
        }
        if (rihanliaoli.isChecked()) {
            types.add("日韩料理");
        }
        if (zhajilingshi.isChecked()) {
            types.add("炸鸡零食");
        }
        if (haixianshaokao.isChecked()) {
            types.add("海鲜烧烤");
        }

        List<String> flag = new ArrayList<>();
        flag.add("新商家");
        if (zsYes.isChecked()) {
            flag.add("美团专送");
        }
//        toastL(types.toString());
//        types.clear();

        ShopBean shop = new ShopBean();
        shop.setName(nameStr);
        shop.setPhone(phoneStr);
        shop.setQisongjia(qisongjia);
        shop.setPeisongfei(peisongfei);
        shop.setPeisongshijian(peisongshijian);
        shop.setType(types);
        shop.setFlag(flag);
        //设置经纬度
        shop.setLocation(location);
        //设置地址
        shop.setAddress(address);
        shop.setLogoImg(logoBmobFile);

//        toastL(shop.toString());
        shop.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    toastL("审核通过");
                    finish();
                } else {
                    toastL("审核失败");
                }
            }
        });
    }

    //上传图片
    @OnClick(R.id.act_newshop_shangchuanTv)
    public void OnSCClick(View v) {
        showAddPhotoDialog();
    }

    //上传图片弹出AlertDialog选择
    public void showAddPhotoDialog() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(act).setTitle("设置Logo").setMessage("请选择图片来源")
                    .setPositiveButton("相册", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            // 设置内容类型
                            intent.setType("image/*");
                            // 剪裁
                            intent.putExtra("crop", "circleCrop");
                            // 裁剪比例
                            intent.putExtra("aspectX", 3);
                            intent.putExtra("aspectY", 2);
                            intent.putExtra("outputX", 320);
                            intent.putExtra("outputY", 214);
                            //去黑边
                            intent.putExtra("scale", true);
                            File file = new File(MyApplication.file, "/shopLogo" + ".jpeg");
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
                            File file = new File(MyApplication.file, "/shopLogoCache" + ".jpeg");
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
        File file = new File(MyApplication.file, "/shopLogo" + ".jpeg");
        Log.i("SKY", "验证路径：" + file.toString());
        if (requestCode == 0 || requestCode == 2) {
            if (!file.exists()) {
                return;
            }
            Log.i("IMG", "request 0 || 2 进入");
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            //添加图片
            logoImg.setImageBitmap(bitmap);
            //将图片上传至服务器
            final BmobFile bmobFile = new BmobFile(file);
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        logoBmobFile = bmobFile;
                        toastS("图片上传成功！");
                    } else {
                        toastS("图片上传失败，请重试！");
                    }
                }
            });

        } else if (requestCode == 1) {
            File fileCache = new File(MyApplication.file, "/shopLogoCache" + ".jpeg");
            Log.i("SKY", "拍照缓存路径：" + fileCache.toString());
            Log.i("IMG", "request 1 进入");
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(Uri.fromFile(fileCache), "image/*"); // 要裁剪的图片URI
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 3);
            // aspectX：aspectY 裁剪比例
            intent.putExtra("aspectY", 2);
            intent.putExtra("outputX", 320);
            intent.putExtra("outputY", 214);
            // 输出图片大小 intent.putExtra("outputY", 1024);
            intent.putExtra("return-data", false);
            // 是否以bitmap方式返回，缩略图可设为true，大图一定要设为false，返回URI
            intent.putExtra("noFaceDetection", true);
            // 去黑边
            intent.putExtra("scale", true);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            // 输出的图片的URI
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 返回格式 intent.putExtra("scale", true);// 去黑边 intent.putExtra("scaleUpIfNeeded", true);
            startActivityForResult(intent, 2); // activity result
        }
    }

    //点击定位跳转
    @OnClick(R.id.act_newshop_gpsLin)
    public void onGpsClick(View v) {
        startAct(MapActivity.class);
    }
}
