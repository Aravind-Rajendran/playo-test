package test.playo.com.playotest;

import android.app.Activity;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import test.playo.com.playotest.datamanager.DataChangeNotifier;
import test.playo.com.playotest.datamanager.HitItemDataManager;
import test.playo.com.playotest.model.Hits;


public class HitItemListAdapter<T> extends RecyclerView.Adapter implements  DataChangeNotifier.DataChangedCallbacks{
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
        this.hitItemDataManager.registerCallback(this);

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

        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Hits.Item item = hitItemList.get(viewHolder.getAdapterPosition());
                if (item.getUrl()!=null&& item.getUrl().contains("http"))
                {
                    String url = item.getUrl();
                    Uri uri = Uri.parse(url);
                    openUri(uri);
                }

            }
        });


        return viewHolder;
    }





    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder customViewHolder, int i) {
        Log.i(TAG, "onBindViewHolder: Position"+i);





        Object saleItem  = hitItemList.get(i);

        if (getItemViewType(i)==ITEM)
        {
            Hits.Item v= (Hits.Item)saleItem;
            onBindViewHolder((ItemViewHolder) customViewHolder,v);

        }
        if (getItemViewType(i)==LOAD_MORE)
        {
            onBindLoadingElement((LoadingMoreHolder)customViewHolder,i);
        }



    }

    public void onBindViewHolder(ItemViewHolder viewHolder, final Hits.Item item)


    {
        viewHolder.title.setText(item.getTitle());
        viewHolder.author.setText(item.getAuthor());
        viewHolder.points.setText(""+item.getPoints());
        if (!item.get_tags().isEmpty()) {
            TagCarouselAdapter tagCarouselAdapter = new TagCarouselAdapter(mContext, item.get_tags());
            viewHolder.recyclerView_tag.setAdapter(tagCarouselAdapter);
            viewHolder.recyclerView_tag.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.recyclerView_tag.setVisibility(View.GONE);
        }


    }







    @Override
    public int getItemCount() {
        return (null != hitItemList ? hitItemList.size() : 0);
    }

    @Override
    public void itemInserted( final int position) {
        if(mainActivity != null)
            ((MainActivity)mainActivity).recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    notifyItemInserted(position);
                }
            });
    }

    @Override
    public void itemRangeInserted(final  int offset,final int size) {
        ((MainActivity)mainActivity).recyclerView.post(new Runnable() {
            @Override
            public void run() {
                notifyItemRangeInserted(offset,size);
            }
        });

    }

    @Override
    public void itemRemoved(final  int position) {
        if(mainActivity != null)
            ((MainActivity)mainActivity).recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,hitItemList.size());
                }
            },300);

    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView title,author,points;
        protected RecyclerView recyclerView_tag;
        protected LinearLayout container;






        protected int type;
        public ItemViewHolder(View view) {
            super(view);

            this.title= (TextView)view.findViewById(R.id.title);
            this.recyclerView_tag= (RecyclerView)view.findViewById(R.id.recycler_view_tag);
            this.author= (TextView)view.findViewById(R.id.author);
            this.points= (TextView)view.findViewById(R.id.points);
            this.container=(LinearLayout)view.findViewById(R.id.container);








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

    private void openUri(Uri uri)
    {

        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

// Begin customizing
// set toolbar colors
        intentBuilder.setToolbarColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));

// set start and exit animations


// build custom tabs intent
        CustomTabsIntent customTabsIntent = intentBuilder.build();

// launch the url
        customTabsIntent.launchUrl(mContext, uri);
    }






}


