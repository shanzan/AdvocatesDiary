package app.diary.advocates.advocatesdiary;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


public class ChangePasswordFragment extends Fragment {
    FragmentManager fragmentManager;
    android.app.FragmentTransaction fragmentTransaction;
    private TextView oldpassword,cnewpassword,cconfirmpassword;
    private Button changePasswordbtn;
    private View changeview;
    private String user_id,user_pward,constantValue,user_old_pward;
    private ProgressDialog progressDialog;
    private CheckValidation checkValidation;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        changeview=inflater.inflate(R.layout.fragment_change_password, container, false);
        initialize();
        changePasswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkpassword();
            }
        });
        return changeview;
    }
    private void initialize(){
        checkValidation=new CheckValidation();
        progressDialog=new ProgressDialog(changeview.getContext());
        oldpassword=(TextView) changeview.findViewById(R.id.old_password);
        cnewpassword=(TextView) changeview.findViewById(R.id.cnew_password);
        cconfirmpassword=(TextView) changeview.findViewById(R.id.cconfirm_password);

        changePasswordbtn=(Button) changeview.findViewById(R.id.cchangepassword);
    }
    private void checkpassword(){
        if (!checkValidation.validatePassword(oldpassword.getText().toString())){
            oldpassword.setError( "password at least 6 charecters" );
        }else if (!checkValidation.validatePassword(cnewpassword.getText().toString())){
            cnewpassword.setError( "password at least 6 charecters" );
        } else if (!cnewpassword.getText().toString().equals(cconfirmpassword.getText().toString())) {
           cconfirmpassword.setError("Password not matches");
        }else {
           updatepassword();
        }


    }
    private void updatepassword(){
        if (SharedPrefLoginInformation.getInstance(changeview.getContext()).checkUsertype()){
            constantValue=Constants.URL_SUB_PASSWORD_CHANGE;
        }else {
            constantValue=Constants.URL_PASSWORD_CHANGE;
        }
        user_old_pward=oldpassword.getText().toString();
        user_pward=cconfirmpassword.getText().toString();
        user_id=SharedPrefLoginInformation.getInstance(getActivity()).getUserEmail();


        progressDialog.setMessage("Processing .....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constantValue,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")){
                                NotificationDialogue.getNotifications("Notice",jsonObject.getString("message"),getActivity());
                                oldpassword.setText("");
                                cnewpassword.setText("");
                                cconfirmpassword.setText("");
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
                NotificationDialogue.getNotifications("Notice","Network Failed",getActivity());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("useremail",user_id);
                params.put("password",user_pward);
                params.put("oldpassword",user_old_pward);

                return params;
            }
        };
        RequestHandler.getInstance(changeview.getContext()).addToRequestQueue(stringRequest);
    }
}
