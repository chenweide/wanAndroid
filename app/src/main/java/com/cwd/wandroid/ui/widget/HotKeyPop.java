package com.cwd.wandroid.ui.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cwd.wandroid.R;
import com.cwd.wandroid.entity.ArticleInfo;
import com.cwd.wandroid.entity.HotKey;
import com.cwd.wandroid.entity.NavInfo;
import com.cwd.wandroid.ui.activity.WebViewActivity;
import com.cwd.wandroid.utils.DensityUtil;
import com.paulyung.laybellayout.LaybelLayout;

import java.util.ArrayList;
import java.util.List;

public class HotKeyPop extends PopupWindow {
    
    private Context context;
    private LaybelLayout flowLayout;

    private OnHotKeyClickListener listener;
    public interface OnHotKeyClickListener{
        void onHotKeyClick(String key);
    }

    public void setOnHotKeyClickListener(OnHotKeyClickListener listener){
        this.listener = listener;
    }

    public HotKeyPop(Context context, final List<HotKey> hotKeyList){
        this.context = context;
        View window = LayoutInflater.from(context).inflate(R.layout.hot_key_layout,null);
        flowLayout = window.findViewById(R.id.fl_nav);
        flowLayout.setAdapter(new LaybelLayout.Adapter(handleTag(hotKeyList)));
        flowLayout.setOnItemClickListener(new LaybelLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int p) {
                if(listener != null){
                    listener.onHotKeyClick(hotKeyList.get(p).getName());
                    dismiss();
                }else{
                    throw new IllegalStateException("OnHotKeyClickListener must not be null");
                }
            }
        });
        setContentView(window);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setAnimationStyle(R.style.PopupAnimation);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        setBackgroundDrawable(dw);
    }

    private List<String> handleTag(List<HotKey> hotKeyList){
        List<String> keys = new ArrayList<>();
        for (HotKey hotKey : hotKeyList){
            keys.add(hotKey.getName());
        }
        return keys;
    }

    private void initTab(FlowLayout flowLayout, final List<HotKey> tags) {
        flowLayout.removeAllViews();
        LinearLayout.MarginLayoutParams layoutParams = new LinearLayout.MarginLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置边距
//        layoutParams.setMargins(30, 30, 10, 10);
        for (int i = 0; i < tags.size(); i++) {
            final HotKey hotKey = tags.get(i);
            final TextView textView = new TextView(context);
            textView.setTag(i);
            textView.setTextSize(15);
            textView.setText(hotKey.getName());
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(DensityUtil.dip2px(context, 18), DensityUtil.dip2px(context, 5), DensityUtil.dip2px(context, 18), DensityUtil.dip2px(context, 5));
            textView.setTextColor(ContextCompat.getColor(context, R.color.textColor));
            textView.setBackgroundResource(R.drawable.flow_tab_bg);
            flowLayout.addView(textView, layoutParams);
            // 标签点击事件
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onHotKeyClick(hotKey.getName());
                        dismiss();
                    }else{
                        throw new IllegalStateException("OnHotKeyClickListener must not be null");
                    }
                }
            });
        }
    }
}
