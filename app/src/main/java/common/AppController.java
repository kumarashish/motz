package common;

import android.app.Application;


import com.google.gson.Gson;

import model.RegisterModel;
import utils.WebApiCall;

public class    AppController extends Application {
    AppController controller;
    WebApiCall webApiCall;
    PrefManager manager;
    RegisterModel profile;
//    BusinessProfile businessProfile;
    @Override
    public void onCreate() {

        controller=this;
        webApiCall=new WebApiCall(getApplicationContext());
        manager=new PrefManager(getApplicationContext());
        if(manager.getUserProfile().length()>0)
        {
            if (manager.getUserProfile().length() > 0) {
                Gson gson = new Gson();
                profile = gson.fromJson(manager.getUserProfile(), RegisterModel.class);

            }
        }else {
            profile=new RegisterModel();

        }
        super.onCreate();
    }

//
    public void setProfile(RegisterModel profile) {
        this.profile = profile;
        Gson gson = new Gson();
        String userProfileString = gson.toJson(profile);
        manager.setUserProfile(userProfileString);

    }
//    public void setBusinessProfile(BusinessProfile profile) {
//        this.businessProfile = profile;
//        Gson gson = new Gson();
//        String userProfileString = gson.toJson(profile);
//        manager.setBusinessProfile(userProfileString);
//
//    }
    public AppController getController() {
        return controller;
    }

    public WebApiCall getWebApiCall() {
        return webApiCall;
    }

    public PrefManager getManager() {
        return manager;
    }
//
//    public BusinessProfile getBusinessProfile() {
//        return businessProfile;
//    }
//
    public RegisterModel getProfile() {
        return profile;
    }

    public void logout()
    {
        manager.setUserToken("");
        manager.setUserProfile("");
        manager.setUserLoggedIn(false);

    }
}
