package app.diary.advocates.advocatesdiary;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class UserLoginFragment extends Fragment implements View.OnClickListener,TextWatcher, CompoundButton.OnCheckedChangeListener{

    FragmentManager fragmentManager;
    android.app.FragmentTransaction fragmentTransaction;
    private Button buttonregister,buttonlogin;
    private TextView hyperlinkforgot;
    private ProgressDialog progressDialog;

    RememberSharedPref rememberSharedPref;
    private EditText login_username,logon_pass;
    private CheckBox rememberme,subuser;
    private View resultview;
    private String subuser_id;
    private CheckValidation checkValidation;
    Bundle bundle;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
       resultview=inflater.inflate(R.layout.fragment_user_login, container, false);
       initialize(resultview);

       RememberSharedPref.getInstance(resultview.getContext()).getRememberme(rememberme,login_username,logon_pass);



        login_username.addTextChangedListener(this);
        logon_pass.addTextChangedListener(this);
        rememberme.setOnCheckedChangeListener(this);
        buttonregister.setOnClickListener(this);
        buttonlogin.setOnClickListener(this);
        hyperlinkforgot.setOnClickListener(this);
       return resultview;
    }

   private void initialize(View tview){
        bundle=new Bundle();
       checkValidation=new CheckValidation();
        //button initialize
        buttonregister=(Button) tview.findViewById(R.id.login_page_register);
        buttonlogin=(Button) tview.findViewById(R.id.buttonlogin);

        //edottext initialize
       login_username=(EditText) tview.findViewById(R.id.username_login);
       logon_pass=(EditText) tview.findViewById(R.id.password_login);

       //checkbox initialize
       rememberme=tview.findViewById(R.id.login_remember_me);
       subuser=tview.findViewById(R.id.subuser_login);

        //underline the text view
        hyperlinkforgot=(TextView) tview.findViewById(R.id.hyperlink);
        hyperlinkforgot.setPaintFlags(hyperlinkforgot.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

       progressDialog=new ProgressDialog(tview.getContext());
    }


    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.login_page_register){
         reDirecttoRegister();
         System.out.println("Hello World");
        }
        if (view.getId()==R.id.buttonlogin){
            if (TextUtils.isEmpty(login_username.getText().toString())){
                login_username.setError( "User name is required!" );
            }else if (TextUtils.isEmpty(logon_pass.getText().toString())){
                logon_pass.setError( "password is required!" );
            }else{
                userLogin();
            }

        }
        if (view.getId()==R.id.hyperlink){
            AlertDialog.Builder builder = new AlertDialog.Builder(resultview.getContext());
            builder.setTitle("Are you sure You Forget Your Password?");
            builder.setMessage("Your Email is :"+login_username.getText().toString());
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch(which){
                        case DialogInterface.BUTTON_POSITIVE:
                            if (!checkValidation.validateEmail(login_username.getText().toString())){
                                login_username.setError( "Invalid email!" );
                            }else{
                                forgetPassword();
                            }
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            break;
                    }
                }
            };
            builder.setPositiveButton("Yes", dialogClickListener);
            builder.setNegativeButton("No",dialogClickListener);
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        RememberSharedPref.getInstance(resultview.getContext()).managePrefs(login_username,logon_pass,rememberme);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        RememberSharedPref.getInstance(resultview.getContext()).managePrefs(login_username,logon_pass,rememberme);
    }


    private void reDirecttoRegister(){
        Fragment fragment=new UserRegisterFragment();
        fragmentManager=getFragmentManager();
        fragmentTransaction =fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_fragment_place,fragment);
        fragmentTransaction.commit();

    }
    private void userLogin(){
        if (subuser.isChecked()){
          subuser_id="subuser";
        }else {
            subuser_id="user";
        }
        final String username=login_username.getText().toString().trim();
        final String pass=logon_pass.getText().toString().trim();

        progressDialog.setMessage("Processing .....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")){
                                if (subuser.isChecked()){
                                    SharedPrefLoginInformation.getInstance(getContext())
                                            .subUserLogin(
                                                    jsonObject.getInt("id"),
                                                    jsonObject.getString("name"),
                                                    jsonObject.getString("email"),
                                                    jsonObject.getString("number"),
                                                    jsonObject.getString("usertypeid")
                                            );
                                }else {
                                    SharedPrefLoginInformation.getInstance(getContext())
                                            .userLogin(
                                                    jsonObject.getInt("id"),
                                                    jsonObject.getString("name"),
                                                    jsonObject.getString("email"),
                                                    jsonObject.getString("number")
                                            );
                                }

                                startActivity(new Intent(getContext(),DashboardActivity.class));
                                getActivity().finish();
                                progressDialog.dismiss();
                            }else {
                                NotificationDialogue.getNotifications("ERROR",jsonObject.getString("message"),getActivity());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            NotificationDialogue.getNotifications("Notice","Server Error",getActivity());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                NotificationDialogue.getNotifications("Notice", "SERVER ERROR",getActivity());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", username);
                params.put("password", pass);
                params.put("usertype", subuser_id);
                return params;
            }
        };
        RequestHandler.getInstance(resultview.getContext()).addToRequestQueue(stringRequest);
    }

    private void forgetPassword(){
        if (subuser.isChecked()){
            subuser_id="subuser";
        }else {
            subuser_id="user";
        }
        final String username=login_username.getText().toString().trim();

        progressDialog.setMessage("Processing .....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_FORGET_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")){

                                bundle.putString("UserName",jsonObject.getString("name"));
                                bundle.putString("UserEmail",jsonObject.getString("email"));
                                bundle.putString("VarCode",jsonObject.getString("vcode"));
                                bundle.putString("UserType",jsonObject.getString("type"));

                                Fragment fragment=new VarificationCode();
                                fragmentManager=getFragmentManager();
                                fragmentTransaction =fragmentManager.beginTransaction();
                                fragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.home_fragment_place,fragment);
                                fragmentTransaction.commit();
                                progressDialog.dismiss();
                                NotificationDialogue.getNotifications("ERROR",jsonObject.getString("message"),getActivity());
                            }else {
                                NotificationDialogue.getNotifications("ERROR",jsonObject.getString("message"),getActivity());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            NotificationDialogue.getNotifications("Notice","Server Error",getActivity());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                NotificationDialogue.getNotifications("Notice","Network Unrechable",getActivity());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", username);
                params.put("usertype", subuser_id);
                return params;
            }
        };
        RequestHandler.getInstance(resultview.getContext()).addToRequestQueue(stringRequest);
    }

}