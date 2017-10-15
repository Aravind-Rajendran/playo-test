package test.playo.com.playotest.datamanager;

public interface DataChangeNotifier {
    void registerCallback(DataChangedCallbacks callbacks);
    void unregisterCallback(DataChangedCallbacks callbacks);

    interface DataChangedCallbacks {
        void itemInserted(int position);
        void itemRangeInserted(int offset, int size);
        void itemRemoved(int position);
    }
}
