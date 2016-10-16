package dream.anywhere.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import dream.anywhere.Activity.ShopTypeActivity;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/2.
 */
public class HomePageGridViewAdapter extends BaseAdapter {

    private int[] imgResIds;
    private String[] texts;

    private Context context;
    private LayoutInflater inflater;

    public HomePageGridViewAdapter(int[] imgResIds, String[] texts, Context context) {
        this.imgResIds = imgResIds;
        this.texts = texts;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return texts.length;
    }

    @Override
    public Object getItem(int i) {
        return texts[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_homefragment_typevp, null);
        }
        ImageView img = (ImageView) view.findViewById(R.id.item_homefragment_typeImg);
        TextView tv = (TextView) view.findViewById(R.id.item_homefragment_typeTv);
        img.setImageResource(imgResIds[position]);
        tv.setText(texts[position]);
        view.setClickable(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.putData("ClickType", texts[position]);
                context.startActivity(new Intent(context, ShopTypeActivity.class));
            }
        });
        return view;
    }
}
