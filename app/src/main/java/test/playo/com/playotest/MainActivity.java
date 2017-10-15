


package test.playo.com.playotest;

import android.net.Uri;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import test.playo.com.playotest.datamanager.DataLoadingSubject;
import test.playo.com.playotest.datamanager.HitItemDataManager;
import test.playo.com.playotest.http.OKHttpHelper;
import test.playo.com.playotest.model.Hits;

public class MainActivity extends AppCompatActivity implements DataLoadingSubject.DataLoadingCallbacks {
    private static final String TAG = "MainActivity";
    RecyclerView recyclerView;
    HitItemDataManager<Hits> hitsHitItemDataManager;
    HitItemListAdapter hitItemListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String feedurl = "https://hn.algolia.com/api/v1/search";
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setVisibility(View.INVISIBLE);


        final LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemViewCacheSize(0);
        hitsHitItemDataManager = new HitItemDataManager<Hits>(MainActivity.this, MainActivity.this, OKHttpHelper.getInstanceOf(MainActivity.this));
        hitsHitItemDataManager.registerCallback(this);

    }

    @Override
    public void dataStartedLoading() {

    }

    @Override
    public void dataFinishedLoading(boolean isSuccess, int code) {
        if (recyclerView.getVisibility() == View.INVISIBLE)
            recyclerView.setVisibility(View.VISIBLE);
        if (isSuccess) {

            if (hitItemListAdapter == null) {
                hitItemListAdapter = new HitItemListAdapter(MainActivity.this, hitsHitItemDataManager.getList(), MainActivity.this,hitsHitItemDataManager);
                recyclerView.setAdapter(hitItemListAdapter);
            }
//            videoListAdapter.notifyDataSetChanged();
            ProgressBar progressbar = (ProgressBar) findViewById(R.id.progress_bar);
            progressbar.setVisibility(View.GONE);


        }

    }
}
