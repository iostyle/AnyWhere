package dream.anywhere.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.io.File;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.SaveListener;
import dream.anywhere.Activity.ShopActivity;
import dream.anywhere.Activity.ShopSystemAddActivity;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseActivity;
import dream.anywhere.Bean.FoodBean;
import dream.anywhere.Bean.ShopBean;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/2.
 */
public class HomePageShopListViewAdapter extends BaseAdapter {

    private List<ShopBean> shops;
    private Context context;
    private LayoutInflater inflater;

    public HomePageShopListViewAdapter(List<ShopBean> shops, android.content.Context context) {
        this.shops = shops;
        this.context = context;
        inflater = LayoutInflater.from(context);
        findLocation();
    }

    @Override
    public int getCount() {
        return shops.size();
    }

    @Override
    public Object getItem(int i) {
        return shops.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder vh = null;
        if (view == null) {
            view = inflater.inflate(R.layout.item_shops, null);
            vh = new ViewHolder();
            vh.shopName = (TextView) view.findViewById(R.id.item_shops_name);
            vh.saleCount = (TextView) view.findViewById(R.id.item_shops_sales);
            vh.minPrice = (TextView) view.findViewById(R.id.item_shops_qisongjia);
            vh.sendPrice = (TextView) view.findViewById(R.id.item_shops_peisongfei);
            vh.distance = (TextView) view.findViewById(R.id.item_shops_distance);
            vh.sendTime = (TextView) view.findViewById(R.id.item_shops_sendTime);
            vh.logo = (ImageView) view.findViewById(R.id.item_shops_img);
            vh.newshop = (ImageView) view.findViewById(R.id.item_shops_newShop);
            vh.zs = view.findViewById(R.id.item_shops_zs);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        final ShopBean shop = shops.get(position);
        vh.shopName.setText(shop.getName());
        vh.saleCount.setText(shop.getSaleCount().toString());
        vh.minPrice.setText(shop.getQisongjia().toString());
        vh.sendPrice.setText(shop.getPeisongfei().toString());

        //展示活动距离
        String distance = "暂未定位";

        long m = 0;
        try {
            LatLng startLocation = new LatLng(MyApplication.bdLocation.getLatitude(), MyApplication.bdLocation.getLongitude());
            LatLng endLocation = shop.getLocation();
            m = (long) DistanceUtil.getDistance(startLocation, endLocation);
        } catch (Exception e) {
            if (!flagThread) {
                findLocation();
            }
        }
        if (m < 1000) {
            distance = m + "米";
        } else {
            m += 500;
            distance = (m / 1000) + "千米";
        }
        vh.distance.setText(distance);
        vh.sendTime.setText(shop.getPeisongshijian().toString() + "分钟");

        BmobFile bFile = shop.getLogoImg();

        //二级缓存Logo
        final File file = new File(MyApplication.file, "/" + shop.getObjectId() + "/" + shop.getObjectId() + ".jpeg");
        Bitmap bit = BitmapFactory.decodeFile(file.getAbsolutePath());
        if (bit == null) {
            final ViewHolder finalVh = vh;
            bFile.download(file.getAbsoluteFile(), new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        //bit设置不了final
                        final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        Log.i("MyFile", "======BitmapFactory.decodeFile(file.getAbsolutePath():=====" + file.getAbsoluteFile());
                        //第二步 设置图片
                        finalVh.logo.setImageBitmap(bitmap);
                        //第三部 缓存到内存中
//                    cache.put(str32, bitmap);
                    }
                }

                @Override
                public void onProgress(Integer integer, long l) {

                }
            });
        } else {
            vh.logo.setImageBitmap(bit);
        }
        if (shop.getFlag().toString().contains("美团专送")) {
            vh.zs.setVisibility(View.VISIBLE);
        }
        if (shop.getFlag().toString().contains("新商家")) {
            vh.newshop.setVisibility(View.VISIBLE);
        }

        //测试
//        view.setClickable(true);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FoodBean food = new FoodBean();
//                food.setFoodName("测试");
//                food.setFoodPrice(1.5);
//                food.setType("测试类");
//                food.setShop(shop);
//                food.save(new SaveListener<String>() {
//                    @Override
//                    public void done(String s, BmobException e) {
//                        if(e==null){
//                            Log.i("food","OK");
//                        }
//                    }
//                });
//            }
//        });
        //测试

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.putData("ClickShop",shop);
                context.startActivity(new Intent(context, ShopActivity.class));
            }
        });

        return view;
    }

    class ViewHolder {
        TextView shopName, saleCount, minPrice, sendPrice, distance, sendTime;
        ImageView logo, newshop;
        View zs;
    }

    //标记线程是否在运行
    boolean flagThread = false;

    private void findLocation() {
        if (MyApplication.bdLocation == null) {
            flagThread = true;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    BaseActivity act = (BaseActivity) context;
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            HomePageViewPagerAdapter.this.notifyDataSetChanged();
                            flagThread = false;
                        }
                    });
                }
            }.start();
        }
    }
}
