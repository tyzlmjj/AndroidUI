package me.majiajie.flexbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void toLogin(View v)
    {
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }

    public void toFlow(View v)
    {
        startActivity(new Intent(MainActivity.this,FlowLayoutActivity.class));
    }

    public void toItem(View v)
    {
        startActivity(new Intent(MainActivity.this,ItemActivity.class));
    }
}
