package test.playo.com.playotest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;




public class TagCarouselAdapter extends RecyclerView.Adapter<TagCarouselAdapter.CustomViewHolder> {

    private List<String> tags;
    private Context context;
    private int dp = 0;
    private String source = "";
    private static int productWidth = 0, productHeight = 0;
    private  boolean isFromVoonikTv =false;

    public TagCarouselAdapter(Context context, List<String> tags) {
        this.tags = tags;
        this.context = context;
        TagCarouselAdapter.productWidth = 0;
        TagCarouselAdapter.productHeight = 0;
    }


    public boolean isFromVoonikTv() {
        return isFromVoonikTv;
    }

    public void setFromVoonikTv(boolean fromVoonikTv) {
        isFromVoonikTv = fromVoonikTv;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final CustomViewHolder holder = new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_tag, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        bindItem(holder, position);
    }

    private void bindItem(final CustomViewHolder holder, final int position) {


            String name  = tags.get(position);

            holder.tag_name.setText(name);


    }

    @Override
    public int getItemCount() {
        return tags != null ? tags.size() : 0;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {


        TextView tag_name;

        public CustomViewHolder(View itemView) {
            super(itemView);

            tag_name = (TextView) itemView.findViewById(R.id.tag_title);

        }
    }

}
