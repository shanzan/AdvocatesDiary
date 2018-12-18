package app.diary.advocates.advocatesdiary;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by khans on 4/2/2018.
 */

public class SubUserAdapter extends RecyclerView.Adapter<SubUserAdapter.ViewHolder> {

    private ArrayList<SubUsers> subuserlist;
    private SubUserAdapter.OnSubAdapterItemClickListener sublisener;

    public interface OnSubAdapterItemClickListener{
        void OnItemClick(View v,int position);
    }

    public  SubUserAdapter(SubUserAdapter.OnSubAdapterItemClickListener mlisener, ArrayList<SubUsers> sublist){
        this.subuserlist=sublist;
        this.sublisener=mlisener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.subuserlists,parent,false);
        SubUserAdapter.ViewHolder vh=new SubUserAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.username.setText(subuserlist.get(position).getSubuser_name());
        holder.useremail.setText(subuserlist.get(position).getSubUserEmail());
        holder.userphone.setText("0"+subuserlist.get(position).getSubUserPhone());
    }

    @Override
    public int getItemCount() {
        return subuserlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView username,useremail,userphone;
        Button subuserdelete;

        public ViewHolder(View itemView) {
            super(itemView);

            username= (TextView) itemView.findViewById(R.id.view_username);
            useremail= (TextView) itemView.findViewById(R.id.view_email);
            userphone= (TextView) itemView.findViewById(R.id.view_phone);
            subuserdelete=(Button) itemView.findViewById(R.id.delete_sub_user);

            subuserdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sublisener.OnItemClick(view,getAdapterPosition());
                }
            });
        }

    }

}
