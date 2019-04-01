package common;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import common.AppController;
import common.Common;


/**
 * Created by Ashish.Kumar on 02-02-2018.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    AppController controller;
    @Override
    public void onTokenRefresh() {
        controller=(AppController) getApplicationContext();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("Firebase", "token "+ FirebaseInstanceId.getInstance().getToken());
        registerToken(token);
    }


    private void registerToken(String token) {
        Common.fcmToken=token;
        controller.getManager().setFcmToken(token);

    }

}

