/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.playo.com.myapplication.datamanager;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Base class for loading data; extending types are responsible for providing implementations of
 * cancel any activity.
 */
public abstract class BaseDataManager<T> implements DataLoadingSubject {

    private final AtomicInteger loadingCount;

    private List<DataLoadingSubject.DataLoadingCallbacks> loadingCallbacks =
            Collections.synchronizedList(new ArrayList<DataLoadingSubject.DataLoadingCallbacks>());


    public BaseDataManager(@NonNull Context context) {
        loadingCount = new AtomicInteger(0);

    }


    @Override
    public boolean isDataLoading() {
        return loadingCount.get() > 0;
    }


    @Override
    public synchronized void registerCallback(DataLoadingSubject.DataLoadingCallbacks callback) {
        if (loadingCallbacks == null) {
            loadingCallbacks = new ArrayList<>(3);
        }
        loadingCallbacks.add(callback);
    }

    @Override
    public void unregisterCallback(DataLoadingSubject.DataLoadingCallbacks callback) {
        if (loadingCallbacks != null && loadingCallbacks.contains(callback)) {
            loadingCallbacks.remove(callback);
        }
    }

    protected synchronized void loadStarted() {
        if (0 == loadingCount.getAndIncrement()) {
            dispatchLoadingStartedCallbacks();
        }
    }

    protected synchronized void loadFinished(boolean isSuccess, int code) {
        if (0 == loadingCount.decrementAndGet()) {
            dispatchLoadingFinishedCallbacks(isSuccess, code);
        }
    }

    protected void resetLoadingCount() {
        loadingCount.set(0);
    }


    protected synchronized void dispatchLoadingStartedCallbacks() {
        if (loadingCallbacks == null || loadingCallbacks.isEmpty()) return;
        List<DataLoadingSubject.DataLoadingCallbacks> loadingCallbackstemp = new ArrayList<DataLoadingSubject.DataLoadingCallbacks>(loadingCallbacks);
        for (DataLoadingCallbacks loadingCallback : loadingCallbackstemp) {
            loadingCallback.dataStartedLoading();
        }
    }

    protected synchronized void dispatchLoadingFinishedCallbacks(boolean isSuccess, int code) {
        if (loadingCallbacks == null || loadingCallbacks.isEmpty()) return;
        List<DataLoadingSubject.DataLoadingCallbacks> loadingCallbackstemp = new ArrayList<DataLoadingSubject.DataLoadingCallbacks>(loadingCallbacks);
        for (DataLoadingCallbacks loadingCallback : loadingCallbackstemp) {
            loadingCallback.dataFinishedLoading(isSuccess, code);
        }
    }


}
