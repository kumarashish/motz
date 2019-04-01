package common;

public class Common {
    public static String fcmToken="";
    public static String baseUrl="https://api.motz-app.com";
   // public static String baseUrl="http://apimotz.umadevcenter.de";

    public static String registerUser=baseUrl+"/motzusers/register";
    public static String login=baseUrl+"/motzusers/login";
    public static String getCategoryUrl=baseUrl+"/categories";
    public static String createCase=baseUrl+"/cases/create_case";
    public static String forgetPasswordUrl=baseUrl+"/motzusers/forgot_password";
    public static String totalCaseUrl=baseUrl+"/cases";
    public static String fbLoginUrl=baseUrl+"/motzusers/login_with_facebook";
    public static String googleLoginUrl=baseUrl+"/motzusers/login_with_google";
    public static String updateProfileUrl=baseUrl+"/motzusers/update_my_details";
    public static String updatePasswordUrl=baseUrl+"/motzusers/update_my_password";
    public static String isUserExistUrl=baseUrl+"/motzusers/check_username_exists";


    public static String updatePasswordKeys[]={"old_password","new_password"};
    public static String updateProfileKeys[]={"first_name","last_name","phone"};
   public static String emaiId[]={"email"};






}
