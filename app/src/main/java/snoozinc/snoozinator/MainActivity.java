package snoozinc.snoozinator;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
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


public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    private FloatingActionButton addAlarmFab;

    private TableLayout alarmTableScrollView;

    private Button deleteAllAlarmsButton;


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
        }
    }

    private void insertAlarmInScrollView(){

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View newAlarm = inflater.inflate(R.layout.alarm_item_layout, null);

        TextView alarmDisplayTextView = (TextView) newAlarm.findViewById(R.id.alarmDisplayTextView);
        Button deleteAlarmButton = (Button) newAlarm.findViewById(R.id.deleteAlarmButton);
        Switch toggleAlarmSwitch = (Switch) newAlarm.findViewById(R.id.toggleAlarmSwitch);

        alarmDisplayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });

        deleteAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmTableScrollView.removeView((View) v.getParent().getParent());
            }
        });

        toggleAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                 }else {

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
