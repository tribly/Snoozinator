package snoozinc.snoozinator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "alarmDB";

    private static final String TABLE_ALARMS = "alarms";

    private static final String KEY_ID = "id";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTE = "minute";
    private static final String KEY_ON = "activationStatus";
    private static final String KEY_MON = "mon";
    private static final String KEY_TUE = "tue";
    private static final String KEY_WED = "wed";
    private static final String KEY_THU = "thu";
    private static final String KEY_FRI = "fri";
    private static final String KEY_SAT = "sat";
    private static final String KEY_SUN = "sun";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ALARMS_TABLE = "CREATE TABLE " + TABLE_ALARMS
                + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_HOUR + " INTEGER,"
                + KEY_MINUTE + " INTEGER," + KEY_ON + " TEXT," + KEY_MON + " TEXT,"
                + KEY_TUE + " TEXT," + KEY_WED + " TEXT," + KEY_THU + " TEXT,"
                + KEY_FRI + " TEXT," + KEY_SAT + " TEXT," + KEY_SUN + " TEXT" + ")";
        db.execSQL(CREATE_ALARMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);

        onCreate(db);
    }

    public void addAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        Boolean[] weekDays = alarm.getWeekDays();

        ContentValues values = new ContentValues();
        values.put(KEY_HOUR, alarm.getHour());
        values.put(KEY_MINUTE, alarm.getMinute());
        values.put(KEY_ON, alarm.getOn().toString());
        values.put(KEY_MON, weekDays[0].toString());
        values.put(KEY_TUE, weekDays[1].toString());
        values.put(KEY_WED, weekDays[2].toString());
        values.put(KEY_THU, weekDays[3].toString());
        values.put(KEY_FRI, weekDays[4].toString());
        values.put(KEY_SAT, weekDays[5].toString());
        values.put(KEY_SUN, weekDays[6].toString());

        db.insert(TABLE_ALARMS, null, values);
        db.close();
    }

    public Alarm getAlarm(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ALARMS, new String[] { KEY_ID, KEY_HOUR, KEY_MINUTE,
                KEY_MON, KEY_TUE, KEY_WED, KEY_THU, KEY_FRI, KEY_SAT, KEY_SUN }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Boolean[] weekDays = {Boolean.parseBoolean(cursor.getString(4)),
                Boolean.parseBoolean(cursor.getString(5)), Boolean.parseBoolean(cursor.getString(6)),
                Boolean.parseBoolean(cursor.getString(7)), Boolean.parseBoolean(cursor.getString(8)),
                Boolean.parseBoolean(cursor.getString(9)), Boolean.parseBoolean(cursor.getString(10))};

        Alarm alarm = new Alarm(Integer.parseInt(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)),
                Boolean.parseBoolean(cursor.getString(3)), weekDays);

        cursor.close();
        db.close();

        return alarm;
    }

    public List<Alarm> getAllAlarms() {
        List<Alarm> alarmList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_ALARMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Alarm alarm = new Alarm();
                alarm.setAlarmId(Integer.parseInt(cursor.getString(0)));
                alarm.setHour(Integer.parseInt(cursor.getString(1)));
                alarm.setMinute(Integer.parseInt(cursor.getString(2)));
                alarm.setOn(Boolean.parseBoolean(cursor.getString(3)));

                Boolean[] weekDays = new Boolean[7];
                weekDays[0] = Boolean.parseBoolean(cursor.getString(4));
                weekDays[1] = Boolean.parseBoolean(cursor.getString(5));
                weekDays[2] = Boolean.parseBoolean(cursor.getString(6));
                weekDays[3] = Boolean.parseBoolean(cursor.getString(7));
                weekDays[4] = Boolean.parseBoolean(cursor.getString(8));
                weekDays[5] = Boolean.parseBoolean(cursor.getString(9));
                weekDays[6] = Boolean.parseBoolean(cursor.getString(10));

                alarm.setWeekDays(weekDays);

                alarmList.add(alarm);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return alarmList;
    }

    public int getAlarmCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ALARMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    public int updateAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        Boolean[] weekDays = alarm.getWeekDays();

        ContentValues values = new ContentValues();
        values.put(KEY_HOUR, alarm.getHour());
        values.put(KEY_MINUTE, alarm.getMinute());
        values.put(KEY_ON, alarm.getOn().toString());
        values.put(KEY_MON, weekDays[0].toString());
        values.put(KEY_TUE, weekDays[1].toString());
        values.put(KEY_WED, weekDays[2].toString());
        values.put(KEY_THU, weekDays[3].toString());
        values.put(KEY_FRI, weekDays[4].toString());
        values.put(KEY_SAT, weekDays[5].toString());
        values.put(KEY_SUN, weekDays[6].toString());
        int id = db.update(TABLE_ALARMS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(alarm.getAlarmId()) });

        db.close();

        return id;
    }

    public void deleteAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARMS, KEY_ID + " = ?",
                new String[] { String.valueOf(alarm.getAlarmId()) });
        db.close();
    }

    public void dbTest (){
        Boolean[] weekDays = new Boolean[7];
        Arrays.fill(weekDays, true);

        this.addAlarm(new Alarm(10,10,true,weekDays));
        this.addAlarm(new Alarm(12,5,true,weekDays));
        this.addAlarm(new Alarm(6,20,true,weekDays));
        this.addAlarm(new Alarm(21,8,true,weekDays));

        this.printContent();

        List<Alarm> alarms = this.getAllAlarms();
        for (Alarm al : alarms){
            this.deleteAlarm(al);
        }
    }

    public void printContent(){
        List<Alarm> alarms = this.getAllAlarms();

        for(Alarm al : alarms){
            String log = " Hour: " + al.getHour() + ", Minute: " + al.getMinute() + ", Alarm is "
                    + (al.getOn()?"on":"off") + printDays(al.getWeekDays());
            Log.d("Alarm " + al.getAlarmId(), log);
        }

    }

    private String printDays(Boolean[] array){

        String string = "";
        String day = "";

        for(int i = 0; i < array.length; i++){
            switch (i){
                case 0: day = ", Mon: ";
                    break;
                case 1: day = ", Tue: ";
                    break;
                case 2: day = ", Wed: ";
                    break;
                case 3: day = ", Thu: ";
                    break;
                case 4: day = ", Fri: ";
                    break;
                case 5: day = ", Sat: ";
                    break;
                case 6: day = ", Sun: ";
                    break;
            }

            string = string + day + (array[i]?"on":"off");
        }

        return string;
    }
}
