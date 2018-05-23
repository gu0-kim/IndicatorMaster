package com.gu.example.indicator;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class App extends Application {
  private RefWatcher mRefWatcher;

  public RefWatcher getRefWatcher() {
    return mRefWatcher;
  }

  public void checkItem(Object item) {
    if (item == null) return;
    if (mRefWatcher != null) mRefWatcher.watch(item);
  }

  @Override
  public void onCreate() {
    super.onCreate();
    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return;
    }
    //    enabledStrictMode();
    mRefWatcher = LeakCanary.install(this);
  }
}
