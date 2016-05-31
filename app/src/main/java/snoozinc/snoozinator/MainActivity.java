package snoozinc.snoozinator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences allAlarms;

    TableLayout alarmTableScrollView;

    Button addAlarmButton;
    Button deleteAllAlarmsButton;

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        alarmTableScrollView = (TableLayout) findViewById(R.id.alarmTableScrollView);

        addAlarmButton = (Button) findViewById(R.id.addAlarmButton);
        deleteAllAlarmsButton = (Button)  findViewById(R.id.deleteAllAlarmsButton);

        id = 0;

        addAlarmButton.setOnClickListener(addAlarmButtonListener);
        deleteAllAlarmsButton.setOnClickListener(deleteAllAlarmsButtonListener);

    }

    private void insertAlarmInScrollView(){

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View newAlarm = inflater.inflate(R.layout.alarm_item_layout, null);

        Button deleteAlarmButton = (Button) newAlarm.findViewById(R.id.deleteAlarmButton);
        deleteAlarmButton.setOnClickListener(deleteAlarmButtonListener);

        alarmTableScrollView.addView(newAlarm, id++);

    }

    public View.OnClickListener addAlarmButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            insertAlarmInScrollView();
        }
    };

    public View.OnClickListener deleteAllAlarmsButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            alarmTableScrollView.removeAllViews();
            id = 0;
        }
    };

    public View.OnClickListener deleteAlarmButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            alarmTableScrollView.removeViewAt(--id);

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
