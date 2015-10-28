package com.mjj.materialdesignanim;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private OnItemClickListener mOnItemClickListener;

    private int[] images;

    private Context context;

    public RecyclerAdapter(int[] images,Context context){
        this.images = images;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main_recycle_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.title.setText(""+position);
        holder.content.setText("这是一行文字");

        Glide.with(context).load(images[position]).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = holder.getLayoutPosition();
                mOnItemClickListener.onItemClick(v,n);
            }
        });


    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView content;
        private ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_titlt);
            content = (TextView) itemView.findViewById(R.id.tv_content);
            imageView = (ImageView) itemView.findViewById(R.id.imageview);
        }
    }

    public void addOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }


}
