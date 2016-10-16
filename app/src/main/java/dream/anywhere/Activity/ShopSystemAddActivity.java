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
import android.widget.EditText;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseActivity;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.Bean.FoodBean;
import dream.anywhere.Bean.ShopBean;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/5.
 */
public class ShopSystemAddActivity extends BaseActivity implements BaseInterface {

    private ShopBean shop;

    @ViewInject(R.id.act_shopsystemadd_foodNameEt)
    private EditText foodNameEt;
    @ViewInject(R.id.act_shopsystemadd_foodPriceEt)
    private EditText foodPriceEt;
    @ViewInject(R.id.act_shopsystemadd_foodTypeEt)
    private EditText foodTypeEt;
    @ViewInject(R.id.act_shopsystemadd_image)
    private ImageView img;

    //上传的菜品图片 返回的bmobFile 用作FoodBean中的字段
    private BmobFile bFile;

    private AlertDialog dialog;

    private FoodBean food = new FoodBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_shopsystemadd);
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
        shop = (ShopBean) MyApplication.getData("ShopAdd", true);
    }

    @Override
    public void InitViewOper() {

    }

    //点击返回
    @OnClick(R.id.act_shopsystemadd_backLinearLayout)
    public void onBackClick(View v) {
        finish();
    }

    //上传图片
    @OnClick(R.id.act_shopsystemadd_shangchuanTv)
    public void onShangChuanClick(View v) {
        showSetFoodPhotoDialog();
    }

    //点击上传
    @OnClick(R.id.act_shopsystemadd_submitBut)
    public void onSubmitClick(View v) {
        String foodName = foodNameEt.getText().toString();
        if (foodName == null) {
            toastS("请输入菜品名称");
            return;
        }
        Double foodPrice = Double.parseDouble(foodPriceEt.getText().toString());
        if (foodPrice == null) {
            toastS("请输入菜品价格");
            return;
        }
        String foodType = foodTypeEt.getText().toString();
        if (foodType == null) {
            toastS("请输入菜品分类");
            return;
        }
        if (bFile == null) {
            toastS("请上传菜品图片");
        }

        food.setFoodName(foodName);
        food.setFoodPrice(foodPrice);
        food.setType(foodType);
        food.setFoodImage(bFile);
        food.setShop(shop);
        food.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    toastL("恭喜您，上传菜品成功!");
                    finish();
                }else{
                    toastS("上传菜品失败，请重试");
                }
            }
        });
    }

    public void showSetFoodPhotoDialog() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(act).setTitle("设置菜品图片").setMessage("请选择图片来源")
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
                            intent.putExtra("outputX", 100);
                            intent.putExtra("outputY", 100);
                            // 去黑边
                            intent.putExtra("scale", true);
                            File file = new File(MyApplication.file, "/" + shop.getObjectId() + "/" + food.getObjectId() + ".jpeg");
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
                            File file = new File(MyApplication.file, "/" + shop.getObjectId() + "/cache.jpeg");
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
        File file = new File(MyApplication.file, "/" + shop.getObjectId() + "/" + food.getObjectId() + ".jpeg");
        Log.i("SKY", "验证路径：" + file.toString());
        if (requestCode == 0 || requestCode == 2) {
            if (!file.exists()) {
                return;
            }
            Log.i("IMG", "request 0 || 2 进入");
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            //添加图片
            img.setImageBitmap(bitmap);
            //将图片上传至服务器
            final BmobFile bmobFile = new BmobFile(file);
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        bFile = bmobFile;
                        toastS("图片上传成功!");

                    } else {
                        toastS("图片上传失败，请重试！");
                    }
                }
            });

        } else if (requestCode == 1) {
            File fileCache = new File(MyApplication.file, "/" + shop.getObjectId() + "/cache.jpeg");
            Log.i("SKY", "拍照缓存路径：" + fileCache.toString());
            Log.i("IMG", "request 1 进入");
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(Uri.fromFile(fileCache), "image/*"); // 要裁剪的图片URI
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            // aspectX：aspectY 裁剪比例
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 100);
            intent.putExtra("outputY", 100);
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

}
