package app.diary.advocates.advocatesdiary;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

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

public class Datewisecase extends Fragment implements CaseAdapter.OnAdapterItemClickListener {

    //create reference for fragment

    private String user_id;
    private View caseview;
    private ProgressDialog progressDialog;
    RecyclerView recyclerView;
    CaseAdapter caseAdapter;
    android.app.Fragment fragment;
    FragmentManager fragmentManager;
    android.app.FragmentTransaction fragmentTransaction;
    TextView gettextview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        caseview=inflater.inflate(R.layout.fragment_datewisecase, container, false);
        progressDialog=new ProgressDialog(caseview.getContext());

        gettextview=(TextView)caseview.findViewById(R.id.datetext);

        recyclerView=(RecyclerView) caseview.findViewById(R.id.datelistrecycleview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);
        caseAdapter=new CaseAdapter();

            Bundle myeventdate=getArguments();
            ArrayList<Case> casesbydate=new ArrayList<>();
            String event=myeventdate.getString("event_case_date");
            gettextview.setText(event);
            for (int position=0;position<DashboardActivity.caseArrayList.size();position++){

                if (DashboardActivity.caseArrayList.get(position).getNext_date().equals(event.toString().trim())){
                    Case caseliset = new Case();
                    caseliset.setCase_id(DashboardActivity.caseArrayList.get(position).getCase_id());
                    caseliset.setCase_number(DashboardActivity.caseArrayList.get(position).getCase_number());
                    caseliset.setCase_type(DashboardActivity.caseArrayList.get(position).getCase_type());
                    caseliset.setComplainant_name(DashboardActivity.caseArrayList.get(position).getComplainant_name());
                    caseliset.setComplainant_address(DashboardActivity.caseArrayList.get(position).getComplainant_address());
                    caseliset.setComplainant_phone(DashboardActivity.caseArrayList.get(position).getComplainant_phone());
                    caseliset.setOpponent_name(DashboardActivity.caseArrayList.get(position).getOpponent_name());
                    caseliset.setOpponent_address(DashboardActivity.caseArrayList.get(position).getOpponent_address());
                    caseliset.setOpponent_phone(DashboardActivity.caseArrayList.get(position).getOpponent_phone());
                    caseliset.setPrevious_date(DashboardActivity.caseArrayList.get(position).getPrevious_date());
                    caseliset.setNext_date(DashboardActivity.caseArrayList.get(position).getNext_date());
                    caseliset.setCourt_name(DashboardActivity.caseArrayList.get(position).getCourt_name());
                    caseliset.setCourt_type(DashboardActivity.caseArrayList.get(position).getCourt_type());
                    caseliset.setCourt_genre(DashboardActivity.caseArrayList.get(position).getCourt_genre());
                    caseliset.setRefered_by(DashboardActivity.caseArrayList.get(position).getRefered_by());
                    caseliset.setComment(DashboardActivity.caseArrayList.get(position).getComment());

                    casesbydate.add(caseliset);
                }
            }
            caseAdapter.setCasetlist(casesbydate);
            recyclerView.setAdapter(caseAdapter);
            caseAdapter.notifyDataSetChanged();

        caseAdapter.setMlistener(this);

        return caseview;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
}
