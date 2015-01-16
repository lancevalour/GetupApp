package main_activity_package;



import java.util.ArrayList;





import java.util.Calendar;
import java.util.Random;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.getup.R;

public class LightUnlockActivity extends Activity{

	private RelativeLayout relative_layout;
	private Button stop_alarm_button;
	private TextView light_level_textView, remind_textView;

	private SensorManager mSensorManager;
	private SensorEventListener sensorEventListener;

	private ArrayList<Integer> songList = new ArrayList<Integer>();

	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;
	public static MediaPlayer mediaPlayer;
	private Vibrator vibrator;

	private float currentLightLevel;

	private long startTime;
	private long endTime;

	public static KeyguardManager.KeyguardLock lock;
	public static PowerManager.WakeLock wakeLock;
	public KeyguardManager keyguardManager;
	
	private int lightHoldTime = 25;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		setContentView(R.layout.light_unlock_activity_layout);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

	/*	if (!isMyServiceRunning(PlayMusicService.class)){
			getBaseContext().startService(new Intent(getBaseContext(), PlayMusicService.class));
		}*/
		
		
		

		setBackGround();

		

		PowerManager pm = (PowerManager) getBaseContext().getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
		wakeLock.acquire();
		 

		stop_alarm_button = (Button) findViewById(R.id.stop_alarm_button);

		light_level_textView = (TextView) findViewById(R.id.light_level_textView);
		remind_textView = (TextView) findViewById(R.id.remind_textView);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		vibrator = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);

		//playSong();

		//playSong();






		/*	runOnUiThread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub

						long[] vibrate_pattern = {0, 100, 1000};
						while (true){
							vibrator.vibrate(vibrate_pattern, -1);
						}
					}

				}).start();
			}

		});*/



		startTime = System.currentTimeMillis();





		SensorEventListener sensorEventListener = new SensorEventListener(){

			@Override
			public void onSensorChanged(SensorEvent event) {
				// TODO Auto-generated method stub
				currentLightLevel = event.values[0];
				
				//light_level_textView.setText(String.valueOf(currentLightLevel));
				light_level_textView.setAlpha((float) currentLightLevel/1000);
				
				
				if (currentLightLevel < 300){
					startTime = System.currentTimeMillis();
				}

			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub

			}

		};

		mSensorManager.registerListener(sensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);

		stop_alarm_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Context.NOTIFICATION_SERVICE != null) {

					if (System.currentTimeMillis() - startTime > lightHoldTime * 1000){

						Intent intent = new Intent(getBaseContext(), ColorUnlockActivity.class);
						overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
						startActivity(intent);
						
						
						finish();
					}
					else{
						remind_textView.setText("Please turn on the light and face your phone to the light for a period of time to cancel the alarm!");
					}
				}
			}
		});








		/*	alarmMgr = (AlarmManager)getBaseContext().getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
		alarmIntent = PendingIntent.getBroadcast(getBaseContext(), 0, intent, 0);

		// Set the alarm to start at 8:30 a.m.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, 8);
		calendar.set(Calendar.MINUTE, 55);

		// setRepeating() lets you specify a precise custom interval--in this case,
		// 20 minutes.
		alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
				1000 * 60 * 1, alarmIntent);*/

		/*ContentResolver contentResolver = getContentResolver();
		Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor cursor = contentResolver.query(uri, null, null, null, null);
		if (cursor == null) {
		    // query failed, handle error.
		} else if (!cursor.moveToFirst()) {
		    // no media on the device
		} else {
		    int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
		    int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
		    do {
		       long thisId = cursor.getLong(idColumn);
		       String thisTitle = cursor.getString(titleColumn);
		       // ...process entry...
		       songList.add(thisTitle);
		       System.out.println(songList);
		    } while (cursor.moveToNext());
		}*/
		/*MediaPlayer mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.song);
		mediaPlayer.start();*/



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


		AudioManager audioManager = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);


		mediaPlayer = MediaPlayer.create(getBaseContext(), songList.get(rand));

		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		mediaPlayer.setLooping(true);
		//final float volume = (float) (1 - (Math.log(100 - 100) / Math.log(100)));
		//mediaPlayer.setVolume(volume, volume);
		mediaPlayer.start();
	}




		private void stopSong(){
		MediaPlayer mediaplayer = PlayMusicService.mediaPlayer;

		if (!mediaplayer.equals(null) &&  mediaplayer.isPlaying()){
			mediaplayer.release();
		}

	}


	@Override
	protected void onResume() {
		// Register a listener for the sensor.
		super.onResume();
		//mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		// Be sure to unregister the sensor when the activity pauses.
		super.onPause();
		mSensorManager.unregisterListener(sensorEventListener);
	}


		@Override
	public void onAttachedToWindow()
	{  
		keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
		lock.disableKeyguard();
	}
	 


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_HOME ){
			return false;
		}
		return true;
	}




	private void setBackGround(){
		relative_layout = (RelativeLayout) findViewById(R.id.light_unlock_relativeLayout);
		Calendar c = Calendar.getInstance();
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		switch (dayOfWeek) {
		case 2: relative_layout.setBackgroundColor(Color.rgb(255, 95, 80));break;
		case 3: relative_layout.setBackgroundColor(Color.rgb(182, 209, 93));break;
		case 4: relative_layout.setBackgroundColor(Color.rgb(127, 156, 176));break;
		case 5: relative_layout.setBackgroundColor(Color.rgb(121, 189, 143));break;
		case 6: relative_layout.setBackgroundColor(Color.rgb(255, 151, 79));break;
		case 7: relative_layout.setBackgroundColor(Color.rgb(251, 151, 133));break;
		case 1: relative_layout.setBackgroundColor(Color.rgb(255, 176, 59));break; 
		}
	}


	private boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//stopSong();
		lock.reenableKeyguard();
		wakeLock.release();
	}
	
	
	
}
