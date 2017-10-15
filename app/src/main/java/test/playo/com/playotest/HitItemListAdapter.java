package test.playo.com.playotest;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import test.playo.com.playotest.datamanager.HitItemDataManager;
import test.playo.com.playotest.model.Hits;


public class HitItemListAdapter<T> extends RecyclerView.Adapter{
    private List<Hits.Item> hitItemList;
    private Activity mContext;

    public static int ITEM = 1;
    public static int LOAD_MORE = 2;

    private static final String TAG = "VideoListAdapter";


    boolean isLoading = false, isMoreDataAvailable = true;
  ;
    int millis =-1;

    FrameLayout framelayout;
    LayoutInflater inflater;


    boolean isFirstPlay = false;

    TextView duration;
    FrameLayout image_layout;
    String loadid=null;
    MainActivity mainActivity;
    HitItemDataManager hitItemDataManager;







    public HitItemListAdapter(Activity context, List<Hits.Item> hitItemList , MainActivity mainActivity, HitItemDataManager hitItemDataManager) {
        this.hitItemList = hitItemList;
        this.mContext = context;

        this.mainActivity=mainActivity;
        inflater = LayoutInflater.from(context);

        this.hitItemDataManager=hitItemDataManager;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        if(viewType==ITEM) {
            return oncreateItemViewHolder(viewGroup);
        }


        if (viewType==LOAD_MORE)
            return new HitItemListAdapter.LoadingMoreHolder(
                    inflater.inflate(R.layout.infinite_loading, viewGroup, false));



        return null;

    }

    private void onBindLoadingElement(LoadingMoreHolder holder, int position) {
        // only show the infinite load progress spinner if there are already items in the
        // grid i.e. it's not the first item & data is being loaded
        holder.progress.setVisibility((position > 0 &&hitItemDataManager .isDataLoading())
                ? View.VISIBLE : View.INVISIBLE);
    }







    public ItemViewHolder oncreateItemViewHolder(ViewGroup viewGroup) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup,false);
        final ItemViewHolder viewHolder = new ItemViewHolder(view);


        return viewHolder;
    }





    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder customViewHolder, int i) {
        Log.i(TAG, "onBindViewHolder: Position"+i);





        Object saleItem  = hitItemList.get(i);

        if (getItemViewType(i)==ITEM)
        {
            Hits.Item v= (Hits.Item)saleItem;
            onBindProductViewHolder((ItemViewHolder) customViewHolder,v);

        }
        if (getItemViewType(i)==LOAD_MORE)
        {
            onBindLoadingElement((LoadingMoreHolder)customViewHolder,i);
        }



    }

    public void onBindProductViewHolder(ItemViewHolder viewHolder, final Hits.Item item)


    {
        viewHolder.title.setText(item.getTitle());
        viewHolder.author.setText(item.getAuthor());
        viewHolder.points.setText(""+item.getPoints());


    }







    @Override
    public int getItemCount() {
        return (null != hitItemList ? hitItemList.size() : 0);
    }




    public class ItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView title,author,points;
        protected RecyclerView recyclerView_tag;






        protected int type;
        public ItemViewHolder(View view) {
            super(view);

            this.title= (TextView)view.findViewById(R.id.title);
            this.recyclerView_tag= (RecyclerView)view.findViewById(R.id.recycler_view_tag);
            this.author= (TextView)view.findViewById(R.id.author);
            this.points= (TextView)view.findViewById(R.id.points);






        }
    }

    public static class LoadingMoreHolder extends RecyclerView.ViewHolder {

        public ProgressBar progress;

        public LoadingMoreHolder(View itemView) {
            super(itemView);
            progress = (ProgressBar) itemView;
        }

    }




    public int getDataItemCount() {
        if (hitItemList == null)
            return 0;
        return hitItemList.size();
    }








    @Override
    public int getItemViewType(int position) {



        Object listItem = hitItemList.get(position);
        Log.e(TAG,"getItemViewType"+position);
        if (listItem instanceof Hits.Item) {
            return ITEM;
        }

        else if (listItem instanceof LoadingMoreHolder)
        {
            Log.e(TAG,"returning -1");
            return LOAD_MORE;
        }


        Log.e(TAG,"returning -i outsisde");
        return LOAD_MORE ;


    }




}


