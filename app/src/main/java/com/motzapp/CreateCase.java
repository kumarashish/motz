package com.motzapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;

public class CreateCase  extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.case_row);

        ButterKnife.bind(this);

    }

    @Override
    public void onClick(View v) {
    }

}