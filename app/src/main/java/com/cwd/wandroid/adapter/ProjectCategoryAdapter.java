package com.cwd.wandroid.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cwd.wandroid.R;
import com.cwd.wandroid.entity.ArticleInfo;
import com.cwd.wandroid.entity.ProjectCategory;

import java.util.List;

public class ProjectCategoryAdapter extends BaseQuickAdapter<ProjectCategory,BaseViewHolder> {

    public ProjectCategoryAdapter(int layoutResId, @Nullable List<ProjectCategory> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProjectCategory item) {
        helper.setText(R.id.tv_category,item.getName());
    }
}
