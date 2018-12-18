package app.diary.advocates.advocatesdiary;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    android.app.Fragment fragment;
    FragmentManager fragmentManager;
    android.app.FragmentTransaction fragmentTransaction;

    private TextView dash_nav_name,dash_nav_email;
    public static ArrayList<Case> caseArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        caseArrayList=new ArrayList<>();

        if (!SharedPrefLoginInformation.getInstance(getApplicationContext()).isLoggedIN()){
            finish();
            startActivity(new Intent(this,HomeActivity.class));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headview=navigationView.getHeaderView(0);
        dash_nav_name=(TextView) headview.findViewById(R.id.dash_nav_name);
        dash_nav_email=(TextView) headview.findViewById(R.id.dash_nav_email);

        dash_nav_name.setText(SharedPrefLoginInformation.getInstance(getApplicationContext()).getUserName().toString());
        dash_nav_email.setText(SharedPrefLoginInformation.getInstance(getApplicationContext()).getUserEmail().toString());


        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//
//        }
//        if (id == R.id.action_logout) {
//
//        }
//
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.addcase) {
            fragment=new AddnewCase();
            fragmentManager=getFragmentManager();
            fragmentTransaction =fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.dashboard_placeholder,fragment);
            fragmentTransaction.commit();
            // Handle the camera action

        } else if (id == R.id.add_sub_user) {
            if (SharedPrefLoginInformation.getInstance(getApplicationContext()).checkUsertype()){
                fragment=new NonAuthorizrdFragment();
            }else {
                fragment=new UserRegisterFragment();
            }
                fragmentManager=getFragmentManager();
                fragmentTransaction =fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.dashboard_placeholder,fragment);
                fragmentTransaction.commit();

        } else if (id == R.id.viewcase) {
            fragment=new ViewCases();
            fragmentManager=getFragmentManager();
            fragmentTransaction =fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.dashboard_placeholder,fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.view_sub_User) {
            if (SharedPrefLoginInformation.getInstance(getApplicationContext()).checkUsertype()){
                fragment=new NonAuthorizrdFragment();
            }else {
                fragment=new ViewSubuser();
            }
            fragmentManager=getFragmentManager();
            fragmentTransaction =fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.dashboard_placeholder,fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.action_settings) {
            fragmentManager=getFragmentManager();
            fragmentTransaction =fragmentManager.beginTransaction();
            fragment=new UserProfileFragment();
            fragmentTransaction.replace(R.id.dashboard_placeholder,fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.action_logout) {
            SharedPrefLoginInformation.getInstance(this).logout();
            finish();
            startActivity(new Intent(this,HomeActivity.class));
        }
        else if (id == R.id.event_date) {
            fragment=new NextEvent();
            fragmentManager=getFragmentManager();
            fragmentTransaction =fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.dashboard_placeholder,fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
