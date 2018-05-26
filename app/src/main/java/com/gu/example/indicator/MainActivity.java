package com.gu.example.indicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.gu.indicator.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
  private static final String[] titles = {"第一页", "第二页", "第三页"};

  @BindView(R.id.vp)
  ViewPager vp;

  @BindView(R.id.tablayout)
  TabLayout mTabLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    vp.setAdapter(new PageAdapter(getSupportFragmentManager()));
    // TabLayout使用
    mTabLayout.createContentByTitles(titles).setViewPager(vp).combine();
  }

  @OnClick(R.id.updateBtn)
  public void update() {
    mTabLayout
        .setTextColors(ContextCompat.getColorStateList(this, R.color.tab_text_state_color))
        .setIndicatorColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        .setMargin(10)
        .setTextSize(14)
        .update();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mTabLayout.destroyView();
    ((App) getApplication()).checkItem(this);
  }

  class PageAdapter extends FragmentPagerAdapter {

    PageAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      return ItemFragment.newInstance(String.valueOf(position));
    }

    @Override
    public int getCount() {
      return titles.length;
    }
  }
}
