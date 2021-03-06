## dependencies
```
implementation 'com.gu:indicator:1.0.4'
```
## 效果
margin+圆角

![image](https://github.com/gu0-kim/IndicatorMaster/blob/master/screen/tablayout_1.gif)

圆角+充满

![image](https://github.com/gu0-kim/IndicatorMaster/blob/master/screen/tablayout_2.gif)

直角+充满

![image](https://github.com/gu0-kim/IndicatorMaster/blob/master/screen/tablayout_3.gif)

## 简单使用

1. 设置标题数组
2. 绑定viewPager
```

private static final String[] titles = {"第一页", "第二页", "第三页"};

@Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    vp.setAdapter(new PageAdapter(getSupportFragmentManager()));
    
    //TabLayout设置标题
    mTabLayout.createContentByTitles(titles).setViewPager(vp).combine();
  }
```

## 动态修改样式

```
public void update() {
    mTabLayout
        .setTextColors(ContextCompat.getColorStateList(this, R.color.tab_text_state_color)) //设置text颜色
        .setIndicatorColor(ContextCompat.getColor(this, R.color.colorPrimaryDark)) //设置indicator颜色
        .setMargin(10) //设置边距
        .setTextSize(14) //设置文字大小
        .update(); //生效设置
  }
```

## xml文件引入
```
<com.gu.indicator.TabLayout
        android:id="@+id/tablayout"
        android:layout_width="match_parent"
        android:layout_height="40dp"
        app:margin="24dp"
        app:rd="8dp"
        app:textColor="@color/text_state_color"
        app:textSize="14sp" />
```

## 相关参数介绍
- 设置indicator距离tablayout左侧的距离

```
app:margin="24dp"
//0dp时充满title按钮
```

- 设置indicator圆角半径
```
app:rd="8dp"
```

- 设置颜色
```
app:textColor="@color/text_state_color"

//indicator颜色会自动使用text selected状态时的颜色
```
res/color/text_stat_color.xml
```
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:color="@color/deep_orange_500" android:state_selected="true" />
    <item android:color="@color/grey_500" />
</selector>
```

- 设置文字大小
```
app:textSize="14sp"
```

