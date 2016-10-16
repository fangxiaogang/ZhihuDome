package com.example.xiaogang.zhihu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.List;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity {
    private static RecyclerView recyclerview;
    private LinearLayoutManager linearLayoutManager;
    private List<zhihu.Stories> stories;
    private ZhihuAdapter zhihuAdapter;
    private zhihu zhi;
    String url = "http://news-at.zhihu.com/api/4/news/latest";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerview = (RecyclerView)findViewById(R.id.recycler);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerview.setLayoutManager(linearLayoutManager);
        loadLatestData();

    }
    private void loadLatestData() {
        OkUtil.getInstance().okHttpZhihuGson(url, new OkUtil.ResultCallback<zhihu>
                () {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(zhihu response, String json) {
                if (response != null ) {
                    zhi = response;
                    System.out.println(zhi.getStories().get(0).getTitle());
                    stories = zhi.getStories();
                    System.out.println(stories.get(0).getTitle()+"5555555");
                    zhihuAdapter = new ZhihuAdapter(MainActivity.this,stories);
                    recyclerview.setAdapter(zhihuAdapter);
                    zhihuAdapter.setOnItemClickListener(new ZhihuAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view) {
                            int position=recyclerview.getChildAdapterPosition(view);
                            final Intent intent = new Intent(MainActivity.this,ZhihunewActivity.class);
                            intent.putExtra("image",stories.get(position).getString().get(0));
                            intent.putExtra("id",String.valueOf(stories.get(position).getId()));
                            intent.putExtra("title",stories.get(position).getTitle());
                            CircularAnim.fullActivity(MainActivity.this, view)
                                    .colorOrImageRes(R.color.colorPrimary)
                                    .duration(300)
                                    .go(new CircularAnim.OnAnimationEndListener() {
                                        @Override
                                        public void onAnimationEnd() {
                                            startActivity(intent);
                                        }
                                    });

                        }
                    });

                }
            }
        });
    }

}
