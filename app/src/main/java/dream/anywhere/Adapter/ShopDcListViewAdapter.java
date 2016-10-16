package dream.anywhere.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import dream.anywhere.Activity.HomeActivity;
import dream.anywhere.Activity.ShopActivity;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Bean.BuyFoodBean;
import dream.anywhere.Bean.FoodBean;
import dream.anywhere.Bean.ShopBean;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/5.
 */
public class ShopDcListViewAdapter extends BaseAdapter {

    private List<FoodBean> foodBeanList;
    private Context context;
    private LayoutInflater inflater;
    private int[] numbers;


    public ShopDcListViewAdapter(List<FoodBean> foodBeanList, Context context) {
        this.foodBeanList = foodBeanList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        numbers = new int[foodBeanList.size()];
        flushMoney();
    }

    @Override
    public int getCount() {
        return foodBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return foodBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder vh = new ViewHolder();
        if (view == null) {
            view = inflater.inflate(R.layout.item_foods, null);
            vh.foodNameTv = (TextView) view.findViewById(R.id.item_foods_foodNameTv);
            vh.foodSalesTv = (TextView) view.findViewById(R.id.item_foods_salesTv);
            vh.priceTv = (TextView) view.findViewById(R.id.item_foods_priceTv);
            vh.img = (ImageView) view.findViewById(R.id.item_foods_ImageView);
            vh.buySumTv = (TextView) view.findViewById(R.id.item_foods_buySumTv);
            vh.jia = (ImageView) view.findViewById(R.id.item_foods_jiaImg);
            vh.jian = (ImageView) view.findViewById(R.id.item_foods_jianImg);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }


//        try {
//            numbers[position] = ShopActivity.buyFoodBeanList.get(position).getSum();
//        } catch (Exception e) {
//            numbers[position] = 0;
//        }

        if (numbers[position] == 0) {
            vh.buySumTv.setVisibility(View.INVISIBLE);
            vh.buySumTv.setText(0 + "");
            vh.jian.setVisibility(View.INVISIBLE);
            vh.jian.setClickable(false);
        } else {
            vh.buySumTv.setVisibility(View.VISIBLE);
            vh.jian.setVisibility(View.VISIBLE);
            vh.jian.setClickable(true);
            vh.buySumTv.setText(numbers[position] + "");
        }

        final FoodBean food = foodBeanList.get(position);
        vh.foodNameTv.setText(food.getFoodName());
        vh.foodSalesTv.setText(food.getSales().toString());
        vh.priceTv.setText(food.getFoodPrice().toString());

        final ViewHolder finalVh1 = vh;
        finalVh1.jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numbers[position] == 0) {
                    finalVh1.buySumTv.setVisibility(View.VISIBLE);
                    finalVh1.jian.setVisibility(View.VISIBLE);
                    finalVh1.jian.setClickable(true);
                }

                numbers[position]++;
                finalVh1.buySumTv.setText(numbers[position] + "");

                //---
                p:
                if (ShopActivity.buyFoodBeanList.size() > 0) {
                    for (int i = 0; i < ShopActivity.buyFoodBeanList.size(); i++) {
                        if (ShopActivity.buyFoodBeanList.get(i).getFood().equals(food)) {
                            Log.i("money", "+1");
//                            int sum = ShopActivity.buyFoodBeanList.get(i).getSum() + 1;
                            ShopActivity.buyFoodBeanList.get(i).setSum(numbers[position]);
                            break p;
                        } else if (i == ShopActivity.buyFoodBeanList.size() - 1) {
                            //遍历到最后了 依然没有
                            ShopActivity.buyFoodBeanList.add(new BuyFoodBean(food, 1));
                        }
                    }
                } else {
                    //没有数据肯定是第一次添加
                    ShopActivity.buyFoodBeanList.add(new BuyFoodBean(food, 1));
                }

                //无论结果如何 刷新总价
                flushMoney();
            }
        });

//        vh = finalVh1;

        final ViewHolder finalVh2 = vh;
        finalVh2.jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < ShopActivity.buyFoodBeanList.size(); i++) {
                    if (ShopActivity.buyFoodBeanList.get(i).getFood().getObjectId().equals(food.getObjectId())) {
                        int sum = ShopActivity.buyFoodBeanList.get(i).getSum() - 1;
                        if (sum == 0) {
                            ShopActivity.buyFoodBeanList.remove(i);
                        } else {
                            ShopActivity.buyFoodBeanList.get(i).setSum(sum);
                        }
                    }
                }
                flushMoney();

                numbers[position]--;
                if (numbers[position] == 0) {
                    finalVh2.buySumTv.setVisibility(View.INVISIBLE);
                    finalVh2.jian.setVisibility(View.INVISIBLE);
                    finalVh2.jian.setClickable(false);
                } else {
                    finalVh2.buySumTv.setText(numbers[position] + "");
                }

            }
        });

//        vh = finalVh2;


        BmobFile bFile = food.getFoodImage();
        //二级缓存菜品图片
        final File file = new File(MyApplication.file, "/" + food.getShop().getObjectId() + "/" + food.getObjectId() + ".jpeg");
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
                        finalVh.img.setImageBitmap(bitmap);
                        //第三部 缓存到内存中
//                    cache.put(str32, bitmap);
                    }
                }

                @Override
                public void onProgress(Integer integer, long l) {

                }
            });
        } else {
            vh.img.setImageBitmap(bit);
        }

//        view.setTag(vh);
        return view;
    }

    private class ViewHolder {
        private TextView foodNameTv, foodSalesTv, priceTv, buySumTv;
        private ImageView img, jia, jian;
    }

    private void flushMoney() {
        Double money = 0.0;
        for (int i = 0; i < ShopActivity.buyFoodBeanList.size(); i++) {
            money += ShopActivity.buyFoodBeanList.get(i).getFood().getFoodPrice() * ShopActivity.buyFoodBeanList.get(i).getSum();
        }
        ShopActivity.money = money;
        ShopActivity.moneyTv.setText(money.toString());
        if (ShopActivity.money < ((ShopBean) (MyApplication.getData("ClickShop", false))).getQisongjia()) {
            ShopActivity.submitTv.setBackgroundColor(Color.parseColor("#BEBEBE"));
            ShopActivity.submitTv.setClickable(false);
            ShopActivity.submitTv.setText("起送价" + ((ShopBean) (MyApplication.getData("ClickShop", false))).getQisongjia() + "元");
        } else {
            ShopActivity.submitTv.setBackgroundResource(R.drawable.selector_but);
            ShopActivity.submitTv.setClickable(true);
            ShopActivity.submitTv.setText("下单");
        }
        for (int i = 0; i < ShopActivity.buyFoodBeanList.size(); i++) {
            Log.i("money", ShopActivity.buyFoodBeanList.get(i).getFood().getFoodName() + "," + ShopActivity.buyFoodBeanList.get(i).getSum());
        }
    }
}
