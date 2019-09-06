package com.cwd.wandroid.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cwd.wandroid.R;
import com.cwd.wandroid.entity.ArticleInfo;

import java.util.List;

public class ArticleAdapter extends BaseQuickAdapter<ArticleInfo,BaseViewHolder> {

    public ArticleAdapter(int layoutResId, @Nullable List<ArticleInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleInfo item) {
        helper.setText(R.id.tv_author,item.getAuthor());
        helper.setText(R.id.tv_date,item.getNiceDate());
        helper.setText(R.id.tv_title,item.getTitle());
        helper.setText(R.id.tv_category,item.getChapterName());
        int type = item.getType();
        if(type == 1){
            helper.setVisible(R.id.tv_flag,true);
            helper.setText(R.id.tv_flag,"TOP");
        }else{
           if(item.isFresh()){
               helper.setVisible(R.id.tv_flag,true);
               helper.setText(R.id.tv_flag,"NEW");
           }else{
               helper.setVisible(R.id.tv_flag,false);
           }
        }
    }
}
