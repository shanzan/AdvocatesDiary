package app.diary.advocates.advocatesdiary;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by khans on 3/28/2018.
 */

public class SharedPrefLoginInformation {
    private static SharedPrefLoginInformation mInstance;
    private static Context mCtx;

    private static final String SHARED_PREFER_NAME="myshareduserdata";
    private static final String KEY_USER_NAME="username";
    private static final String KEY_USER_EMAIL="useremail";
    private static final String KEY_USER_PHONE="usernumber";
    private static final String KEY_USER_ID="userid";
    private static final String KEY_SUB_USER_ID="subuserid";

    private SharedPrefLoginInformation(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefLoginInformation getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefLoginInformation(context);
        }
        return mInstance;
    }

    public boolean userLogin(int id,String username,String email,String phone){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREFER_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putInt(KEY_USER_ID,id);
        editor.putString(KEY_USER_EMAIL,email);
        editor.putString(KEY_USER_NAME,username);
        editor.putString(KEY_USER_PHONE,phone);

        editor.apply();
        return true;
    }
    public boolean subUserLogin(int id,String username,String email,String phone,String user_id){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREFER_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putInt(KEY_USER_ID,id);
        editor.putString(KEY_USER_EMAIL,email);
        editor.putString(KEY_USER_NAME,username);
        editor.putString(KEY_USER_PHONE,phone);
        editor.putString(KEY_SUB_USER_ID,user_id);

        editor.apply();
        return true;
    }
    public boolean checkUsertype(){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREFER_NAME,Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_SUB_USER_ID,null)!=null){
            return true;
        }
        return false;
    }

    public boolean isLoggedIN(){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREFER_NAME,Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_USER_EMAIL,null)!=null){
            return true;
        }
        return false;
    }

    public boolean logout(){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREFER_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public String getUserName(){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREFER_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_NAME,null);
    }
    public String getUserEmail(){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREFER_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_EMAIL,null);
    }
    public String getUserPhone(){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREFER_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_PHONE,null);
    }
    public int getUserId(){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREFER_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USER_ID,0);
    }
    public String getKeySubUserId(){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREFER_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_SUB_USER_ID,null);
    }

}
