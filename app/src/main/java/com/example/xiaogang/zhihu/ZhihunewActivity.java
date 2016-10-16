package com.example.xiaogang.zhihu;


import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import okhttp3.Call;

public class ZhihunewActivity extends AppCompatActivity {
     private String id = "id";//不写这个下面就要getStringExtra("id"),当时一时没有反应过来要加""
    private String title = "title";
    public  Zhihunews zhihunews;
    private  String url = id;
    private WebView webView;
    private ImageView imageView;
    private String image = "image";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihunew);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int newsid = Integer.valueOf(getIntent().getStringExtra(id));
        title = getIntent().getStringExtra(title);
        image =getIntent().getStringExtra(image);
        getSupportActionBar().setTitle(title);
        url = "http://news-at.zhihu.com/api/4/news/"+newsid;
        System.out.println(url);
        webView = (WebView) findViewById(R.id.zhuhunews_webview);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //        加载缓存，如果不存在就加载网络数据
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //        app cache
        settings.setAppCacheEnabled(true);
        //        dom storage
        settings.setDomStorageEnabled(true);
        //        database cache
        settings.setDatabaseEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        imageView = (ImageView) findViewById(R.id.zhihunews_image);

        loadLatestData();
        initView();

    }


    private void loadLatestData() {
        OkUtil.getInstance().okHttpZhihuGson(url, new OkUtil.ResultCallback<Zhihunews>
                () {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Zhihunews response, String json) {
                if (response != null ) {
                    zhihunews = response;
                    System.out.println(zhihunews.getTitle());
                    loadWebView();
                }
            }
        });
    }
    private void loadWebView() {
        Picasso.with(this).load(zhihunews.getImage()).into(imageView);
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news\" " +
                "type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + zhihunews.getBody() + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        webView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
    }
    private void initView() {

        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbartop);
//        mCollapsingToolbarLayout.setTitle();
//        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
//        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.GREEN);//设置收缩后Toolbar上字体的颜色
//        想好页面布局在设置
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
