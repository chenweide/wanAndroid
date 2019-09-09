package com.cwd.wandroid.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.cwd.wandroid.R;
import com.cwd.wandroid.api.ApiService;
import com.cwd.wandroid.api.RetrofitUtils;
import com.cwd.wandroid.base.BaseActivity;
import com.cwd.wandroid.contract.CollectContract;
import com.cwd.wandroid.entity.Article;
import com.cwd.wandroid.entity.ArticleInfo;
import com.cwd.wandroid.entity.MessageEvent;
import com.cwd.wandroid.presenter.CollectPresenter;
import com.cwd.wandroid.source.DataManager;
import com.cwd.wandroid.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

public class WebViewActivity extends BaseActivity implements CollectContract.View{

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.web_view_progressbar)
    ProgressBar progressBar;
    @BindView(R.id.web_view)
    WebView webView;

    private String url;
    private String title;
    private int id;
    private int originId;
    private boolean enableCollect;
    //是否从收藏列表跳转过来的
    private boolean isFromCollect;
    //发送取消收藏发送通知到收藏列表，但需要等到回到收藏列表后再移除，视觉效果好些
    private int position = -1;

    private MenuItem menuItem;
    private CollectPresenter collectPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    public void createPresenter() {
        DataManager dataManager = new DataManager(RetrofitUtils.get().retrofit().create(ApiService.class));
        collectPresenter = new CollectPresenter(dataManager);
        collectPresenter.attachView(this);
    }

    public static void startAction(Context context, ArticleInfo articleInfo){
        startAction(context,articleInfo,true,false,0);
    }

    public static void startAction(Context context, ArticleInfo articleInfo,boolean enableCollect,boolean isFromCollect,int position){
        Intent intent = new Intent(context,WebViewActivity.class);
        intent.putExtra("ARTICLE_INFO",articleInfo);
        intent.putExtra("ENABLE_COLLECT",enableCollect);
        intent.putExtra("IS_FROM_COLLECT",isFromCollect);
        intent.putExtra("POSITION",position);
        context.startActivity(intent);
    }

    @Override
    public void init() {
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        ArticleInfo articleInfo = (ArticleInfo) intent.getSerializableExtra("ARTICLE_INFO");
        url = articleInfo.getLink();
        title = articleInfo.getTitle();
        id = articleInfo.getId();
        originId = articleInfo.getOriginId() == 0 ? -1 : articleInfo.getOriginId();
        enableCollect = intent.getBooleanExtra("ENABLE_COLLECT",true);
        isFromCollect = intent.getBooleanExtra("IS_FROM_COLLECT",false);
        position = intent.getIntExtra("POSITION",0);
        toolbar.setTitle(title != null ? title : "");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(progressBar != null){
                    progressBar.setProgress(newProgress);
                    if (newProgress >= 100) {
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_view_menu,menu);
        menuItem = menu.getItem(0);
        if(!enableCollect){
            //如果是banner点进来的则不显示收藏按钮
            menuItem.setVisible(false);
        }else{
            if(isFromCollect){
                menuItem.setTitle("取消收藏");
            }else{
                menuItem.setTitle("收藏");
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(webView.canGoBack()){
                    webView.goBack();
                }else{
                    finish();
                }
                break;
            case R.id.collect:
                if(isFromCollect){
                    //取消收藏
                    collectPresenter.cancelCollectArticle(id,originId);
                }else{
                    //收藏
                    collectPresenter.collectArticle(id);
                }
                break;
            case R.id.open_by_browser:
                openByBrowser();
                break;
            case R.id.refresh:
                webView.reload();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openByBrowser(){
        if(url.startsWith("http://") || url.startsWith("https://")){
            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
            startActivity(intent);
        }
    }

    @Override
    public void showArticleList(List<ArticleInfo> articleInfoList, boolean isEnd) {

    }

    @Override
    public void showNoCollectView() {

    }

    @Override
    public void showCollectSuccess() {
        ToastUtils.showShort("收藏成功");
    }

    @Override
    public void showCancelCollectSuccess() {
        ToastUtils.showShort("取消收藏成功");
        MessageEvent event = new MessageEvent();
        event.setPosition(position);
        EventBus.getDefault().post(event);
    }

}
