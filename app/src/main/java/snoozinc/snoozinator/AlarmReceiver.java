package snoozinc.snoozinator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

/**
 * Created by heinz on 5/31/16.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Im running", Toast.LENGTH_SHORT).show();


        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);

        context.startService(serviceIntent);
    }
}
