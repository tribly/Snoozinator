package snoozinc.snoozinator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by heinz on 6/1/16.
 */
public class RingtonePlayingService extends Service {
    private Context context;
    MediaPlayer mediaPlayer;
    private Ringtone ringtone;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        this.ringtone = RingtoneManager.getRingtone(this, alarmUri);
        ringtone.play();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ringtone.stop();
    }
}
