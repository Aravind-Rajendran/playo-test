package test.playo.com.playotest.datamanager;

public interface DataLoadingSubject {
    boolean isDataLoading();
    void registerCallback(DataLoadingCallbacks callbacks);
    void unregisterCallback(DataLoadingCallbacks callbacks);

    public interface DataLoadingCallbacks {
        void dataStartedLoading();
        void dataFinishedLoading(boolean isSuccess, int code);
    }
}