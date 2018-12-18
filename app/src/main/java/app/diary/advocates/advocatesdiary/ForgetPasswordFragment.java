package app.diary.advocates.advocatesdiary;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ForgetPasswordFragment extends Fragment {

    private EditText forgetnewpass,forgetconfirmpass;
    private Button forgetsubmit;
    View view;
    ProgressDialog progressDialog;
    CheckValidation checkValidation;
    FragmentManager fragmentManager;
    android.app.FragmentTransaction fragmentTransaction;
    Fragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_forget_password, container, false);
        initialize();
        return view;
    }
    private void initialize(){
        progressDialog=new ProgressDialog(view.getContext());
        checkValidation=new CheckValidation();

        forgetnewpass=(EditText) view.findViewById(R.id.forgetnewpass);
        forgetconfirmpass=(EditText) view.findViewById(R.id.forgetconfirmpass);

        forgetsubmit=(Button) view.findViewById(R.id.forgetchangepass);

        forgetsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkValidation.validatePassword(forgetnewpass.getText().toString())){
                    forgetnewpass.setError( "password at least 6 charecters" );
                } else if (!forgetconfirmpass.getText().toString().equals(forgetnewpass.getText().toString())) {
                    forgetconfirmpass.setError("Password not matches");
                }else {
                    passwordChange();
                }
            }
        });
    }
    private void passwordChange(){
        Bundle mybundle=getArguments();
        final String usermail=mybundle.getString("email");
        final String usertype=mybundle.getString("type");
        final String userPass=forgetconfirmpass.getText().toString().trim();

        progressDialog.setMessage("Processing .....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_RECOVER_CHANGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")){
                                Fragment fragment=new UserLoginFragment();
                                fragmentManager=getFragmentManager();
                                fragmentTransaction =fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.home_fragment_place,fragment);
                                fragmentTransaction.commit();
                                NotificationDialogue.getNotifications("Notice",jsonObject.getString("message"),getActivity());
                            }else {
                                NotificationDialogue.getNotifications("ERROR",jsonObject.getString("message"),getActivity());
                            }
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            NotificationDialogue.getNotifications("Notice",e.getMessage(),getActivity());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                NotificationDialogue.getNotifications("Notice", error.getMessage(),getActivity());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("useremail",usermail);
                params.put("password",userPass);
                params.put("type",usertype);

                return params;
            }
        };
        RequestHandler.getInstance(view.getContext()).addToRequestQueue(stringRequest);
    }
}
