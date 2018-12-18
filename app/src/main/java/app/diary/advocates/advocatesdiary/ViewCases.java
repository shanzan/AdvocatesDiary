package app.diary.advocates.advocatesdiary;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
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
import java.util.List;
import java.util.Map;

public class ViewCases extends Fragment implements CaseAdapter.OnAdapterItemClickListener{

    //create reference for fragment

    private String user_id;
    private View caseview;
    private ProgressDialog progressDialog;
    RecyclerView recyclerView;
    CaseAdapter caseAdapter;
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
        caseview=inflater.inflate(R.layout.fragment_view_cases, container, false);
        progressDialog=new ProgressDialog(caseview.getContext());

        recyclerView=(RecyclerView) caseview.findViewById(R.id.listrecycleview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);
        caseAdapter=new CaseAdapter();

            if (checkConncetion()==true){
                getCaseFromDatabase();
            }else {
                caseAdapter.setCasetlist(DashboardActivity.caseArrayList);
                recyclerView.setAdapter(caseAdapter);
                caseAdapter.notifyDataSetChanged();
            }
            
        caseAdapter.setMlistener(this);

        final SearchView searchView= (SearchView)caseview.findViewById(R.id.searchCase);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()){
                    searchView.setIconified(true);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<Case> filterModellist=filter(DashboardActivity.caseArrayList,newText);
                caseAdapter.setFilter(filterModellist);
                recyclerView.setAdapter(caseAdapter);
                return false;
            }
        });



       return caseview;
    }


    public void getCaseFromDatabase(){
        if (DashboardActivity.caseArrayList!=null){
            DashboardActivity.caseArrayList.clear();
        }
        if (SharedPrefLoginInformation.getInstance(caseview.getContext()).checkUsertype()){
            user_id=SharedPrefLoginInformation.getInstance(getActivity()).getKeySubUserId();
        }else {
            user_id=Integer.toString(SharedPrefLoginInformation.getInstance(getActivity()).getUserId());
        }
        progressDialog.setMessage("Processing .....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_CASES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                                try {
                                    JSONArray jsonarray = new JSONArray(response);
                                    if (response.length()>0) {
                                        for (int i = 0; i < jsonarray.length(); i++) {
                                            JSONObject jsonObject =jsonarray.getJSONObject(i);
                                            Case caseliset = new Case();
                                            caseliset.setCase_id(jsonObject.getString("case_id"));
                                            caseliset.setCase_number(jsonObject.getString("case_number"));
                                            caseliset.setCase_type(jsonObject.getString("case_type"));
                                            caseliset.setComplainant_name(jsonObject.getString("complainant_name"));
                                            caseliset.setComplainant_address(jsonObject.getString("complainant_address"));
                                            caseliset.setComplainant_phone(jsonObject.getString("complainant_phone"));
                                            caseliset.setOpponent_name(jsonObject.getString("opponent_name"));
                                            caseliset.setOpponent_address(jsonObject.getString("opponent_address"));
                                            caseliset.setOpponent_phone(jsonObject.getString("opponent_phone"));
                                            caseliset.setPrevious_date(jsonObject.getString("previous_date"));
                                            caseliset.setNext_date(jsonObject.getString("next_date"));
                                            caseliset.setCourt_name(jsonObject.getString("court_name"));
                                            caseliset.setCourt_type(jsonObject.getString("court_type"));
                                            caseliset.setCourt_genre(jsonObject.getString("court_genre"));
                                            caseliset.setRefered_by(jsonObject.getString("refered_by"));
                                            caseliset.setComment(jsonObject.getString("comment"));

                                            DashboardActivity.caseArrayList.add(caseliset);
                                        }
                                        caseAdapter.setCasetlist(DashboardActivity.caseArrayList);
                                        recyclerView.setAdapter(caseAdapter);
                                        caseAdapter.notifyDataSetChanged();
                                    }
                                    progressDialog.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    NotificationDialogue.getNotifications("Notice", e.getMessage(), getActivity());
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
        RequestHandler.getInstance(caseview.getContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private boolean checkConncetion(){

        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }else {
            return false;
        }
    }


    @Override
    public void OnItemClick(View v, int position) {
        Bundle casebundel=new Bundle();
        casebundel.putString("case_id",DashboardActivity.caseArrayList.get(position).getCase_id());
        casebundel.putString("case_number",DashboardActivity.caseArrayList.get(position).getCase_number());
        casebundel.putString("case_type",DashboardActivity.caseArrayList.get(position).getCase_type());
        casebundel.putString("complainant_name",DashboardActivity.caseArrayList.get(position).getComplainant_name());
        casebundel.putString("complainant_address",DashboardActivity.caseArrayList.get(position).getComplainant_address());
        casebundel.putString("complainant_phone",DashboardActivity.caseArrayList.get(position).getComplainant_phone());
        casebundel.putString("opponent_name",DashboardActivity.caseArrayList.get(position).getOpponent_name());
        casebundel.putString("opponent_address",DashboardActivity.caseArrayList.get(position).getOpponent_address());
        casebundel.putString("opponent_phone",DashboardActivity.caseArrayList.get(position).getOpponent_phone());
        casebundel.putString("previous_date",DashboardActivity.caseArrayList.get(position).getPrevious_date());
        casebundel.putString("next_date",DashboardActivity.caseArrayList.get(position).getNext_date());
        casebundel.putString("court_name",DashboardActivity.caseArrayList.get(position).getCourt_name());
        casebundel.putString("court_type",DashboardActivity.caseArrayList.get(position).getCourt_type());
        casebundel.putString("court_genre",DashboardActivity.caseArrayList.get(position).getCourt_genre());
        casebundel.putString("refered_by",DashboardActivity.caseArrayList.get(position).getRefered_by());
        casebundel.putString("comment",DashboardActivity.caseArrayList.get(position).getComment());


        fragment=new ViewSelectedCase();
        fragment.setArguments(casebundel);
        fragmentManager=getFragmentManager();
        fragmentTransaction =fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.dashboard_placeholder,fragment);
        fragmentTransaction.commit();
    }

    private List<Case> filter(List<Case> searchcase,String query){
        query=query.toLowerCase();
        final List<Case> filterCaseList=new ArrayList<>();
        for (Case model:searchcase){
            final String no=model.getCase_number().toLowerCase();
            final String name=model.getComplainant_name().toLowerCase();
            if (no.startsWith(query) || name.startsWith(query) ){
                filterCaseList.add(model);
            }
        }
        return filterCaseList;
    }
}
