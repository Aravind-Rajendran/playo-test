package test.playo.com.myapplication.datamanager;

import android.support.annotation.Nullable;
import android.util.Log;

import com.squareup.moshi.FromJson;


public class NullPrimitiveAdapter {
    private static final String TAG = "NullPrimitiveAdapter";

    @FromJson
    public int intFromJson(@Nullable Integer value) {
        if (value == null) {
            Log.i(TAG,"value is coming null");
            return -1;
        }
        Log.i(TAG,value+"");

        return value;
    }



    @FromJson
    public boolean booleanFromJson(@Nullable Boolean value) {
        if (value == null) {
            return false;
        }
        return value;
    }

    @FromJson
    public double doubleFromJson(@Nullable Double value) {
        if (value == null) {
            return -1;
        }
        return value;
    }
}
