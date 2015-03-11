package vh1981.com.funnyphotosstorage;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class SwipeImageViewAdapter extends FragmentPagerAdapter {
    SwipeImageViewDataSource _dataSource;

    public SwipeImageViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        SwipeImageViewFragment fragment = new SwipeImageViewFragment();
        fragment.setImageBitmap(_dataSource.getBitmap(index));
        return fragment;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return _dataSource.getCount();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    void set_dataSource(SwipeImageViewDataSource dataSource)
    {
        _dataSource = dataSource;
    }
}