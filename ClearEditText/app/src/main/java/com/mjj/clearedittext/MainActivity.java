package com.mjj.clearedittext;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private ClearEditText passwordEditText;
    private ClearEditText passwordEditTextB;
    private TextInputLayout mTextInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        passwordEditText = (ClearEditText) findViewById(R.id.edt_password);
        passwordEditTextB = (ClearEditText) findViewById(R.id.edt_passwordB);
        mTextInputLayout = (TextInputLayout) findViewById(R.id.layout);
    }

    public void loginA(View v){
        passwordEditText.startShakeAnimation();
        passwordEditText.setError("输入的密码错误");
    }

    public void loginB(View v){
        passwordEditTextB.startShakeAnimation();
        mTextInputLayout.setError("输入的密码错误");
    }

}
