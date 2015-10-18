package com.mjj.recyclerviewlocation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private RadioGroup mRadioGroup;
    private EditText mEditText;

    private boolean move = false;

    private int mIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setAdapter();
        initEvent();

    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        mEditText = (EditText) findViewById(R.id.editText);
    }

    private void setAdapter() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerAdapter();
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initEvent() {
        mAdapter.addOnItemClickListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerViewListener());
    }


    @Override
    public void onItemClick(View view,int position) {
        move(position);
    }

    public void go(View view){
        String str = mEditText.getText().toString();
        if (str.equals("")){
            Toast.makeText(this,"输入数字",Toast.LENGTH_SHORT).show();
            return;
        }
        int n = Integer.valueOf(str);
        move(n);
    }

    private void move(int n){
        if (n<0 || n>=mAdapter.getItemCount() ){
            Toast.makeText(this,"超出范围了",Toast.LENGTH_SHORT).show();
            return;
        }
        mIndex = n;
        mRecyclerView.stopScroll();
        switch (mRadioGroup.getCheckedRadioButtonId()){
            case R.id.scroll:
                moveToPosition(n);
                break;
            case R.id.smoothScroll:
                smoothMoveToPosition(n);
                break;
        }
    }

    private void smoothMoveToPosition(int n) {

        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        if (n <= firstItem ){
            mRecyclerView.smoothScrollToPosition(n);
        }else if ( n <= lastItem ){
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.smoothScrollBy(0, top);
        }else{
            mRecyclerView.smoothScrollToPosition(n);
            move = true;
        }

    }

    private void moveToPosition(int n) {

        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        if (n <= firstItem ){
            mRecyclerView.scrollToPosition(n);
        }else if ( n <= lastItem ){
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        }else{
            mRecyclerView.scrollToPosition(n);
            move = true;
        }

    }

    class RecyclerViewListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (move && newState == RecyclerView.SCROLL_STATE_IDLE && mRadioGroup.getCheckedRadioButtonId() == R.id.smoothScroll){
                move = false;
                int n = mIndex - mLinearLayoutManager.findFirstVisibleItemPosition();
                if ( 0 <= n && n < mRecyclerView.getChildCount()){
                    int top = mRecyclerView.getChildAt(n).getTop();
                    mRecyclerView.smoothScrollBy(0, top);
                }

            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (move && mRadioGroup.getCheckedRadioButtonId() == R.id.scroll){
                move = false;
                int n = mIndex - mLinearLayoutManager.findFirstVisibleItemPosition();
                if ( 0 <= n && n < mRecyclerView.getChildCount()){
                    int top = mRecyclerView.getChildAt(n).getTop();
                    mRecyclerView.scrollBy(0, top);
                }
            }
        }
    }

}
