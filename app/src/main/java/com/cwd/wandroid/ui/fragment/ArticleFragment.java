package com.cwd.wandroid.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cwd.wandroid.R;
import com.cwd.wandroid.adapter.ArticleAdapter;
import com.cwd.wandroid.api.ApiService;
import com.cwd.wandroid.api.RetrofitUtils;
import com.cwd.wandroid.base.BaseFragment;
import com.cwd.wandroid.contract.ArticleContract;
import com.cwd.wandroid.entity.ArticleInfo;
import com.cwd.wandroid.entity.Banner;
import com.cwd.wandroid.presenter.ArticlePresenter;
import com.cwd.wandroid.source.DataManager;
import com.cwd.wandroid.ui.activity.WebViewActivity;
import com.cwd.wandroid.utils.GlideImageLoader;
import com.cwd.wandroid.utils.ToastUtils;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import butterknife.BindView;

public class ArticleFragment extends BaseFragment implements ArticleContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_article)
    RecyclerView rvArticle;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private com.youth.banner.Banner bannerView;
    private View bannerLayout;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private int page = 0;
    private boolean isRefresh;

    private ArticlePresenter articlePresenter;
    private DataManager dataManager;
    private ArticleAdapter articleAdapter;
    private List<ArticleInfo> articleInfoList = new ArrayList<>();

    public ArticleFragment() {

    }

    public static ArticleFragment newInstance() {
        ArticleFragment fragment = new ArticleFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_article;
    }

    @Override
    public void createPresenter() {
        dataManager = new DataManager(RetrofitUtils.get().retrofit().create(ApiService.class));
        articlePresenter = new ArticlePresenter(dataManager);
        articlePresenter.attachView(this);
    }

    @Override
    public void init() {
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setOnRefreshListener(this);
        rvArticle.setLayoutManager(new LinearLayoutManager(getContext()));
        articleAdapter = new ArticleAdapter(R.layout.item_article,articleInfoList);
        articleAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        articleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleInfo articleInfo = articleInfoList.get(position);
                WebViewActivity.startAction(getContext(),articleInfo);
            }
        });
        articleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isRefresh = false;
                page++;
                articlePresenter.getArticleList(page);
            }
        },rvArticle);
        rvArticle.setAdapter(articleAdapter);
        refreshLayout.setRefreshing(true);
        articlePresenter.getArticleList(page);
        articlePresenter.getBanner();
        bannerLayout = LayoutInflater.from(context).inflate(R.layout.banner_layout,null);
        bannerView = bannerLayout.findViewById(R.id.banner);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void showArticleList(List<ArticleInfo> list,boolean isEnd) {
        refreshLayout.setRefreshing(false);
        if(isRefresh){
            articleInfoList.clear();
        }else{
            if(isEnd){
                articleAdapter.loadMoreEnd();
            }else{
                articleAdapter.loadMoreComplete();
            }
        }
        articleInfoList.addAll(list);
        articleAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNoSearchResultView() {

    }

    @Override
    public void showBanner(List<Banner> banners) {
        if(banners.isEmpty()){
            return;
        }
        List<String> images = new ArrayList<>();
        final List<String> titles = new ArrayList<>();
        final List<String> urls = new ArrayList<>();
        final List<Integer> ids = new ArrayList<>();
        for(Banner banner : banners){
            images.add(banner.getImagePath());
            titles.add(banner.getTitle());
            urls.add(banner.getUrl());
            ids.add(banner.getId());
        }

        bannerView.isAutoPlay(true);
        bannerView.setImages(images);
        bannerView.setBannerTitles(titles);
        bannerView.setImageLoader(new GlideImageLoader());
        bannerView.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                String link = urls.get(position);
                String title = titles.get(position);
                ArticleInfo articleInfo = new ArticleInfo();
                articleInfo.setLink(link);
                articleInfo.setTitle(title);
                WebViewActivity.startAction(context,articleInfo,false,false,position);
            }
        });
        bannerView.start();
        articleAdapter.addHeaderView(bannerLayout);
    }

    @Override
    public void showError(String message) {
        super.showError(message);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        page = 0;
        isRefresh = true;
        articlePresenter.getArticleList(page);
    }

    @Override
    public void onStart() {
        super.onStart();
        bannerView.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        bannerView.stopAutoPlay();
    }
}
