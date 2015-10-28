package com.mjj.materialdesignanim;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;

    private int[] images =
            {R.mipmap.test_a,
            R.mipmap.test_b,
            R.mipmap.test_c,
            R.mipmap.test_d,
            R.mipmap.test_e,
            R.mipmap.test_f};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        initView();
        setAdapter();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
    }

    private void setAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerAdapter adapter = new RecyclerAdapter(images,this);
        mRecyclerView.setAdapter(adapter);
        adapter.addOnItemClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("image",images[position]);
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP){
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, view.findViewById(R.id.imageview), "imageview");

            startActivity(intent, options.toBundle());
        }else{
            startActivity(intent);
        }
    }
}
