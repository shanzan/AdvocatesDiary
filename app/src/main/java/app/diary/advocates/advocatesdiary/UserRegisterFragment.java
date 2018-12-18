package app.diary.advocates.advocatesdiary;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class UserRegisterFragment extends Fragment implements View.OnClickListener{

    //create reference for fragment
    FragmentManager fragmentManager;
    android.app.FragmentTransaction fragmentTransaction;

    //create reference for xml item
    private EditText register_username,register_userpassword,register_useremail,register_userphone,register_confirmpass;
    private Button registerButton;
    private ProgressDialog progressDialog;
    private String useridtype;
    private CheckValidation checkValidation;
    View resultview;
    RadioButton radioButton;
    private String username,email,password,confirmpassword,phonenumber;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        resultview=inflater.inflate(R.layout.fragment_user_register, container, false);
        if (SharedPrefLoginInformation.getInstance(resultview.getContext()).isLoggedIN()){
            useridtype=Integer.toString(SharedPrefLoginInformation.getInstance(getActivity()).getUserId());
            NotificationDialogue.getNotifications("Warning","Here you can add a sub user",getActivity());
        }else {
            useridtype="0";
        }
        initialize(resultview);
        return resultview;
    }


    //initialization for all reference
    private void initialize(View view){

        checkValidation=new CheckValidation();

        register_username=(EditText) view.findViewById(R.id.reg_user_name);
        register_useremail=(EditText)  view.findViewById(R.id.reg_user_email);
        register_userphone=(EditText)  view.findViewById(R.id.reg_user_phone);
        register_userpassword=(EditText)  view.findViewById(R.id.reg_user_password);
        register_confirmpass=(EditText)  view.findViewById(R.id.reg_confirm_password);

        registerButton=(Button) view.findViewById(R.id.reg_register_button);
        radioButton=(RadioButton) view.findViewById(R.id.redio_term);

        progressDialog=new ProgressDialog(view.getContext());
        registerButton.setOnClickListener(this);

    }

    private void registerUser(View view){

        username=register_username.getText().toString().trim();
        email=register_useremail.getText().toString().trim();
        password=register_userpassword.getText().toString().trim();
        confirmpassword=register_confirmpass.getText().toString().trim();
        phonenumber=register_userphone.getText().toString().trim();

        progressDialog.setMessage("Registering User .....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            NotificationDialogue.getNotifications("Success",jsonObject.getString("message"),getActivity());
                            if (Boolean.valueOf(jsonObject.getString("error"))==false){
                                if (SharedPrefLoginInformation.getInstance(resultview.getContext()).isLoggedIN()){
                                    Fragment fragment=new ViewSubuser();
                                    fragmentManager=getFragmentManager();
                                    fragmentTransaction =fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.dashboard_placeholder,fragment);
                                    fragmentTransaction.commit();
                                }else {
                                    Fragment fragment=new UserLoginFragment();
                                    fragmentManager=getFragmentManager();
                                    fragmentTransaction =fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.home_fragment_place,fragment);
                                    fragmentTransaction.commit();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                NotificationDialogue.getNotifications("Error",error.getMessage(),getActivity());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", username);
                params.put("email", email);
                params.put("phone", phonenumber);
                params.put("password", password);
                params.put("type", useridtype);
                return params;
            }
        };
        RequestHandler.getInstance(view.getContext()).addToRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View view) {
        if (view==registerButton){
            if (radioButton.isChecked()){
                setCheckValidation();
            }else {
                NotificationDialogue.getNotifications("Warnings","Please Check the terms & conditions",getActivity());
            }
        }
    }

    public void setCheckValidation(){
        if (TextUtils.isEmpty(register_username.getText().toString())){
            register_username.setError( "User name is required!" );
        }else if (!checkValidation.validateEmail(register_useremail.getText().toString())){
            register_useremail.setError( "Not a valid email!" );
        }else if (!checkValidation.isValidMobile(register_userphone.getText().toString())) {
            register_userphone.setError("Invalid phone number");
        } else if (!checkValidation.validatePassword(register_userpassword.getText().toString())){
            register_userpassword.setError( "password at least 6 charecters" );
        } else if (!register_confirmpass.getText().toString().equals(register_userpassword.getText().toString())) {
            register_confirmpass.setError("Password not matches");
        }else {
            registerUser(resultview);
        }
    }
}
