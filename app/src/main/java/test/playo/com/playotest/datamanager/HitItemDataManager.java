package test.playo.com.playotest.datamanager;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import test.playo.com.playotest.MainActivity;
import test.playo.com.playotest.model.Hits;
import test.playo.com.playotest.utils.NullPrimitiveAdapter;
import test.playo.com.playotest.utils.NullPrimitiveJsonAdapter;


/**
 * Copyright (c) 2016 Voonik Technologies Pvt Limited. All rights reserved
 * Author : Aravind Rajendran
 * Date   : 06/12/16.
 */

public class HitItemDataManager<T> extends BaseDataManager<T> implements DataChangeNotifier {
    List<T> feedItems = new ArrayList<>();

    Context context;

    String url = "https://hn.algolia.com/api/v1/search";


    Uri feedUri;
    boolean noMoreItems = false;
    int limit;

    LinearLayoutManager layoutManager;
    OkHttpClient okHttpClient;

   MainActivity mainActivity;

    Loader loader = new Loader(null);

    private  int page = 0;



    boolean isComingFromCategoryFeed= false;
    final Moshi moshi=new Moshi.Builder().add(NullPrimitiveJsonAdapter.FACTORY).add(new NullPrimitiveAdapter()).build();

    private static final String TAG = "VoonikTVDataManager";
    private List<DataChangeNotifier.DataChangedCallbacks> itemChangedCallbacks =
            Collections.synchronizedList(new ArrayList<DataChangeNotifier.DataChangedCallbacks>());

    public HitItemDataManager(@NonNull Context context, MainActivity mainActivity, OkHttpClient okHttpClient) {
        super(context);
        this.mainActivity=mainActivity;
        this.context = context;
        this.okHttpClient=okHttpClient;

        page=1;
        feedUri = Uri.parse(url);
        loaddata(feedUri);





    }


    public HitItemDataManager(@NonNull Context context , String url  ) {
        super(context);

        this.context = context;

        page=1;
        feedUri = Uri.parse(url);
        loaddata(feedUri);

    }


  /*  @Override
    public void success(int code, VoonikTv object, Request request) {
        addVoonikData(object);
        if (object.getVideos_list()!=null&&object.getVideos_list().get(0).getUrl()!=null)
            feedUri=Uri.parse(object.getVideos_list().get(0).getUrl());
            offset=offset+limit;
        loadFinished(true, code);
    }*/




    protected void loaddata(Uri uri) {
        loadStarted();
        feedUri = uri;

        Uri.Builder uriBuilder = feedUri.buildUpon();
        Log.d(TAG, "feedUri " + uri + "");

        uriBuilder.appendQueryParameter("page", page+ "");


        Log.d(TAG, "final uri " + uriBuilder.build().toString());
        Uri finaluri = uriBuilder.build();

        final Request request = new Request.Builder().url(finaluri.toString()).build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final int code = response.code();
                if (response.isSuccessful())
                {
                    String responseData = response.body().string();
                    Log.i(TAG, "onResponse: "+responseData);
                    Log.i(TAG, "response code: "+code);


                    JsonAdapter<Hits> jsonAdapter =  moshi.adapter(Hits.class);
                   final Hits hits=  jsonAdapter.fromJson(responseData);
                    Log.i(TAG, "onResponse: Size "+hits.getHits().size());

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            addHitItemData(hits);
                            loadFinished(true,code);
                        }
                    });
                }

            }
        });
    }






    public void loadmoreData() {

        if (!isDataLoading() && !noMoreItems) {
            addLoadingElement();
            page++;
            loaddata(feedUri);
        }

    }

    public void addLoadingElement(){
        Log.d("PaginatedManager","called addLoadingElement");
        feedItems.add((T) loader);
        dispatchItemInsertedCallbacks(feedItems.size());
    }
    public void removeLoadingElement(){
        Log.d("PaginatedManager","called removeLoadingElement");
        int pos = feedItems.size();
        if( !feedItems.isEmpty()&&feedItems.get(pos-1) instanceof Loader){
            feedItems.remove(pos-1);
            dispatchItemRemovedCallbacks(pos);
        }

    }


    public void addHitItemData(final Hits hits) {
        if (feedItems == null) {
            feedItems = new ArrayList();
        }
        int initSize = hits.getHits().size();


        feedItems.addAll((Collection<? extends T>) hits.getHits());


        dispatchItemRangeInsertedCallbacks(initSize+1,feedItems.size()-initSize);
    }


    public List<T> getList() {
        return feedItems;
    }


    @Override
    public synchronized void registerCallback(DataChangeNotifier.DataChangedCallbacks callback) {
        if (itemChangedCallbacks == null) {
            itemChangedCallbacks = new ArrayList<>(3);
        }
        itemChangedCallbacks.add(callback);
    }

    public void unregisterCallback(DataChangeNotifier.DataChangedCallbacks callback) {
        if (itemChangedCallbacks != null && itemChangedCallbacks.contains(callback)) {
            itemChangedCallbacks.remove(callback);
        }
    }

    protected synchronized void dispatchItemInsertedCallbacks(int position) {
        Log.d("PaginatedManager","dispatchItemInsertedCallbacks "+position);
        if (itemChangedCallbacks == null || itemChangedCallbacks.isEmpty()) return;
        List<DataChangeNotifier.DataChangedCallbacks> loadingCallbackstemp = new ArrayList(itemChangedCallbacks);
        for (DataChangedCallbacks loadingCallback : loadingCallbackstemp) {
            loadingCallback.itemInserted(position);
        }
    }

    protected synchronized void dispatchItemRangeInsertedCallbacks(int offset,int size) {
        Log.d("PaginatedManager","dispatchItemRangeInsertedCallbacks "+offset+ " >>> "+size);
        if (itemChangedCallbacks == null || itemChangedCallbacks.isEmpty()) return;
        List<DataChangeNotifier.DataChangedCallbacks> loadingCallbackstemp = new ArrayList(itemChangedCallbacks);
        for (DataChangedCallbacks loadingCallback : loadingCallbackstemp) {
            loadingCallback.itemRangeInserted(offset,size);
        }
    }

    protected synchronized void dispatchItemRemovedCallbacks(int position) {
        Log.d("PaginatedManager","dispatchItemRemovedCallbacks "+position);
        if (itemChangedCallbacks == null || itemChangedCallbacks.isEmpty()) return;
        List<DataChangeNotifier.DataChangedCallbacks> loadingCallbackstemp = new ArrayList(itemChangedCallbacks);
        for (DataChangedCallbacks loadingCallback : loadingCallbackstemp) {
            loadingCallback.itemRemoved(position);
        }
    }

    public static class Loader implements Serializable {
        String loadingText = null;

        public Loader(String loadingText) {
            this.loadingText = loadingText;
        }

        public String getLoadingText() {
            return loadingText;
        }

        public void setLoadingText(String loadingText) {
            this.loadingText = loadingText;
        }
    }

}
