package main_activity_package;




import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;




import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import app.getup.R;

public class AlarmReceiver extends WakefulBroadcastReceiver{

	private SensorManager mSensorManager;
	private Sensor sensor;

	private Vibrator vibrator;

	public static PowerManager.WakeLock wakeLock;

	private ArrayList<Integer> songList = new ArrayList<Integer>();

	public static MediaPlayer mediaPlayer;

	private Context baseContext;

	public static KeyguardManager.KeyguardLock kl;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		baseContext = context;

		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);



		context.startService(new Intent(context, PlayMusicService.class));



	/*	KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE); 
		kl = km .newKeyguardLock("MyKeyguardLock"); 
		kl.disableKeyguard(); */


		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
		wakeLock.acquire();
		

		Intent openUnlockActivityIntent = new Intent();
		openUnlockActivityIntent.setClassName("app.getup","main_activity_package.LightUnlockActivity");
		openUnlockActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		context.startActivity(openUnlockActivityIntent);

	}



	private void playSong(){

		//songList.add(R.raw.song_mon);


		/*Calendar c = Calendar.getInstance();
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		switch (dayOfWeek) {
		        case 1: relative_layout.setBackgroundColor(Color.rgb(255, 95, 80));break;
		        case 2: relative_layout.setBackgroundColor(Color.rgb(182, 209, 93));break;
		        case 3: relative_layout.setBackgroundColor(Color.rgb(127, 156, 176));break;
		        case 4: relative_layout.setBackgroundColor(Color.rgb(121, 189, 143));break;
		        case 5: relative_layout.setBackgroundColor(Color.rgb(255, 151, 79));break;
		        case 6: relative_layout.setBackgroundColor(Color.rgb(251, 151, 133));break;
		        case 7: relative_layout.setBackgroundColor(Color.rgb(255, 176, 59));break; 
		}

		 */


		Random r = new Random();
		int rand = r.nextInt(songList.size()) + 0;


		AudioManager audioManager = (AudioManager) baseContext.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);


		mediaPlayer = MediaPlayer.create(baseContext, songList.get(rand));

		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		//mediaPlayer.setWakeMode(baseContext, PowerManager.PARTIAL_WAKE_LOCK);

		//final float volume = (float) (1 - (Math.log(100 - 100) / Math.log(100)));
		//mediaPlayer.setVolume(volume, volume);
		mediaPlayer.start();
	}








}
