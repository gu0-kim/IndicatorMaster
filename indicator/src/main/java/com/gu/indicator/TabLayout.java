package com.gu.indicator;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabLayout extends FrameLayout
    implements ViewPager.OnPageChangeListener, View.OnClickListener {

  private ColorStateList csl;
  private List<String> mTitles;
  private int margin;
  private int rd;
  private int textSize;
  private LinearLayout mContentLayout;
  private IndicatorView mIndicatorView;
  private ViewPager viewPager;

  private int startPos;
  private boolean clicked;

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    // 如果是点击事件，则不使用滚动的百分比，使用默认动画处理
    if (clicked) return;
    // 如果是左右滑动，使用百分比处理mIndicatorView效果
    if (position == startPos) {
      // 向右
      mIndicatorView.setPercent(positionOffset);
    } else if (position < startPos) {
      // 向左
      mIndicatorView.setPercent(positionOffset - 1);
    }
  }

  @Override
  public void onPageSelected(int position) {
    updateSelected(position);
  }

  @Override
  public void onPageScrollStateChanged(int state) {
    if (state == 0) {
      startPos = viewPager.getCurrentItem();
      if (!clicked) {
        mIndicatorView.setCurrentNoAnim(startPos);
      }
      clicked = false;
    }
  }

  @Override
  public void onClick(View v) {
    if (viewPager == null || mIndicatorView == null) return;
    int to = (Integer) v.getTag();
    int from = viewPager.getCurrentItem();
    viewPager.setCurrentItem(to, true);
    clicked = true;
    if (isOffsetTwo(from, to)) {
      mIndicatorView.setCurrent(to);
    } else if (toRight(from, to)) {
      mIndicatorView.indicator2Right();
    } else if (toLeft(from, to)) {
      mIndicatorView.indicator2Left();
    }
  }

  // 滚动跨度超过两个
  private boolean isOffsetTwo(int from, int to) {
    return Math.abs(to - from) > 1;
  }

  // 向右滚动
  private boolean toRight(int from, int to) {
    return to - from == 1;
  }

  // 向左滚动
  private boolean toLeft(int from, int to) {
    return to - from == -1;
  }

  private void updateSelected(int pos) {
    int size = mContentLayout.getChildCount();
    for (int i = 0; i < size; i++) {
      mContentLayout.getChildAt(i).setSelected(pos == i);
    }
  }

  public TabLayout(@NonNull Context context) {
    this(context, null);
  }

  public TabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    int minHeight = context.getResources().getDimensionPixelOffset(R.dimen.min_tab_layout_height);
    if (attrs != null) {
      TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabLayout);
      csl = a.getColorStateList(R.styleable.TabLayout_textColor);
      if (csl == null) {
        csl = getResources().getColorStateList(R.color.text_state_color);
      }
      textSize = a.getDimensionPixelOffset(R.styleable.TabLayout_textSize, sp2px(context, 14));
      margin = a.getDimensionPixelOffset(R.styleable.TabLayout_margin, 80);
      rd = a.getDimensionPixelOffset(R.styleable.TabLayout_rd, 2);
    } else {
      textSize = sp2px(context, 14);
      csl = getResources().getColorStateList(R.color.text_state_color);
      margin = 80;
      rd = 10;
    }
    mTitles = new ArrayList<>();
    LayoutParams params = new LayoutParams(context, attrs);
    if (params.height < minHeight) params.height = minHeight;
    setLayoutParams(params);
    addTabContent(context);
    addIndicatorView(context);
  }

  public void setTitles(String[] titles) {
    setTitles(titles, null);
  }

  public void setViewPager(ViewPager vp) {
    if (viewPager != null) {
      viewPager.removeOnPageChangeListener(this);
    }
    viewPager = vp;
    viewPager.addOnPageChangeListener(this);
  }

  public void setTitles(String[] titles, ColorStateList csl) {
    if (!mTitles.isEmpty()) {
      mTitles.clear();
    }
    if (csl != null) this.csl = csl;
    mTitles.addAll(Arrays.asList(titles));
    update();
  }

  public void destroyView() {
    releaseListener();
    removeAllViews();
    if (mTitles != null) mTitles.clear();
    mTitles = null;
    mContentLayout = null;
    mIndicatorView = null;
  }

  private void update() {
    releaseListener();
    removeAllViews();
    addTabContent(getContext());
    addIndicatorView(getContext());
  }

  private void addTabContent(Context context) {
    if (mTitles == null || mTitles.isEmpty()) return;
    if (mContentLayout != null) {
      releaseListener();
      removeView(mContentLayout);
    }
    mContentLayout = new LinearLayout(context);
    LayoutParams params =
        new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    mContentLayout.setOrientation(LinearLayout.HORIZONTAL);
    mContentLayout.setLayoutParams(params);
    for (int i = 0; i < mTitles.size(); i++) {
      TextView textView = new TextView(context);
      textView.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f));
      textView.setGravity(Gravity.CENTER);
      textView.setText(mTitles.get(i));
      textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
      textView.setTag(i);
      textView.setTextColor(csl);
      textView.setOnClickListener(this);
      if (i == 0) textView.setSelected(true);
      mContentLayout.addView(textView);
    }
    addView(mContentLayout);
  }

  private void addIndicatorView(Context context) {
    if (mTitles == null || mTitles.isEmpty()) return;
    if (mIndicatorView != null) removeView(mIndicatorView);
    mIndicatorView = new IndicatorView(context);
    mIndicatorView.setNum(mTitles.size());
    mIndicatorView.setMargin(margin);
    mIndicatorView.setRoundCorner(rd);
    mIndicatorView.setIndicatorColor(
        csl.getColorForState(new int[] {android.R.attr.state_selected}, Color.BLUE));
    LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10);
    params.gravity = Gravity.BOTTOM;
    mIndicatorView.setLayoutParams(params);
    addView(mIndicatorView);
  }

  private void releaseListener() {
    int size = mTitles == null ? 0 : mTitles.size();
    for (int i = 0; i < size; i++) {
      View view = findViewWithTag(i);
      if (view != null) view.setOnClickListener(null);
    }
    if (viewPager != null) {
      viewPager.removeOnPageChangeListener(this);
    }
  }

  public static int sp2px(Context context, float spValue) {
    final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
    return (int) (spValue * fontScale + 0.5f);
  }
}
