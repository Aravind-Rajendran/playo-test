


package test.playo.com.playotest;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import test.playo.com.playotest.http.OKHttpHelper;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String feedurl = "https://hn.algolia.com/api/v1/search";
        OkHttpClient okHttpClient = OKHttpHelper.getInstanceOf(MainActivity.this);

        Uri.Builder uriBuilder = Uri.parse(feedurl).buildUpon();
        uriBuilder.appendQueryParameter("query","sports");
        Uri uri = uriBuilder.build();

        final Request request = new Request.Builder().url(uri.toString()).build();

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
                    Log.i(TAG, "onResponse: "+response.body().string());
                    Log.i(TAG, "response code: "+code);
                }

            }
        });

    }
}
