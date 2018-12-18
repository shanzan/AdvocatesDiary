package app.diary.advocates.advocatesdiary;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Random;

public class VarificationCode extends Fragment {

    private View varificationview;
    private TextView username;
    private EditText userVarificationCode;
    private Button submit;
    Bundle mybundle;
    String message;
    FragmentManager fragmentManager;
    android.app.FragmentTransaction fragmentTransaction;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        varificationview=inflater.inflate(R.layout.fragment_varification_code, container, false);
        initialize();


        return varificationview;
    }
    private void initialize(){
        username=(TextView) varificationview.findViewById(R.id.myusername);
        userVarificationCode=(EditText) varificationview.findViewById(R.id.myVarificationcode);

       submit=(Button) varificationview.findViewById(R.id.mysubmit);
       mybundle=getArguments();
           username.setText(mybundle.getString("UserName"));
            message = mybundle.getString("VarCode");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userVarificationCode.getText().toString().equals(message)){
                    Bundle bundle=new Bundle();
                    bundle.putString("email",mybundle.getString("UserEmail"));
                    bundle.putString("type",mybundle.getString("UserType"));

                    Fragment fragment=new ForgetPasswordFragment();
                    fragment.setArguments(bundle);
                    fragmentManager=getFragmentManager();
                    fragmentTransaction =fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.home_fragment_place,fragment);
                    fragmentTransaction.commit();
                    NotificationDialogue.getNotifications("Notice","CODE MATCHED",getActivity());
                }else {
                    NotificationDialogue.getNotifications("Notice","WRONG CODE ENTERD",getActivity());
                }
            }
        });

    }
}
