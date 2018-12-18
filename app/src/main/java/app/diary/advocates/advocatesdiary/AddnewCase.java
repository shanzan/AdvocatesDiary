package app.diary.advocates.advocatesdiary;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class AddnewCase extends Fragment {

    private static final String TAG = "MainActivity";

    private TextView mDisplayDate;
    private String user_id,selected_date;
    private ProgressDialog progressDialog;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText case_number,complainant_name,complainant_phone,
            complainant_address,opponent_name,opponent_phone,opponent_address,
            court_genre,refered_by,comment;
    private Spinner case_type,court_name,court_type;
    private View result;
    private CheckValidation checkValidation;
    private Button add_new_button;
    Fragment fragment;
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
        result=inflater.inflate(R.layout.fragment_addnew_case, container, false);
        initialize(result);
        return result;
    }
    private void initialize(View result){
        progressDialog=new ProgressDialog(result.getContext());
        checkValidation=new CheckValidation();

        //initialize the edit text field
        case_number=(EditText) result.findViewById(R.id.add_case_number);
        complainant_name=(EditText) result.findViewById(R.id.add_case_co_name);
        complainant_phone=(EditText) result.findViewById(R.id.add_case_co_phone);
        complainant_address=(EditText) result.findViewById(R.id.add_case_co_address);
        opponent_name=(EditText) result.findViewById(R.id.add_case_op_name);
        opponent_phone=(EditText) result.findViewById(R.id.add_case_op_phone);
        opponent_address=(EditText) result.findViewById(R.id.add_case_op_address);
        court_genre=(EditText) result.findViewById(R.id.add_case_genre);
        refered_by=(EditText) result.findViewById(R.id.add_case_referedby);
        comment=(EditText) result.findViewById(R.id.add_case_comment);


        //initialize the spinner text
        case_type=(Spinner)result.findViewById(R.id.add_case_type);
        court_name=(Spinner)result.findViewById(R.id.add_case_court_name);
        court_type=(Spinner)result.findViewById(R.id.add_case_court_type);


        //initialize button
        add_new_button=(Button)result.findViewById(R.id.add_case_button);
        add_new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkfield();
            }
        });


        //initialize the date field
        mDisplayDate = (TextView) result.findViewById(R.id.case_select_date);



        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                selected_date=year + "-" + month + "-" +day;

                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };
    }


    private void addNewCasetoDatabase(){
        if (SharedPrefLoginInformation.getInstance(result.getContext()).checkUsertype()){
            user_id=SharedPrefLoginInformation.getInstance(getActivity()).getKeySubUserId();
        }else {
            user_id=Integer.toString(SharedPrefLoginInformation.getInstance(getActivity()).getUserId());
        }


        final String d_case_number=case_number.getText().toString().trim();
        final String d_complainant_name=complainant_name.getText().toString().trim();;
        final String d_complainant_phone=complainant_phone.getText().toString().trim();;
        final String d_complainant_address=complainant_address.getText().toString().trim();;
        final String d_opponent_name=opponent_name.getText().toString().trim();
        final String d_opponent_phone=opponent_phone.getText().toString().trim();;
        final String d_opponent_address=opponent_address.getText().toString().trim();;
        final String d_court_genre=court_genre.getText().toString().trim();
        final String d_refered_by=refered_by.getText().toString().trim();;
        final String d_comment=comment.getText().toString().trim();

        final String d_case_type=case_type.getSelectedItem().toString().trim();
        final String d_court_name=court_name.getSelectedItem().toString().trim();
        final String d_court_type=court_type.getSelectedItem().toString().trim();


        String[] dat=selected_date.split("-");
        if (dat[1].length()==1){
            dat[1]="0"+dat[1];
        }
        if (dat[2].length()==1){
            dat[2]="0"+dat[2];
        }
        final String previous_date=dat[0]+"-"+dat[1]+"-"+dat[2];
        System.out.println(previous_date);
        final String next_date=selected_date.trim();




        progressDialog.setMessage("Processing .....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_ADD_CASE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")){
                                fragment=new ViewCases();
                                fragmentManager=getFragmentManager();
                                fragmentTransaction =fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.dashboard_placeholder,fragment);
                                fragmentTransaction.commit();
                                NotificationDialogue.getNotifications("Notice",jsonObject.getString("message"),getActivity());
                                progressDialog.dismiss();
                            }else {
                                NotificationDialogue.getNotifications("ERROR",jsonObject.getString("message"),getActivity());
                            }
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

                params.put("number",d_case_number);
                params.put("type",d_case_type);
                params.put("c_name",d_complainant_name);
                params.put("c_phone",d_complainant_phone);
                params.put("c_address",d_complainant_address);
                params.put("o_name",d_opponent_name);
                params.put("o_phone",d_opponent_phone);
                params.put("o_address",d_opponent_address);
                params.put("pre_date",previous_date);
                params.put("next_date",next_date);
                params.put("court_name",d_court_name);
                params.put("court_type",d_court_type);
                params.put("court_genre",d_court_genre);
                params.put("referedby",d_refered_by);
                params.put("comment",d_comment);
                params.put("userid",user_id);

                return params;
            }
        };
        RequestHandler.getInstance(result.getContext()).addToRequestQueue(stringRequest);
    }
    private void checkfield(){
        if (TextUtils.isEmpty(case_number.getText().toString())){
           case_number.setError( "Case Number is required!" );
        }else if (TextUtils.isEmpty(complainant_name.getText().toString())){
            complainant_name.setError( "Complainant name is required" );
        }else if (TextUtils.isEmpty(complainant_address.getText().toString())){
            complainant_address.setError( "Complainant address is required" );
        } else if (complainant_phone.getText().toString().length()< 11 || !checkValidation.isValidMobile(complainant_phone.getText().toString())){
            complainant_phone.setError( "Complaint Number is required" );
        }else if (mDisplayDate.getText().equals("Select Case Date")){
            mDisplayDate.setError( "Case Date is required" );
        }else if (TextUtils.isEmpty(court_genre.getText().toString())){
            court_genre.setError( "Court Genre is required" );
        }else if (TextUtils.isEmpty(refered_by.getText().toString())){
            refered_by.setError( "Refered by is required" );
        }else if (TextUtils.isEmpty(comment.getText().toString())){
            comment.setError( "Comment is required" );
        }
        else {
            addNewCasetoDatabase();
        }
    }
    /*else if (TextUtils.isEmpty(opponent_name.getText().toString())){
            opponent_name.setError( "Opponent name is required" );
        }else if (TextUtils.isEmpty(opponent_address.getText().toString())){
            opponent_address.setError( "Opponent addrss is required" );
        }else if (opponent_phone.getText().toString().length()< 11 || !checkValidation.isValidMobile(opponent_phone.getText().toString())){
            opponent_phone.setError( "Opponent Number is required" );
        }*/



}
