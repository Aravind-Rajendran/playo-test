


package test.playo.com.playotest;

import android.net.Uri;

import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import test.playo.com.playotest.datamanager.DataLoadingSubject;
import test.playo.com.playotest.datamanager.HitItemDataManager;
import test.playo.com.playotest.datamanager.InfiniteScrollListener;
import test.playo.com.playotest.http.OKHttpHelper;
import test.playo.com.playotest.model.Hits;


public class MainActivity extends AppCompatActivity implements DataLoadingSubject.DataLoadingCallbacks , AdapterView.OnItemSelectedListener {
    private static final String TAG = "MainActivity";
    RecyclerView recyclerView;
    HitItemDataManager<Hits> hitsHitItemDataManager;
    HitItemListAdapter hitItemListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);




        String feedurl = "https://hn.algolia.com/api/v1/search";
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setVisibility(View.INVISIBLE);


        final LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);



        hitsHitItemDataManager = new HitItemDataManager<Hits>(MainActivity.this, MainActivity.this, OKHttpHelper.getInstanceOf(MainActivity.this));
        hitsHitItemDataManager.registerCallback(this);
        InfiniteScrollListener listener = new InfiniteScrollListener(layoutManager, hitsHitItemDataManager) {
            @Override
            public void onLoadMore() {
                hitsHitItemDataManager.loadmoreData();
            }
        };
        recyclerView.addOnScrollListener(listener);

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

            ProgressBar progressbar = (ProgressBar) findViewById(R.id.progress_bar);
            progressbar.setVisibility(View.GONE);


        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


        Log.i(TAG,"spinner choose"+adapterView.getAdapter().getItem(i));


        hitsHitItemDataManager.setQuery(adapterView.getAdapter().getItem(i).toString());
        hitsHitItemDataManager.clearData();







    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
