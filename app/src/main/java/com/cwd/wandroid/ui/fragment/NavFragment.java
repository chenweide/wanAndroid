package com.cwd.wandroid.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cwd.wandroid.R;
import com.cwd.wandroid.adapter.NavAdapter;
import com.cwd.wandroid.adapter.ProjectFragmentAdapter;
import com.cwd.wandroid.api.ApiService;
import com.cwd.wandroid.api.RetrofitUtils;
import com.cwd.wandroid.base.BaseFragment;
import com.cwd.wandroid.contract.NavContract;
import com.cwd.wandroid.contract.ProjectContract;
import com.cwd.wandroid.entity.ArticleInfo;
import com.cwd.wandroid.entity.NavInfo;
import com.cwd.wandroid.entity.NavTitle;
import com.cwd.wandroid.entity.ProjectCategory;
import com.cwd.wandroid.entity.SystemDetail;
import com.cwd.wandroid.presenter.NavPresenter;
import com.cwd.wandroid.presenter.ProjectPresenter;
import com.cwd.wandroid.source.DataManager;
import com.cwd.wandroid.ui.activity.WebViewActivity;
import com.cwd.wandroid.ui.widget.FlowLayout;
import com.cwd.wandroid.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NavFragment extends BaseFragment implements NavContract.View {

    @BindView(R.id.fl_nav)
    FlowLayout flNav;
    @BindView(R.id.rv_nav_title)
    RecyclerView rvNavTitle;

    private NavPresenter navPresenter;
    private DataManager dataManager;
    private NavAdapter navAdapter;
    private List<NavTitle> navTitles = new ArrayList<>();

    public NavFragment() {

    }

    public static NavFragment newInstance() {
        return new NavFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_nav;
    }

    @Override
    public void createPresenter() {
        dataManager = new DataManager(RetrofitUtils.get().retrofit().create(ApiService.class));
        navPresenter = new NavPresenter(dataManager);
        navPresenter.attachView(this);
    }

    @Override
    public void init() {
        rvNavTitle.setLayoutManager(new LinearLayoutManager(getActivity()));
        navAdapter = new NavAdapter(R.layout.item_nav,navTitles);
        rvNavTitle.setAdapter(navAdapter);
        navPresenter.getNavInfo();
        navAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NavTitle navTitle = navTitles.get(position);
                initTab(flNav,navTitle.getArticles());
                navAdapter.setHighLightItem(position);
            }
        });
    }

    private void initTab(FlowLayout flowLayout, final List<NavInfo> tags) {
        flowLayout.removeAllViews();
        LinearLayout.MarginLayoutParams layoutParams = new LinearLayout.MarginLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置边距
        layoutParams.setMargins(30, 30, 10, 10);
        for (int i = 0; i < tags.size(); i++) {
            final NavInfo navInfo = tags.get(i);
            final TextView textView = new TextView(getActivity());
            textView.setTag(i);
            textView.setTextSize(15);
            textView.setText(tags.get(i).getTitle());
            textView.setPadding(DensityUtil.dip2px(getActivity(),18), DensityUtil.dip2px(getActivity(),5), DensityUtil.dip2px(getActivity(),18), DensityUtil.dip2px(getActivity(),5));
            textView.setTextColor(ContextCompat.getColor(getActivity(),R.color.textColor));
            textView.setBackgroundResource(R.drawable.flow_tab_bg);
            flowLayout.addView(textView, layoutParams);
            // 标签点击事件
            final ArticleInfo articleInfo = new ArticleInfo();
            articleInfo.setTitle(navInfo.getTitle());
            articleInfo.setLink(navInfo.getLink());
            articleInfo.setId(navInfo.getId());
            articleInfo.setCollect(navInfo.isCollect());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebViewActivity.startAction(getActivity(),articleInfo);
                }
            });
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void showNavInfo(List<NavTitle> navTitleList) {
        navTitles.clear();
        navTitles.addAll(navTitleList);
        navAdapter.notifyDataSetChanged();
        initTab(flNav,navTitleList.get(0).getArticles());
    }
}
