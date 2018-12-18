package app.diary.advocates.advocatesdiary;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class ViewSelectedCase extends Fragment {
    View selectView;

    private TextView case_number,complainant_name,opponent_name, refered_by,previousdate,mnextDate;
    private EditText complainant_phone,complainant_address,opponent_phone,opponent_address,court_genre,comment;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Spinner case_type,court_type,case_stage;
    Bundle getcasearguments;
    private Button updateButton,deleteButton;
    private String case_id,selected_date;
    private ProgressDialog progressDialog;
    private CheckValidation checkValidation;
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
        selectView=inflater.inflate(R.layout.fragment_view_selected_case, container, false);
        getcasearguments=getArguments();
        initialize();
        return selectView;
    }

    private void initialize(){
        progressDialog=new ProgressDialog(selectView.getContext());
        checkValidation=new CheckValidation();

        //button initialize
        updateButton=(Button) selectView.findViewById(R.id.update_case_button);
        deleteButton=(Button)selectView.findViewById(R.id.delete_case_button);

        //initialize text view component
        case_number = (TextView) selectView.findViewById(R.id.update_case_no);
        complainant_name = (TextView) selectView.findViewById(R.id.update_complain_name);
        opponent_name = (TextView) selectView.findViewById(R.id.update_opponent_name);
        refered_by = (TextView) selectView.findViewById(R.id.update_refered_by);


//        //initialize date
        previousdate= (TextView) selectView.findViewById(R.id.lastcaseDate);
        mnextDate= (TextView) selectView.findViewById(R.id.upnextcasedate);
//
//
//        //initialize edit text component
        comment = (EditText) selectView.findViewById(R.id.updateComment);
        court_genre= (EditText) selectView.findViewById(R.id.updatecourtGenre);
        complainant_phone= (EditText) selectView.findViewById(R.id.updateC_phone);
        complainant_address= (EditText) selectView.findViewById(R.id.updateC_address);
        opponent_phone= (EditText) selectView.findViewById(R.id.update_opponent_phone);
        opponent_address= (EditText) selectView.findViewById(R.id.update_oppo_address);

//
        //initialize spinner
        case_type=(Spinner)selectView.findViewById(R.id.updateCasetype);
        court_type=(Spinner)selectView.findViewById(R.id.updateCourtType);
        case_stage=(Spinner)selectView.findViewById(R.id.updateCourtName);

        //get the value of spinner which will show
        case_type.setSelection(getIndex(case_type,getcasearguments.getString("case_type") ));
        court_type.setSelection(getIndex(court_type, getcasearguments.getString("court_type")));
        case_stage.setSelection(getIndex(case_stage, getcasearguments.getString("court_name")));

        getDetails();


    }
    private void getDetails(){
        //text view set text
        case_number.setText(getcasearguments.getString("case_number"));
        complainant_name.setText(getcasearguments.getString("complainant_name"));
        opponent_name.setText(getcasearguments.getString("opponent_name"));
        refered_by.setText(getcasearguments.getString("refered_by"));


        //edittext set text
        court_genre.setText(getcasearguments.getString("court_genre"));
        comment.setText( getcasearguments.getString("comment"));
        complainant_phone.setText("0"+getcasearguments.getString("complainant_phone"));
        complainant_address.setText( getcasearguments.getString("complainant_address"));
        opponent_phone.setText("0"+getcasearguments.getString("opponent_phone"));
        opponent_address.setText( getcasearguments.getString("opponent_address"));


        //set date
        final String previous[]=getcasearguments.getString("previous_date").split(",");
        previousdate.setText(previous[previous.length-1]);

        previousdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getpredates="";
                for (int i=0;i<previous.length;i++){
                    getpredates+=previous[i]+"\n";
                }
                NotificationDialogue.getNotifications("Previous Dates",getpredates,getActivity());
            }
        });

        final String nextdate[]=getcasearguments.getString("next_date").split("-");
        mnextDate.setText(nextdate[1]+"/"+nextdate[2]+"/"+nextdate[0]);
        selected_date=getcasearguments.getString("next_date").trim();

        mnextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year=Integer.parseInt(nextdate[0]);
                int month=Integer.parseInt(nextdate[1])-1;
                int day=Integer.parseInt(nextdate[2]);

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
                selected_date=year + "-" + month + "-" +day;
                String date = month + "/" + day + "/" + year;
                mnextDate.setText(date);
            }
        };

        //update case by clickbutton
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckField();
            }
        });

        //delete case by alert dialogue
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Build an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(selectView.getContext());

                // Set a title for alert dialog
                builder.setTitle("Delete Case");

                // Ask the final question
                builder.setMessage("Are you sure?");

                // Set click listener for alert dialog buttons
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteCase();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Yes", dialogClickListener);

                // Set the alert dialog no button click listener
                builder.setNegativeButton("No",dialogClickListener);

                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();
            }
        });

    }





    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString.trim())){
                index = i;
            }
        }
        return index;
    }

    private void updateCase(){

        //case id initialize
        case_id=getcasearguments.getString("case_id").trim();

        final String d_complainant_phone=complainant_phone.getText().toString().trim();;
        final String d_complainant_address=complainant_address.getText().toString().trim();;
        final String d_opponent_phone=opponent_phone.getText().toString().trim();;
        final String d_opponent_address=opponent_address.getText().toString().trim();;
        final String d_court_genre=court_genre.getText().toString().trim();
        final String d_comment=comment.getText().toString().trim();
        final String d_case_type=case_type.getSelectedItem().toString().trim();
        final String d_court_name=case_stage.getSelectedItem().toString().trim();
        final String d_court_type=court_type.getSelectedItem().toString().trim();

        final String previous_date=getcasearguments.getString("next_date").trim();
        final String next_date=selected_date.trim();




        progressDialog.setMessage("Processing .....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_UPDATE_CASES,
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

                params.put("type",d_case_type);
                params.put("c_phone",d_complainant_phone);
                params.put("c_address",d_complainant_address);
                params.put("o_phone",d_opponent_phone);
                params.put("o_address",d_opponent_address);
                params.put("pre_date",previous_date);
                params.put("next_date",next_date);
                params.put("court_name",d_court_name);
                params.put("court_type",d_court_type);
                params.put("court_genre",d_court_genre);
                params.put("comment",d_comment);
                params.put("caseid",case_id);

                return params;
            }
        };
        RequestHandler.getInstance(selectView.getContext()).addToRequestQueue(stringRequest);
    }
    private void CheckField(){
        if (TextUtils.isEmpty(complainant_address.getText().toString())){
            complainant_address.setError( "Complainant address is required" );
        } else if (complainant_phone.getText().toString().length()< 11 || !checkValidation.isValidMobile(complainant_phone.getText().toString())){
            complainant_phone.setError( "Complaint Number is required" );
        }else if (TextUtils.isEmpty(opponent_address.getText().toString())){
            opponent_address.setError( "Opponent addrss is required" );
        }else if (opponent_phone.getText().toString().length()< 11 || !checkValidation.isValidMobile(opponent_phone.getText().toString())){
            opponent_phone.setError( "Opponent Number is required" );
        }else if (TextUtils.isEmpty(court_genre.getText().toString())){
            court_genre.setError( "Court Genre is required" );
        }else if (TextUtils.isEmpty(comment.getText().toString())){
            comment.setError( "Comment is required" );
        }
        else {
           updateCase();
        }
    }
    private void deleteCase(){

        //case id initialize
        case_id=getcasearguments.getString("case_id");
        System.out.println(case_id);

        progressDialog.setMessage("Processing .....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_DELETE_CASES,
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

                params.put("caseid",case_id);

                return params;
            }
        };
        RequestHandler.getInstance(selectView.getContext()).addToRequestQueue(stringRequest);
    }

}
