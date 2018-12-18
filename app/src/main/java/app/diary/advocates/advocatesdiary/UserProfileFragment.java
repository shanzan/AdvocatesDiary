package app.diary.advocates.advocatesdiary;

import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class UserProfileFragment extends Fragment {

    Button userchangePassword;
    Fragment fragment;
    FragmentManager fragmentManager;
    android.app.FragmentTransaction fragmentTransaction;
    private TextView username,useremail,userPhone;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View result=inflater.inflate(R.layout.fragment_user_profile, container, false);


        //initialize textview for show information
        username=(TextView) result.findViewById(R.id.profile_user_name);
        useremail=(TextView)result.findViewById(R.id.profile_user_email);
        userPhone=(TextView)result.findViewById(R.id.profile_userphone);

        username.setText(SharedPrefLoginInformation.getInstance(getActivity()).getUserName());
        useremail.setText(SharedPrefLoginInformation.getInstance(getActivity()).getUserEmail());
        userPhone.setText("0"+SharedPrefLoginInformation.getInstance(getActivity()).getUserPhone());




        //change password button initialize to go the changepassword fragment
        userchangePassword=(Button)result.findViewById(R.id.user_change_password);

        //change password button click to go the changepassword fragment
        userchangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment=new ChangePasswordFragment();
                fragmentManager=getFragmentManager();
                fragmentTransaction =fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.dashboard_placeholder,fragment);
                fragmentTransaction.commit();
            }
        });
        return result;
    }
}
