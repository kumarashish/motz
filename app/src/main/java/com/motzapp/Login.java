package com.motzapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Login  extends Activity implements View.OnClickListener {
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.signUp)
    TextView signUp;
    @BindView(R.id.facebook)
    Button fb;
    @BindView(R.id.google)
    Button google;

    @BindView(R.id.forgetpassword)
    TextView forgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) && (Build.VERSION.SDK_INT < 26)) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else if (Build.VERSION.SDK_INT >= 26) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        }
        ButterKnife.bind(this);
        fb.setOnClickListener(this);
        google.setOnClickListener(this);
        submit.setOnClickListener(this);
        signUp.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.facebook:
                Toast.makeText(Login.this, "Logged in Sucessfully", Toast.LENGTH_SHORT).show();
                break;
            case R.id.google:
                Toast.makeText(Login.this, "Logged in Sucessfully", Toast.LENGTH_SHORT).show();
                break;
            case R.id.submit:
                startActivity(new Intent(Login.this,Dashboard.class));
                finish();
                break;
            case R.id.signUp:
                startActivity(new Intent(Login.this,Register.class));
                break;
            case R.id.forgetpassword:
                startActivity(new Intent(Login.this,ForgetPassword.class));

                break;
        }

    }
}
