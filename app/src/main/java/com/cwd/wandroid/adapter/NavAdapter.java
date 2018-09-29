package com.cwd.wandroid.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cwd.wandroid.R;
import com.cwd.wandroid.entity.NavTitle;
import com.cwd.wandroid.entity.System;
import com.cwd.wandroid.entity.SystemDetail;
import com.cwd.wandroid.ui.widget.FlowLayout;
import com.cwd.wandroid.utils.DensityUtil;

import java.util.List;

public class NavAdapter extends BaseQuickAdapter<NavTitle,BaseViewHolder> {

    private int highLightItemPosition;

    public NavAdapter(int layoutResId, @Nullable List<NavTitle> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NavTitle item) {
        if(helper.getLayoutPosition() == highLightItemPosition){
            helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.grayBtn));
        }else{
            helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.grayBg));
        }
        helper.setText(R.id.tv_title,item.getName());
    }

    public void setHighLightItem(int position){
        this.highLightItemPosition = position;
        notifyDataSetChanged();
    }
}
