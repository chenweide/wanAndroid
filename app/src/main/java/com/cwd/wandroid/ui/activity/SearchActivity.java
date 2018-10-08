package com.cwd.wandroid.ui.activity;

import android.animation.Animator;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cwd.wandroid.R;
import com.cwd.wandroid.adapter.ArticleAdapter;
import com.cwd.wandroid.api.ApiService;
import com.cwd.wandroid.api.RetrofitUtils;
import com.cwd.wandroid.base.BaseActivity;
import com.cwd.wandroid.contract.ArticleContract;
import com.cwd.wandroid.entity.ArticleInfo;
import com.cwd.wandroid.entity.Banner;
import com.cwd.wandroid.presenter.ArticlePresenter;
import com.cwd.wandroid.source.DataManager;
import com.cwd.wandroid.ui.fragment.ArticleFragment;
import com.cwd.wandroid.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchActivity extends BaseActivity implements ArticleContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.rv_article)
    RecyclerView rvArticle;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;


    private ArticlePresenter articlePresenter;
    private DataManager dataManager;
    private ArticleAdapter articleAdapter;
    private List<ArticleInfo> articleInfoList = new ArrayList<>();
    private int page = 0;
    private boolean isRefresh;
    private String keyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void createPresenter() {
        dataManager = new DataManager(RetrofitUtils.get().retrofit().create(ApiService.class));
        articlePresenter = new ArticlePresenter(dataManager);
        articlePresenter.attachView(this);
    }

    @Override
    public void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setOnRefreshListener(this);
        rvArticle.setLayoutManager(new LinearLayoutManager(mContext));
        articleAdapter = new ArticleAdapter(R.layout.item_article,articleInfoList);
        articleAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        articleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleInfo articleInfo = articleInfoList.get(position);
                String url = articleInfo.getLink();
                String title = articleInfo.getTitle();
                WebViewActivity.startAction(mContext,title,url);
            }
        });
        articleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isRefresh = false;
                page++;
                articlePresenter.getSearchList(page,keyword);
            }
        },rvArticle);
        rvArticle.setAdapter(articleAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setIconified(false);
        searchView.onActionViewExpanded();
        searchView.setQueryHint("搜索文章...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                articleInfoList.clear();
                keyword = newText;
                if(TextUtils.isEmpty(keyword)){
                    keyword = "";
                }
                articlePresenter.getSearchList(page,keyword);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
        articlePresenter.getSearchList(page,keyword);
    }

    @Override
    public void showArticleList(List<ArticleInfo> list, boolean isEnd) {
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
        View emptyView = View.inflate(mContext,R.layout.empty_view,null);
        TextView tvContent = emptyView.findViewById(R.id.tv_content);
        tvContent.setText("暂无搜索结果，换个关键词试试");
        articleAdapter.setEmptyView(emptyView);
    }

    @Override
    public void showBanner(List<Banner> banners) {

    }
}
