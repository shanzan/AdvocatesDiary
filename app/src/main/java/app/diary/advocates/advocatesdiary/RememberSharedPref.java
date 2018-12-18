package app.diary.advocates.advocatesdiary;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by khans on 3/28/2018.
 */

public class RememberSharedPref {

    private static RememberSharedPref rmInstance;
    private static Context mCtx;

    private static final String SHARED_PREFER_NAME="remembertestDate";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASS = "password";

    private RememberSharedPref(Context context) {
        mCtx = context;
    }

    public static synchronized RememberSharedPref getInstance(Context context) {
        if (rmInstance == null) {
            rmInstance = new RememberSharedPref(context);
        }
        return rmInstance;
    }
    public void managePrefs(EditText etUsername, EditText etPass, CheckBox rem_userpass) {
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREFER_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        if (rem_userpass.isChecked()) {
            editor.putString(KEY_USERNAME, etUsername.getText().toString().trim());
            editor.putString(KEY_PASS, etPass.getText().toString().trim());
            editor.putBoolean(KEY_REMEMBER, true);
            editor.apply();
        } else {
            editor.putBoolean(KEY_REMEMBER, false);
            editor.remove(KEY_PASS);//editor.putString(KEY_PASS,"");
            editor.remove(KEY_USERNAME);//editor.putString(KEY_USERNAME, "");
            editor.apply();
        }
    }
    public void getRememberme(CheckBox remember,EditText etUsername, EditText etPass){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREFER_NAME,Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean(KEY_REMEMBER, false))
            remember.setChecked(true);
        else
            remember.setChecked(false);
        etUsername.setText(sharedPreferences.getString(KEY_USERNAME,""));
        etPass.setText(sharedPreferences.getString(KEY_PASS,""));
    }
}
