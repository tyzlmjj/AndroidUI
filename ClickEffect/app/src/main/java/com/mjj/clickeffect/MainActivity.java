package com.mjj.clickeffect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toXML(View v){

        Intent intent = new Intent(this, ButtonActivity.class);
        startActivity(intent);

    }

    public void toMaterialButton(View v){

        Intent intent = new Intent(this, MaterialButtonActivity.class);
        startActivity(intent);

    }

    public void toList(View v){

        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);

    }

    public void toImageView(View v){

        Intent intent = new Intent(this, ImageViewActivity.class);
        startActivity(intent);

    }

    public void toStateListAnim(View v){

        Intent intent = new Intent(this, StateListAnimActivity.class);
        startActivity(intent);
    }
}
