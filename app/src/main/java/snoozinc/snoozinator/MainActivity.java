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
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Button;
import android.widget.TableLayout;
import java.util.Arrays;
import java.util.List;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    private FloatingActionButton addAlarmFab;
    private PendingIntent pendingIntent;
    private Intent alarmIntent;

    private SharedPreferences allAlarms;
    private FloatingActionButton fab;

    private TableLayout alarmTableScrollView;

    private Button deleteAllAlarmsButton;

    // Save the movement on slide
    float historicX = Float.NaN;
    // Distance to register the slide
    static final int DELTA = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        alarmTableScrollView = (TableLayout) findViewById(R.id.alarmTableScrollView);

        deleteAllAlarmsButton = (Button)  findViewById(R.id.deleteAllAlarmsButton);
        addAlarmFab = (FloatingActionButton) findViewById(R.id.addAlarmFab);

        deleteAllAlarmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmTableScrollView.removeAllViews();
            }
        });

        addAlarmFab.setOnClickListener(new View.OnClickListener() {
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

        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(this, "Alarm set for " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
    }

    private void insertAlarmInScrollView(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View newAlarm = inflater.inflate(R.layout.alarm_item_layout, null);

        RelativeLayout alarmRelativeLayout = (RelativeLayout) newAlarm.findViewById(R.id.alarmRelativeLayout);
        TextView alarmDisplayTextView = (TextView) newAlarm.findViewById(R.id.alarmDisplayTextView);
        final Switch toggleAlarmSwitch = (Switch) newAlarm.findViewById(R.id.toggleAlarmSwitch);

        alarmRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlarmItemSettings.class);
                startActivity(intent);
            }
        });

        /**
         * Register a slide to remove listener
         */
        alarmRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        historicX = event.getX();
                        break;

                    case MotionEvent.ACTION_UP:
                        if (event.getX() - historicX < -DELTA) {
                            alarmTableScrollView.removeView(v);
                            Toast.makeText(getBaseContext(), "Deleted", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        break;

                    default: return false;
                }
                return false;
            }
        });

        alarmDisplayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");
                toggleAlarmSwitch.setChecked(true);
            }
        });

        toggleAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //TODO: set toggle on
                 }else {
                    Intent stopIntent = new Intent(MainActivity.this, RingtonePlayingService.class);
                    MainActivity.this.stopService(stopIntent);
                }
            }
        });

        alarmTableScrollView.addView(newAlarm);
    }

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
