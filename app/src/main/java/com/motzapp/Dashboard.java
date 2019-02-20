package com.motzapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.UndeclaredThrowableException;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import interfaces.WebApiResponseCallback;
import utils.Utils;

public class Dashboard extends Activity implements View.OnClickListener, WebApiResponseCallback {
    @BindView(R.id.mail)
    View mail;
    @BindView(R.id.menu)
    ImageView menu;
    @BindView(R.id.notification)
    View notification;
    @BindView(R.id.create_case)
    FloatingActionButton fab;
    @BindView(R.id.notification_count)
    TextView notificationCount;
    @BindView(R.id.email_count)
    TextView emailCount;
    @BindView(R.id.total_cases_count)
    TextView totalCasesCount;
    @BindView(R.id.caeslist)
            View caseList;
    @BindView(R.id.noCase)
            TextView noCase;
    ProgressDialog progressDialog;
    AppController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        progressDialog=new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        ButterKnife.bind(this);
        controller=(AppController)getApplicationContext();
        notification.setOnClickListener(this);
        fab.setOnClickListener(this);
        menu.setOnClickListener(this);
        mail.setOnClickListener(this);
        getData();
    }
public void getData(){
    if (Utils.isNetworkAvailable(Dashboard.this)) {
        progressDialog.show();
        controller.getWebApiCall().postData(Common.totalCaseUrl,controller.getProfile().getUserId(),controller.getManager().getUserToken(),Dashboard.this);
    }
}
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.notification:
                startActivity(new Intent(Dashboard.this,Notifications.class));
                break;
            case R.id.mail:
                startActivity(new Intent(Dashboard.this,Inbox.class));
                break;
            case R.id.menu:
                popupMenu();
                break;
            case R.id.create_case:
                startActivityForResult(new Intent(Dashboard.this,CreateCase.class),2);
                break;
        }
    }


    public void popupMenu()
    {
        PopupMenu popup = new PopupMenu(Dashboard.this,menu);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.logout:
                        startActivity(new Intent(Dashboard.this,Login.class));
                        Toast.makeText(Dashboard.this, "Logged out Sucessfully", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case R.id.profile_update:
                        startActivity(new Intent(Dashboard.this,Profile.class));

                        break;
                    case R.id.settings:
                        startActivity(new Intent(Dashboard.this,Settings.class));

                        break;
                }

                return true;
            }
        });
        popup.show();
    }
public void setCases(final String value)
{runOnUiThread(new Runnable() {
    @Override
    public void run() {
        totalCasesCount.setText(value);
        if(Integer.parseInt(value)>0)
        {   noCase.setVisibility(View.VISIBLE);
            noCase.setText("You have "+value+ " case but case details not available in total_cases  api");
        }else{
            caseList.setVisibility(View.GONE);
            noCase.setVisibility(View.VISIBLE);
        }
    }
});

}
    @Override
    public void onSucess(String value) {
        if (Utils.getStatus(value) == true) {
            setCases(Utils.getTotalCasesCount(value));

        } else {
            Utils.showToast(Dashboard.this, Utils.getMessage(value));
        }
        progressDialog.cancel();
    }

    @Override
    public void onError(String value) {
        Utils.showToast(Dashboard.this,Utils.getMessage(value));
        progressDialog.cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                boolean result=data.getBooleanExtra("result",false);
                if(result==true)
                {
                     getData();
                }
            }
            }
    }
}