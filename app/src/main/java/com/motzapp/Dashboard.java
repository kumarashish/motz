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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import interfaces.WebApiResponseCallback;
import model.CasesModel;
import model.RegisterModel;
import utils.Utils;

public class Dashboard extends Activity implements View.OnClickListener, WebApiResponseCallback {
    @BindView(R.id.mail)
    View mail;
    @BindView(R.id.menu)
    ImageView menu;
    @BindView(R.id.notification)
    View notification;
    @BindView(R.id.create_case)
    Button fab;
    @BindView(R.id.notification_count)
    TextView notificationCount;
    @BindView(R.id.email_count)
    TextView emailCount;
    @BindView(R.id.total_cases_count)
    TextView totalCasesCount;

    @BindView(R.id.noCase)
            TextView noCase;
    ProgressDialog progressDialog;
    AppController controller;
    @BindView(R.id.listView1)
    ListView caseList;
    ArrayList<CasesModel>caselistItems=new ArrayList<>();
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
        caseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CaseDetails.model=caselistItems.get(position);
                startActivity(new Intent(Dashboard.this,CaseDetails.class));
            }
        });
    }
public void getData(){
    if (Utils.isNetworkAvailable(Dashboard.this)) {
        progressDialog.show();
        caselistItems.clear();
        controller.getWebApiCall().getDataCommonMethod(Common.totalCaseUrl,controller.getManager().getUserToken(),Dashboard.this);
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
                        controller.logout();
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

    public void setCases(final String value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject jsonObject = new JSONObject(value);
                    JSONArray caseList = jsonObject.getJSONArray("case_details");
                    for (int i = 0; i < caseList.length(); i++) {
                        CasesModel model = new CasesModel(caseList.getJSONObject(i));
                        caselistItems.add(model);
                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
                totalCasesCount.setText(Integer.toString(caselistItems.size()));
                if (caselistItems.size() > 0) {
                    caseList.setVisibility(View.VISIBLE);
                    noCase.setVisibility(View.GONE);
                    caseList.setAdapter(new Adapter(caselistItems, Dashboard.this));
                } else {
                    caseList.setVisibility(View.GONE);
                    noCase.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    @Override
    public void onSucess(String value) {
        if (Utils.getStatus(value) == true) {

            setCases(value);

        } else {
            Utils.showToast(Dashboard.this, Utils.getMessage(value));
        }
        Utils.cancelProgressDialog(Dashboard.this,progressDialog);
    }

    @Override
    public void onError(String value) {
        Utils.showToast(Dashboard.this,Utils.getMessage(value));
        Utils.cancelProgressDialog(Dashboard.this,progressDialog);
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