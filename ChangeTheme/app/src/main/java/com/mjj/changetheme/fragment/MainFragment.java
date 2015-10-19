package com.mjj.changetheme.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.mjj.changetheme.activity.MainActivity;
import com.mjj.changetheme.R;
import com.mjj.changetheme.activity.SecondActivity;
import com.mjj.changetheme.adapter.RecyclerAdapter;


public class MainFragment extends Fragment implements RecyclerAdapter.OnItemClickListener {

    private Context mContext;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    private boolean move = false;

    private OnRecyclerViewCreated mOnRecyclerViewCreated;
    //记录顶部显示的项
    private int position = 0;
    //记录顶部项的偏移
    private int scroll = 0;

    @Override
    public void onAttach(Context context) {
        mContext = context;
        mOnRecyclerViewCreated = (OnRecyclerViewCreated) context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_main_content, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        setActionBar(toolbar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        setAdapter();
        return view;
    }

    private void setActionBar(Toolbar toolbar) {
        ((MainActivity)mContext).setSupportActionBar(toolbar);
    }

    private void setAdapter(){
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        RecyclerAdapter adapter = new RecyclerAdapter();
        mRecyclerView.setAdapter(adapter);
        adapter.addOnItemClickListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerViewListener());

        position = getArguments().getInt("position");
        scroll = getArguments().getInt("scroll");
        move();

    }

    private void move(){
        if (position<0 || position>=mRecyclerView.getAdapter().getItemCount() ) {
            return;
        }
        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        if (position <= firstItem ){
            mRecyclerView.scrollToPosition(position);
            move = true;
        }else if ( position <= lastItem ){
            int top = mRecyclerView.getChildAt(position - firstItem).getTop() - scroll;
            mRecyclerView.scrollBy(0, top);
            mOnRecyclerViewCreated.recyclerViewCreated();
        }else{
            mRecyclerView.scrollToPosition(position);
            move = true;
        }
    }

    @Override
    public void onItemClick(View view) {
        Intent intent = new Intent(mContext, SecondActivity.class);
        startActivity(intent);
    }

    class RecyclerViewListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (move){
                move = false;
                int n = position - mLinearLayoutManager.findFirstVisibleItemPosition();
                if ( 0 <= n && n < mRecyclerView.getChildCount()){
                    int top = mRecyclerView.getChildAt(n).getTop() - scroll;
                    mRecyclerView.smoothScrollBy(0, top);
                }
                mOnRecyclerViewCreated.recyclerViewCreated();

            }
        }
    }

    public RecyclerView getRecyclerView(){
        return mRecyclerView;
    }

    public interface OnRecyclerViewCreated{
        void recyclerViewCreated();
    }
}
