package com.mjj.recyclerviewlocation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private OnItemClickListener mOnItemClickListener;
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main_recycle_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.title.setText(""+position);
        holder.content.setText("点击这一项置顶");

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
        return 100;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView content;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_titlt);
            content = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    public void addOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }


}
