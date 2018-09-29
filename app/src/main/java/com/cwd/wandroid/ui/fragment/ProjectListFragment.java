package com.cwd.wandroid.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cwd.wandroid.R;
import com.cwd.wandroid.adapter.ArticleAdapter;
import com.cwd.wandroid.api.ApiService;
import com.cwd.wandroid.api.RetrofitUtils;
import com.cwd.wandroid.base.BaseFragment;
import com.cwd.wandroid.contract.ArticleContract;
import com.cwd.wandroid.contract.ProjectContract;
import com.cwd.wandroid.contract.ProjectListContract;
import com.cwd.wandroid.entity.ArticleInfo;
import com.cwd.wandroid.entity.ProjectCategory;
import com.cwd.wandroid.presenter.ArticlePresenter;
import com.cwd.wandroid.presenter.ProjectListPresenter;
import com.cwd.wandroid.source.DataManager;
import com.cwd.wandroid.ui.activity.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ProjectListFragment extends BaseFragment implements ProjectListContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_article)
    RecyclerView rvArticle;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private static final String CID = "cid";
    private static final String IS_SYSTEM = "isSystem";

    private int cid;
    private int page = 0;
    private boolean isRefresh;
    private boolean isSystem;

    private ProjectListPresenter projectListPresenter;
    private DataManager dataManager;
    private ArticleAdapter articleAdapter;
    private List<ArticleInfo> articleInfoList = new ArrayList<>();

    public ProjectListFragment() {

    }

    /**
     *
     * @param cid
     * @param isSystem 是否是体系下文章
     * @return
     */
    public static ProjectListFragment newInstance(int cid,boolean isSystem) {
        ProjectListFragment fragment = new ProjectListFragment();
        Bundle args = new Bundle();
        args.putInt(CID, cid);
        args.putBoolean(IS_SYSTEM,isSystem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cid = getArguments().getInt(CID);
            isSystem = getArguments().getBoolean(IS_SYSTEM);
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
        projectListPresenter = new ProjectListPresenter(dataManager);
        projectListPresenter.attachView(this);
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
                String url = articleInfo.getLink();
                String title = articleInfo.getTitle();
                WebViewActivity.startAction(getContext(),title,url);
            }
        });
        articleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isRefresh = false;
                page++;
                projectListPresenter.getProjectList(page,cid,isSystem);
            }
        },rvArticle);
        rvArticle.setAdapter(articleAdapter);
        refreshLayout.setRefreshing(true);
        projectListPresenter.getProjectList(page,cid,isSystem);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
        projectListPresenter.getProjectList(page,cid,isSystem);
    }

    @Override
    public void showProjectList(List<ArticleInfo> projectList, boolean isEnd) {
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
        articleInfoList.addAll(projectList);
        articleAdapter.notifyDataSetChanged();
    }
}
