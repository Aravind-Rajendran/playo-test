package test.playo.com.playotest.datamanager;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class InfiniteScrollListener extends RecyclerView.OnScrollListener {

    // The minimum number of items remaining before we should loading more.
    private int VISIBLE_THRESHOLD = 8;

    private final LinearLayoutManager layoutManager;
    private final DataLoadingSubject dataLoading;

    public InfiniteScrollListener(@NonNull LinearLayoutManager layoutManager,
                                  @NonNull DataLoadingSubject dataLoading) {
        this.layoutManager = layoutManager;
        this.dataLoading = dataLoading;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        // bail out if scrolling upward or already loading data
        if (dy < 0 || dataLoading.isDataLoading()) return;

        final int visibleItemCount = recyclerView.getChildCount();
        final int totalItemCount = layoutManager.getItemCount();
        final int firstVisibleItem = layoutManager.findLastVisibleItemPosition();

        if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
            onLoadMore();
        }
    }

    public abstract void onLoadMore();

    public void setVISIBLE_THRESHOLD(int VISIBLE_THRESHOLD) {
        this.VISIBLE_THRESHOLD = VISIBLE_THRESHOLD;
    }
}