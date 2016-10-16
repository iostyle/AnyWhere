package dream.anywhere.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import dream.anywhere.Bean.BuyFoodBean;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/6.
 */
public class SetOrderListViewAdapter extends BaseAdapter {

    private List<BuyFoodBean> buyFoodBeanList;
    private Context context;
    private LayoutInflater inflater;

    public SetOrderListViewAdapter(List<BuyFoodBean> buyFoodBeanList, Context context) {
        this.buyFoodBeanList = buyFoodBeanList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return buyFoodBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return buyFoodBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (view == null) {
            view = inflater.inflate(R.layout.item_setorder_listview, null);
            vh = new ViewHolder();
            vh.foodNameTv = (TextView) view.findViewById(R.id.item_setorder_listview_foodNameTv);
            vh.foodSumTv = (TextView) view.findViewById(R.id.item_setorder_listview_foodNumTv);
            vh.foodSumPriceTv = (TextView) view.findViewById(R.id.item_setorder_listview_foodNumPriceTv);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        vh.foodNameTv.setText(buyFoodBeanList.get(position).getFood().getFoodName());
        vh.foodSumTv.setText(buyFoodBeanList.get(position).getSum() + "");
        Double sumPrice = (buyFoodBeanList.get(position).getFood().getFoodPrice()) * (buyFoodBeanList.get(position).getSum());
        vh.foodSumPriceTv.setText(sumPrice.toString());
        return view;
    }

    private class ViewHolder {
        TextView foodNameTv, foodSumTv, foodSumPriceTv;
    }
}
