package dream.anywhere.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import dream.anywhere.R;


/**
 * Created by SKYMAC on 16/9/2.
 */
public class HomePageViewPagerAdapter extends PagerAdapter {

    private int[] imgResIds = new int[]{R.drawable.home_vp1, R.drawable.home_vp2};
    private Context context;
    private LayoutInflater inflater;

    public HomePageViewPagerAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.item_homefragment_titlevp, null);
        ImageView img = (ImageView) view.findViewById(R.id.item_homefragment_titlevp_img);
        img.setImageResource(imgResIds[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return imgResIds.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
