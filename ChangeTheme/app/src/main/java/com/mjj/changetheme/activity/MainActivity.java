package com.mjj.changetheme.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mjj.changetheme.R;
import com.mjj.changetheme.app.MyAppliction;
import com.mjj.changetheme.data.Theme;
import com.mjj.changetheme.fragment.MainFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.OnRecyclerViewCreated {
    private ViewGroup mViewGroup;
    private ImageView mImageView;

    private MainFragment mMainFragment;

    private final long ANIMTION_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(MyAppliction.getThemeResources());
        setContentView(R.layout.activity_main);

        addFragment(0, 0);
        initView();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        save();
    }

    /**
     * 主题选择的本地存储
     */
    private void save() {
        SharedPreferences sharedPreferences = getSharedPreferences("theme",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("theme", MyAppliction.getThemeValue());
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id  = item.getItemId();
        switch (id){
            case R.id.action_changetheme:
                changeTheme();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView() {

        mViewGroup = (ViewGroup) findViewById(R.id.layout_fragment);
        mImageView = (ImageView) findViewById(R.id.imageview);
    }

    /**
     * 添加Fragment,如果已存在Fragment就先移除在添加
     * @param position
     * @param scroll
     */
    private void addFragment(int position,int scroll) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (mMainFragment != null){
            fragmentTransaction.remove(mMainFragment);
        }
        mMainFragment =  new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        bundle.putInt("scroll",scroll);
        mMainFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.layout_fragment, mMainFragment);
        fragmentTransaction.commit();
    }

    /**
     * 改变主题
     */
    private void changeTheme(){
        setDrawableCahe();
        setTheme();
        getState();

    }

    /**
     * 获取当前fragment状态，在Demo中简单演示了RecyclerView的位置恢复
     */
    public void getState() {

        RecyclerView recyclerView = mMainFragment.getRecyclerView();
        recyclerView.stopScroll();
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int position = layoutManager.findFirstVisibleItemPosition();
            int scroll = recyclerView.getChildAt(0).getTop();

            addFragment(position,scroll);
        }

    }

    /**
     * 获取布局的DrawableCahe给ImageView覆盖Fragment
     */
    private void setDrawableCahe() {
        //设置false清除缓存
        mViewGroup.setDrawingCacheEnabled(false);
        //设置true之后可以获取Bitmap
        mViewGroup.setDrawingCacheEnabled(true);
        mImageView.setImageBitmap(mViewGroup.getDrawingCache());
        mImageView.setAlpha(1f);
        mImageView.setVisibility(View.VISIBLE);
    }

    /**
     * 设置主题
     */
    private void setTheme() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.myTheme, typedValue, true);
        switch (typedValue.data){

            case Theme.DAYTHEME:
                MyAppliction.setThemeValue(Theme.NIGHTTHEME);
                setTheme(Theme.RESOURCES_NIGHTTHEME);
                break;
            case Theme.NIGHTTHEME:
                MyAppliction.setThemeValue(Theme.DAYTHEME);
                setTheme(Theme.RESOURCES_DAYTHEME);
                break;
        }
    }

    /**
     * Fragment状态恢复完毕的监听回调
     */
    @Override
    public void recyclerViewCreated() {
        startAnimation(mImageView);
    }

    /**
     * ImageView的动画
     * @param view
     */
    private void startAnimation(final View view) {
        ValueAnimator animator = ValueAnimator.ofFloat(1f).setDuration(ANIMTION_TIME);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float n = (float) animation.getAnimatedValue();
                view.setAlpha(1f - n);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mImageView.setVisibility(View.INVISIBLE);
            }
        });
        animator.start();
    }


}
