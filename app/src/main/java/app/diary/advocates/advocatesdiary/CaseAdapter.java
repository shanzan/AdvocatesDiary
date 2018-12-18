package app.diary.advocates.advocatesdiary;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khans on 3/31/2018.
 */

public class CaseAdapter extends  RecyclerView.Adapter<CaseAdapter.ViewHolder> {



    private ArrayList<Case> casetlist;

    private OnAdapterItemClickListener mlistener;


    public interface OnAdapterItemClickListener{
        void OnItemClick(View v,int position);
    }

    public OnAdapterItemClickListener getMlistener() {
        return mlistener;
    }

    public void setMlistener(OnAdapterItemClickListener mlistener) {
        this.mlistener = mlistener;
    }
    public ArrayList<Case> getCasetlist() {
        return casetlist;
    }

    public void setCasetlist(ArrayList<Case> casetlist) {
        this.casetlist = casetlist;
    }

    @Override
    public CaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.caselist,parent,false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CaseAdapter.ViewHolder holder, int position) {
        holder.case_number.setText(getCasetlist().get(position).getCase_number());
        holder.complainant_name.setText(getCasetlist().get(position).getComplainant_name());
        holder.complainant_previousdate.setText(getCasetlist().get(position).getComplainant_phone());
        holder.complainant_nextdate.setText(casetlist.get(position).getNext_date());
    }

    public void setFilter(List<Case> caseitem){
        casetlist=new ArrayList<>();
        casetlist.addAll(caseitem);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return casetlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView case_number,complainant_name,complainant_previousdate,complainant_nextdate;

        public ViewHolder(View itemView) {
            super(itemView);

            case_number= (TextView) itemView.findViewById(R.id.recyclecasenumber);
            complainant_name= (TextView) itemView.findViewById(R.id.recyclecasename);
            complainant_previousdate= (TextView) itemView.findViewById(R.id.recyclepriviousdate);
            complainant_nextdate= (TextView) itemView.findViewById(R.id.recyclenextdate);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getMlistener().OnItemClick(view,getAdapterPosition());
                }
            });
        }

    }
}
