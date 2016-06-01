package snoozinc.snoozinator;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    private PendingIntent pendingIntent;
    private Intent alarmIntent;

    private SharedPreferences allAlarms;
    private FloatingActionButton fab;

    TableLayout alarmTableScrollView;

    Button deleteAllAlarmsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        alarmTableScrollView = (TableLayout) findViewById(R.id.alarmTableScrollView);

        deleteAllAlarmsButton = (Button)  findViewById(R.id.deleteAllAlarmsButton);

        deleteAllAlarmsButton.setOnClickListener(deleteAllAlarmsButtonListener);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertAlarmInScrollView();
            }
        });

        alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
        /*
        * Make sure the time is only set once. Dialog has a bug giving a value, when closed without
        * hitting the 'OK' button
        * */
        if (view.isShown()) {
            TextView textView = (TextView) findViewById(R.id.alarmDisplayTextView);
            /*
            * Format the hour and minute so that we have leading zeroes
            * */
            String hour = String.format("%02d", hourOfDay);
            String minute = String.format("%02d", minuteOfDay);
            textView.setText(hour + ":" + minute);

            startAlarm(hourOfDay, minuteOfDay);
        }
    }

    public void startAlarm(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 10000;

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                    interval, pendingIntent);
    }

    public void showTimePicker(View v) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    private void insertAlarmInScrollView(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View newAlarm = inflater.inflate(R.layout.alarm_item_layout, null);

        Button deleteAlarmButton = (Button) newAlarm.findViewById(R.id.deleteAlarmButton);
        deleteAlarmButton.setOnClickListener(deleteAlarmButtonListener);

        alarmTableScrollView.addView(newAlarm);

        Switch toggleAlarmSwitch = (Switch) newAlarm.findViewById(R.id.toggleAlarmSwitch);

        toggleAlarmSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                manager.cancel(pendingIntent);
            }
        });
    }

    public View.OnClickListener deleteAllAlarmsButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            alarmTableScrollView.removeAllViews();
        }
    };



    public View.OnClickListener deleteAlarmButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            alarmTableScrollView.removeView((View) v.getParent().getParent());
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
