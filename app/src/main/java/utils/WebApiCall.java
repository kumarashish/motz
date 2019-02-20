package utils;

import android.content.Context;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import interfaces.WebApiResponseCallback;

import model.RegisterModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Ashish.Kumar on 23-01-2018.
 */

public class WebApiCall {
    OkHttpClient client;
    private  OkHttpClient.Builder client1;
    public static final MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    Context context;
    static OkHttpClient clientForMP;

    public WebApiCall(Context context) {
        client = new OkHttpClient();
        this.context = context;
    }

    private int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }
    public void getData(String url, final WebApiResponseCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS).build();
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.fillInStackTrace().toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    if (response != null) {
                        callback.onSucess(response.body().string());
                    } else {
                        if (response.message() != null) {
                            callback.onError(response.message());
                        } else {
                            callback.onError("No data found!");
                        }

                    }
                }else{
                    callback.onError(response.body().string());
                }
            }
        });
    }
    public void getData(String url,String token, final WebApiResponseCallback callback) {

        HttpUrl httpUrl = HttpUrl.parse(url).newBuilder()
                .addEncodedQueryParameter("code",token)

                .build();
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS).build();
        final Request request = new Request.Builder().url(httpUrl).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.fillInStackTrace().toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    if (response != null) {
                        callback.onSucess(response.body().string());
                    } else {
                        if (response.message() != null) {
                            callback.onError(response.message());
                        } else {
                            callback.onError("No data found!");
                        }

                    }
                }else{
                    callback.onError(response.body().string());
                }
            }
        });
    }

    public String getData(String url) {
        OkHttpClient client = new OkHttpClient();
        client.newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS).readTimeout(60000, TimeUnit.MILLISECONDS).build();
        final Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                return response.body().string();

            } else {
                return getErrorData();
            }

        } catch (Exception ex) {
            ex.fillInStackTrace();
            return getErrorData();
        }

    }

    public String getErrorData() {
        JSONObject object = new JSONObject();
        try {
            object.put("Status", false);
            object.put("Message", "Error occured");
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return object.toString();
    }
    public void forgetPassword(String url,String Email ,final WebApiResponseCallback callback) {
        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        RequestBody formBody = null;

        formBody = new FormBody.Builder()
                .add("email", Email)
                .build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(formBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.fillInStackTrace().toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    if (response != null) {
                        callback.onSucess(response.body().string());
                    } else {
                        if (response.message() != null) {
                            callback.onError(response.message());
                        } else {
                            callback.onError("No data found!");
                        }

                    }
                }
            }
        });
    }

    public void loginWithFb(String url, String fb_Id, String email, String userName, String deviceId, String accessToken, final WebApiResponseCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build();
        RequestBody formBody = null;
        formBody = new FormBody.Builder()
                .add("fb_id", fb_Id)
                .add("email", email)
                .add("username", userName)
                .add("device_id", deviceId)
                .add("access_token", accessToken)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                callback.onError(e.fillInStackTrace().toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200 || response.code() == 201) {
                    if (response != null) {
                        callback.onSucess(response.body().string());
                    } else {
                        callback.onError(response.message());
                    }
                } else {
                    callback.onError(response.message());
                }
            }
        });
    }

    public void register(String url, RegisterModel model, final WebApiResponseCallback callback) {

        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS).build();
        RequestBody formBody = null;



        formBody = new FormBody.Builder()
                .add("email", model.getEmailId())
                .add("password", model.getPassword())
                .add("first_name", model.getFname())
                .add("last_name", model.getLname())
                .add("mobile", model.getMobile())

                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                callback.onError(e.fillInStackTrace().toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200 || response.code() == 201) {
                    if (response != null) {
                        callback.onSucess(response.body().string());
                    } else {
                        callback.onError(response.message());
                    }
                } else {
                    callback.onError(response.message());
                }
            }
        });
    }
    public void login(String url, String email, String password, final WebApiResponseCallback callback) {

        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build();
        RequestBody formBody = null;

        formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .add("device_id","androidTest")
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                callback.onError(e.fillInStackTrace().toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200 || response.code() == 201) {
                    if (response != null) {
                        callback.onSucess(response.body().string());
                    } else {
                        callback.onError(response.message());
                    }
                } else {
                    callback.onError(response.message());
                }
            }
        });
    }


public void postFormData(String url,String key,String userId ,final WebApiResponseCallback callback)
{
    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).build();
    RequestBody formBody = null;
    formBody = new FormBody.Builder()
            .add(key, userId)
            .build();
    Request request = new Request.Builder().url(url).post(formBody).build();
    client.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

            callback.onError(e.fillInStackTrace().toString());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.code() == 200 || response.code() == 201) {
                if (response != null) {
                    callback.onSucess(response.body().string());
                } else {
                    callback.onError(response.message());
                }
            } else {
                callback.onError(response.message());
            }
        }
    });
}
//
    public String postData(String url,String userId,String acesstoken,final WebApiResponseCallback callback)
    {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build();
        RequestBody formBody = null;
        formBody = new FormBody.Builder()
                .add("user_id", userId)
                .build();
        Request request = new Request.Builder().header("auth_token",acesstoken).url(url).post(formBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                callback.onError(e.fillInStackTrace().toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200 || response.code() == 201) {
                    if (response != null) {
                        callback.onSucess(response.body().string());
                    } else {
                        callback.onError(response.message());
                    }
                } else {
                    callback.onError(response.message());
                }
            }
        });
              return "";
    }

    public String createCase(String url,String userId,String category,String title,String description,String acesstoken,final WebApiResponseCallback callback)
    {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build();
        RequestBody formBody = null;
        formBody = new FormBody.Builder()
                .add("user_id", userId)
                .add("category", category)
                .add("title",title)
                .add("description", description)
                .build();
        Request request = new Request.Builder().header("auth_token",acesstoken).url(url).post(formBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                callback.onError(e.fillInStackTrace().toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200 || response.code() == 201) {
                    if (response != null) {
                        callback.onSucess(response.body().string());
                    } else {
                        callback.onError(response.message());
                    }
                } else {
                    callback.onError(response.message());
                }
            }
        });
        return "";
    }
//    public void register(String url, RegistrationModel model, final WebApiResponseCallback callback) {
//
//        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
//                .writeTimeout(60, TimeUnit.SECONDS)
//                .readTimeout(60, TimeUnit.SECONDS).build();
//        RequestBody formBody = null;
//
//
//
//        formBody = new FormBody.Builder()
//                .add("email", model.getEmail())
//                .add("password", model.getPassword())
//                .add("first_name", model.getFirst_name())
//                .add("last_name", model.getLast_name())
//                .add("salutation", model.getSalutation())
//                .add("category_id", model.getCategory_id())
//                .add("company_name", model.getCompany_name())
//                .add("street_name", model.getStreet_name())
//                .add("door_no", model.getDoor_no())
//                .add("city", model.getCity())
//
//                .add("zip_code", model.getZip_code())
//                .add("phone_number", model.getPhoneNumber())
//                .add("business_email", model.getBusiness_email())
//                .add("paypal_email", model.getPaypal_email())
//                .add("vat_id", model.getVat_id())
//                .build();
//        Request request = new Request.Builder().url(url).post(formBody).build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//                callback.onError(e.fillInStackTrace().toString());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.code() == 200 || response.code() == 201) {
//                    if (response != null) {
//                        callback.onSucess(response.body().string());
//                    } else {
//                        callback.onError(response.message());
//                    }
//                } else {
//                    callback.onError(response.message());
//                }
//            }
//        });
//    }
//    public void postData(String url, String json,final WebApiResponseCallback callback) {
//        client.newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS).readTimeout(60000, TimeUnit.MILLISECONDS).build();
//        RequestBody reqBody = RequestBody.create(JSON, json);
//        Request request = new Request.Builder().url(url).post(reqBody).build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//                callback.onError(e.fillInStackTrace().toString());
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//               // cancelProgressDialog(pd);
//                if (response.code() == 200 || response.code() == 201) {
//                    if (response != null) {
//                        callback.onSucess(response.body().string());
//                    } else {
//                        callback.onError(response.message());
//                    }
//                } else {
//                    callback.onError(response.message());
//                }
//            }
//        });
//    }
//    public String postData(String url, String json) {
//        client.newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS).readTimeout(60000, TimeUnit.MILLISECONDS).build();
//        RequestBody reqBody = RequestBody.create(JSON, json);
//        Request request = new Request.Builder().url(url).post(reqBody).build();
//        try {
//            Response response = client.newCall(request).execute();
//            if (response.code() == 200) {
//                return response.body().string();
//
//            } else {
//                return getErrorData();
//            }
//
//        } catch (Exception ex) {
//            ex.fillInStackTrace();
//            return getErrorData();
//        }
//
//    }

    }

