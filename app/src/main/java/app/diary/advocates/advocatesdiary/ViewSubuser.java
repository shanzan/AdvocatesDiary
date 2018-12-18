package app.diary.advocates.advocatesdiary;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewSubuser extends Fragment {
    private ProgressDialog progressDialog;
    RecyclerView recyclerView;
    SubUserAdapter subUserAdapter;
    public static ArrayList<SubUsers> subUsersArrayList;
    SubUserAdapter.OnSubAdapterItemClickListener listener;
    View subuserview;
    private String user_id;
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
        subuserview=inflater.inflate(R.layout.fragment_view_subuser, container, false);

        subUsersArrayList=new ArrayList<>();
        progressDialog=new ProgressDialog(subuserview.getContext());

        recyclerView=(RecyclerView) subuserview.findViewById(R.id.listuserview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);
        getSubUsers();
        listener= new SubUserAdapter.OnSubAdapterItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {
                final String subuserid=subUsersArrayList.get(position).getSubuseid();
                AlertDialog.Builder builder = new AlertDialog.Builder(subuserview.getContext());
                builder.setTitle("Delete Sub User ");
                builder.setMessage("Are you sure?");
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:
                               deleteSubUsers(subuserid);
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
        };

        return subuserview;
    }
    private void getSubUsers(){
        if (SharedPrefLoginInformation.getInstance(subuserview.getContext()).isLoggedIN()){
            user_id=Integer.toString(SharedPrefLoginInformation.getInstance(getActivity()).getUserId());
        }
        progressDialog.setMessage("Processing .....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_ALL_SUB_USERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            if (response.length()>0) {
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject jsonObject =jsonarray.getJSONObject(i);
                                    SubUsers subUsers=new SubUsers();
                                    subUsers.setSubuseid(jsonObject.getString("sub_user_id"));
                                    subUsers.setSubuser_name(jsonObject.getString("sub_user_name"));
                                    subUsers.setSubUserEmail(jsonObject.getString("sub_user_email"));
                                    subUsers.setSubUserPhone(jsonObject.getString("sub_user_phone"));

                                    subUsersArrayList.add(subUsers);
                                }
                                subUserAdapter=new SubUserAdapter(listener,subUsersArrayList);
                                recyclerView.setAdapter(subUserAdapter);
                                subUserAdapter.notifyDataSetChanged();
                            }else {
                                NotificationDialogue.getNotifications("Notice", "No SUB USER FOUND", getActivity());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            NotificationDialogue.getNotifications("Notice", "No SUB USER FOUND", getActivity());
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
                params.put("id",user_id);
                return params;
            }
        };
        RequestHandler.getInstance(subuserview.getContext()).addToRequestQueue(stringRequest);
    }

    private void deleteSubUsers(final String id){

        progressDialog.setMessage("Processing .....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_DELETE_SUB_USERS,
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
                params.put("id",id);
                return params;
            }
        };
        RequestHandler.getInstance(subuserview.getContext()).addToRequestQueue(stringRequest);
    }
}
