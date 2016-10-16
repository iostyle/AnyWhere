package dream.anywhere.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import dream.anywhere.Base.BaseFragment;


/**
 * Created by SKYMAC on 16/8/30.
 */
public class HomeViewPagerAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> fragments;

    public HomeViewPagerAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }

//    public void flush(){
//        this.notifyDataSetChanged();
//    }
}
