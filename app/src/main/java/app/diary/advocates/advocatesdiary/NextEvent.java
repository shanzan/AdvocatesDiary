package app.diary.advocates.advocatesdiary;

import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;



public class NextEvent extends Fragment{

    private View cview;
    private com.applandeo.materialcalendarview.CalendarView calendarView;
    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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
        cview=inflater.inflate(R.layout.fragment_next_event, container, false);


        initializedCalender();
        return cview;
    }
    private void initializedCalender(){
        Calendar min = Calendar.getInstance();
        calendarView = (com.applandeo.materialcalendarview.CalendarView) cview.findViewById(R.id.calendarView);

        //calendarView.setMinimumDate(min);
        final List<EventDay> events = new ArrayList<>();
        for (int i=0;i<DashboardActivity.caseArrayList.size();i++){
            String[] date=DashboardActivity.caseArrayList.get(i).getNext_date().split("-");

            System.out.println(DashboardActivity.caseArrayList.get(i).getNext_date());

            Calendar calendar=new GregorianCalendar();
            calendar.set(Calendar.YEAR,Integer.parseInt(date[0]));
            calendar.set(Calendar.MONTH,Integer.parseInt(date[1])-1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));
            events.add(new EventDay(calendar,R.drawable.ic_add_box_black_24dp));
        }
        calendarView.setEvents(events);
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                for (int i=0;i<events.size();i++){
                    if (eventDay.getCalendar().equals(events.get(i).getCalendar())){
                        Bundle casebundel=new Bundle();
                        casebundel.putString("event_case_date",sdf.format(clickedDayCalendar.getTime()).toString());
                        Toast.makeText(cview.getContext(),sdf.format(clickedDayCalendar.getTime()),Toast.LENGTH_LONG).show();

                        fragment=new Datewisecase();
                        fragment.setArguments(casebundel);
                        fragmentManager=getFragmentManager();
                        fragmentTransaction =fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.dashboard_placeholder,fragment);
                        fragmentTransaction.commit();
                    }
                }

            }
        });
    }
}
