package com.cwd.wandroid.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cwd.wandroid.R;
import com.cwd.wandroid.adapter.ArticleAdapter;
import com.cwd.wandroid.api.ApiService;
import com.cwd.wandroid.api.RetrofitUtils;
import com.cwd.wandroid.base.BaseActivity;
import com.cwd.wandroid.contract.CollectContract;
import com.cwd.wandroid.entity.ArticleInfo;
import com.cwd.wandroid.entity.MessageEvent;
import com.cwd.wandroid.presenter.CollectPresenter;
import com.cwd.wandroid.source.DataManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CollectActivity extends BaseActivity implements CollectContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.rv_article)
    RecyclerView rvArticle;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;


    private CollectPresenter collectPresenter;
    private DataManager dataManager;
    private ArticleAdapter articleAdapter;
    private List<ArticleInfo> articleInfoList = new ArrayList<>();
    private int page = 0;
    private boolean isRefresh;
    private int needRemoveItemPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_collect;
    }

    @Override
    public void createPresenter() {
        dataManager = new DataManager(RetrofitUtils.get().retrofit().create(ApiService.class));
        collectPresenter = new CollectPresenter(dataManager);
        collectPresenter.attachView(this);
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("收藏");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setOnRefreshListener(this);
        rvArticle.setLayoutManager(new LinearLayoutManager(context));
        articleAdapter = new ArticleAdapter(R.layout.item_article,articleInfoList);
        articleAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        articleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleInfo articleInfo = articleInfoList.get(position);
                WebViewActivity.startAction(context,articleInfo,true,true,position);
            }
        });
        articleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isRefresh = false;
                page++;
                collectPresenter.getCollectList(page);
            }
        },rvArticle);
        rvArticle.setAdapter(articleAdapter);
        collectPresenter.getCollectList(page);
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
        collectPresenter.getCollectList(page);
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
    public void showNoCollectView() {
        refreshLayout.setRefreshing(false);
        View emptyView = View.inflate(context,R.layout.empty_view,null);
        TextView tvContent = emptyView.findViewById(R.id.tv_content);
        tvContent.setText("暂无收藏文章");
        articleAdapter.setEmptyView(emptyView);
    }

    @Override
    public void showCollectSuccess() {

    }

    @Override
    public void showCancelCollectSuccess() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(needRemoveItemPosition != -1){
            articleInfoList.remove(needRemoveItemPosition);
            if(!articleInfoList.isEmpty()){
                articleAdapter.notifyItemRemoved(needRemoveItemPosition);
            }else{
                articleAdapter.notifyDataSetChanged();
            }
            needRemoveItemPosition = -1;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        collectPresenter.detachView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        needRemoveItemPosition = event.getPosition();
    }
}
