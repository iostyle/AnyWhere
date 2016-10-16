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

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import dream.anywhere.Activity.OrderActivity;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Bean.OrderBean;
import dream.anywhere.Bean.ShopBean;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/7.
 */
public class OrderListViewAdapter extends BaseAdapter {

    private List<OrderBean> orderBeanList;
    private Context context;
    private LayoutInflater inflater;

    public OrderListViewAdapter(List<OrderBean> orderBeanList, Context context) {
        this.orderBeanList = orderBeanList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return orderBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return orderBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder vh = new ViewHolder();
        if (view == null) {
            view = inflater.inflate(R.layout.item_order_listview, null);
            vh.shopImg = (ImageView) view.findViewById(R.id.item_order_shopImg);
            vh.shopNameTv = (TextView) view.findViewById(R.id.item_order_shopNameTv);
            vh.statusTv = (TextView) view.findViewById(R.id.item_order_statusTv);
            vh.timeTv = (TextView) view.findViewById(R.id.item_order_timeTv);
            vh.sumTv = (TextView) view.findViewById(R.id.item_order_sumTv);
            vh.priceTv = (TextView) view.findViewById(R.id.item_order_priceTv);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        final OrderBean order = orderBeanList.get(position);

//        final ShopBean shop = new ShopBean();

        BmobQuery<ShopBean> bmobQuery = new BmobQuery<ShopBean>();
        final ViewHolder finalVh1 = vh;
        bmobQuery.getObject(order.getShopObjectId(), new QueryListener<ShopBean>() {
            @Override
            public void done(ShopBean object, BmobException e) {
                if (e == null) {
                    finalVh1.shopNameTv.setText(object.getName());
                } else {
                }
            }
        });

        ShopBean shop = order.getShop();

        Log.i("shop", order.getShop().toString());
        Log.i("shop", shop.toString());
//        Log.i("shop", shop.getObjectId().toString());
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
                        finalVh.shopImg.setImageBitmap(bitmap);
                        //第三部 缓存到内存中
//                    cache.put(str32, bitmap);
                    }
                }

                @Override
                public void onProgress(Integer integer, long l) {

                }
            });
        } else {
            vh.shopImg.setImageBitmap(bit);
        }


//        vh.shopNameTv.setText(shop.getName() + "");
        if (!order.getPay()) {
            //未支付
            vh.statusTv.setText("未支付");
        } else {
            if (!order.getReceive()) {
                vh.statusTv.setText("配送中");
            } else {
                vh.statusTv.setText("订单完成");
            }
        }
        vh.timeTv.setText(order.getCreatedAt());
        vh.sumTv.setText(order.getBuyFoodBeanList().size() + "");
        vh.priceTv.setText(order.getPayPrice().toString());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.putData("ClickOrder", order);
                context.startActivity(new Intent(context, OrderActivity.class));
            }
        });

        return view;
    }

    class ViewHolder {
        ImageView shopImg;
        TextView shopNameTv, statusTv, timeTv, sumTv, priceTv;
    }
}
