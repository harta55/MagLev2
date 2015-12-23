package com.alexhart.maglev2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

/**
 * MainActivity that will swap out fragments through pagerAdapter
 */

public class MainActivity extends ActionBarActivity {

    ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                inPreview = false;
            }else inPreview = true;

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    ViewPager mViewPager = null;
    public static boolean inPreview = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new pagerAdapter(fm));
        mViewPager.setOnPageChangeListener(mOnPageChangeListener);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("Main", "InflateActionBar");

        getMenuInflater().inflate(R.menu.main, menu);
        invalidateOptionsMenu();

        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (inPreview){
            Log.d("MainActivity", "HideActionBar");
            getSupportActionBar().hide();


        } else {
            getSupportActionBar().show();
        }


        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

}

//dont need state pager adapter
class pagerAdapter extends FragmentPagerAdapter {

    public pagerAdapter (FragmentManager fm) {
        super(fm);
    }


    //return fragment at given position
    @Override
    public Fragment getItem(int position) {
        Log.d("Frag", "Position: "+position);
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new MagLevControlFrag();
                break;
            case 1:
                fragment = new PreviewFrag();
                break;
            case 2:
                fragment = new GalleryViewFrag();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        //# pages
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.d("MainActivity", "GetPageTitle");
        switch (position){
            case 0:
                return "MagLev Control";
            case 1:
                return "Preview";
            case 2:
                return "GalleryView";

        }
        return null;
    }

}