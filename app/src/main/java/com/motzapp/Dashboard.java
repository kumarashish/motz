package com.motzapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Dashboard extends Activity implements View.OnClickListener {
    @BindView(R.id.mail)
    View mail;
    @BindView(R.id.menu)
    ImageView menu;
    @BindView(R.id.notification)
    View notification;
    @BindView(R.id.create_case)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        ButterKnife.bind(this);
        notification.setOnClickListener(this);
        fab.setOnClickListener(this);
        menu.setOnClickListener(this);
        mail.setOnClickListener(this);

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
                startActivity(new Intent(Dashboard.this,CreateCase.class));
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

    }