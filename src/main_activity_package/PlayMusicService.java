package main_activity_package;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import app.getup.R;
import app.getup.R.style;

public class PlayMusicService extends Service {
	
	public static MediaPlayer mediaPlayer;
	
	private Vibrator vibrator;
	
	
	private ArrayList<Integer> mon_songList = new ArrayList<Integer>();
	private ArrayList<Integer> tue_songList = new ArrayList<Integer>();
	private ArrayList<Integer> wed_songList = new ArrayList<Integer>();
	private ArrayList<Integer> thu_songList = new ArrayList<Integer>();
	private ArrayList<Integer> fri_songList = new ArrayList<Integer>();
	private ArrayList<Integer> sat_songList = new ArrayList<Integer>();
	private ArrayList<Integer> sun_songList = new ArrayList<Integer>();
	


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		NotificationManager mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		int notifyID = 1;

		NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(getBaseContext())
		.setContentTitle("Get Up!")
		.setOngoing(true)
		.setContentText("Perfect time to RISE AND SHINE!")
		.setSmallIcon(R.drawable.ic_action_notification);
		
		
		
		
		Intent openUnlockActivityIntent = new Intent();
		openUnlockActivityIntent.setClassName("app.getup","main_activity_package.LightUnlockActivity");
		openUnlockActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

	

		PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(),
				0, openUnlockActivityIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);


		mNotifyBuilder.setContentIntent(contentIntent);

		Notification notification = mNotifyBuilder.build();

		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
		
		//Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.song_mon);
		//notification.sound = sound;
		//notification.audioStreamType = AudioManager.STREAM_MUSIC;
		startForeground(notifyID, notification);
		
		playSong();
		
		vibrator = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				long[] vibrate_pattern = {0, 1500, 1500};
				/*while (!MessageActivity.okToStopVibrator){*/
					vibrator.vibrate(vibrate_pattern, 0);
				/*}*/
			}
			
		}).start();
		
		
		//playSong();
		//super.onStartCommand(intent, flags, startId);
		 return START_STICKY_COMPATIBILITY;
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
		//Toast.makeText(getBaseContext(), "service destroyed", Toast.LENGTH_SHORT).show();
		if (mediaPlayer != null){
			mediaPlayer.release();
		}
		
		if (vibrator.hasVibrator()){
			vibrator.cancel();
		}

	}



	private void playSong(){
		addSong();
		
		Calendar c = Calendar.getInstance();
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		
		Integer songName = null;

		switch (dayOfWeek) {
		        case 2: songName = shuffleSong(mon_songList); break;
		        case 3: songName = shuffleSong(tue_songList); break;
		        case 4: songName = shuffleSong(wed_songList); break;
		        case 5: songName = shuffleSong(thu_songList); break;
		        case 6: songName = shuffleSong(fri_songList); break;
		        case 7: songName = shuffleSong(sat_songList); break;
		        case 1: songName = shuffleSong(sun_songList); break; 
		}

	

		if (mediaPlayer == null){

			AudioManager audioManager = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
			
			mediaPlayer = MediaPlayer.create(getBaseContext(), songName);
			mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
			
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setLooping(true);
			mediaPlayer.start();
		}
		else{
			mediaPlayer.start();
		}
		

	}
	
	private void addSong(){
		mon_songList.add(R.raw.song_mon1);
		mon_songList.add(R.raw.song_mon2);
		mon_songList.add(R.raw.song_mon3);
		
		tue_songList.add(R.raw.song_tue1);
		tue_songList.add(R.raw.song_tue2);
		tue_songList.add(R.raw.song_tue3);
		
		wed_songList.add(R.raw.song_wed1);
		wed_songList.add(R.raw.song_wed2);
		wed_songList.add(R.raw.song_wed3);
		
		thu_songList.add(R.raw.song_thu1);
		thu_songList.add(R.raw.song_thu2);
		thu_songList.add(R.raw.song_thu3);
		
		fri_songList.add(R.raw.song_fri1);
		fri_songList.add(R.raw.song_fri2);
		fri_songList.add(R.raw.song_fri3);
		
		sat_songList.add(R.raw.song_sat1);
		sat_songList.add(R.raw.song_sat2);
		sat_songList.add(R.raw.song_sat3);
		
		sun_songList.add(R.raw.song_sun1);
		sun_songList.add(R.raw.song_sun2);
		sun_songList.add(R.raw.song_sun3);
		

	}
	
	private Integer shuffleSong(ArrayList<Integer> songList){
		Random r = new Random();
		int rand = r.nextInt(songList.size()) + 0;
		return songList.get(rand);
		
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}








} 