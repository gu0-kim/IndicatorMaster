package com.gu.example.indicator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ItemFragment extends Fragment {
  public static ItemFragment newInstance(String tag) {
    Bundle data = new Bundle();
    data.putString("tag", tag);
    ItemFragment fragment = new ItemFragment();
    fragment.setArguments(data);
    return fragment;
  }

  public String getTagName() {
    if (getArguments() != null) return getArguments().getString("tag", "");
    return "";
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View parent = inflater.inflate(R.layout.fragment_layout, container, false);
    ((TextView) parent).setText(getTagName());
    return parent;
  }
}
