package main_activity_package;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

	private SharedPreferences local_user_information;
	private SharedPreferences.Editor local_user_editor;
	private String PREFS_NAME = "AlarmInfo";

	private Set<String> ringtoneSet;

	private ArrayList<String> mon_songList = new ArrayList<String>();
	private ArrayList<String> tue_songList = new ArrayList<String>();
	private ArrayList<String> wed_songList = new ArrayList<String>();
	private ArrayList<String> thu_songList = new ArrayList<String>();
	private ArrayList<String> fri_songList = new ArrayList<String>();
	private ArrayList<String> sat_songList = new ArrayList<String>();
	private ArrayList<String> sun_songList = new ArrayList<String>();

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		local_user_information = this.getSharedPreferences(PREFS_NAME, 0);

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		int notifyID = 1;

		NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
				getBaseContext()).setContentTitle("Get Up!").setOngoing(true)
				.setContentText("Perfect time to RISE AND SHINE!")
				.setSmallIcon(R.drawable.ic_action_notification);

		Intent openUnlockActivityIntent = new Intent();
		openUnlockActivityIntent.setClassName("app.getup",
				"main_activity_package.LightUnlockActivity");
		openUnlockActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent contentIntent = PendingIntent.getActivity(
				getBaseContext(), 0, openUnlockActivityIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		mNotifyBuilder.setContentIntent(contentIntent);

		Notification notification = mNotifyBuilder.build();

		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
				audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

		// Uri sound = Uri.parse("android.resource://" + getPackageName() + "/"
		// + R.raw.song_mon);
		// notification.sound = sound;
		// notification.audioStreamType = AudioManager.STREAM_MUSIC;
		startForeground(notifyID, notification);

		try {
			playSong();
		}
		catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		vibrator = (Vibrator) getBaseContext().getSystemService(
				Context.VIBRATOR_SERVICE);

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				long[] vibrate_pattern = { 0, 1500, 1500 };
				/* while (!MessageActivity.okToStopVibrator){ */
				vibrator.vibrate(vibrate_pattern, 0);
				/* } */
			}

		}).start();

		// playSong();
		// super.onStartCommand(intent, flags, startId);
		return START_STICKY_COMPATIBILITY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
		// Toast.makeText(getBaseContext(), "service destroyed",
		// Toast.LENGTH_SHORT).show();
		if (mediaPlayer != null) {
			mediaPlayer.release();
		}

		if (vibrator.hasVibrator()) {
			vibrator.cancel();
		}

	}

	private void playSong() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
		ringtoneSet = local_user_information.getStringSet("ringtone", null);
		if (ringtoneSet == null) {
			addDefaultSong();
		}
		else {
			addSong();
		}

		Calendar c = Calendar.getInstance();
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		String songUri = null;

		switch (dayOfWeek) {
		case 2:
			songUri = shuffleSong(mon_songList);
			break;
		case 3:
			songUri = shuffleSong(tue_songList);
			break;
		case 4:
			songUri = shuffleSong(wed_songList);
			break;
		case 5:
			songUri = shuffleSong(thu_songList);
			break;
		case 6:
			songUri = shuffleSong(fri_songList);
			break;
		case 7:
			songUri = shuffleSong(sat_songList);
			break;
		case 1:
			songUri = shuffleSong(sun_songList);
			break;
		}

		if (mediaPlayer == null) {

			AudioManager audioManager = (AudioManager) getBaseContext()
					.getSystemService(Context.AUDIO_SERVICE);
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
					audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
					0);

			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(songUri));
			mediaPlayer.setWakeMode(getApplicationContext(),
					PowerManager.PARTIAL_WAKE_LOCK);

			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setLooping(true);
			mediaPlayer.start();
		}
		else {
			mediaPlayer.start();
		}

	}
	
	private final String rawPath = "android.resource://main_activity_package//" + R.raw.song_fri1;

	private void addDefaultSong() {
		mon_songList.add(rawPath);

		tue_songList.add(rawPath);

		wed_songList.add(rawPath);

		thu_songList.add(rawPath);

		fri_songList.add(rawPath);

		sat_songList.add(rawPath);

		sun_songList.add(rawPath);
	}

	private void addSong() {
		String[] rongtoneArray = ringtoneSet.toArray(new String[0]);
		mon_songList.add(rongtoneArray[0]);

		tue_songList.add(rongtoneArray[1]);

		wed_songList.add(rongtoneArray[2]);

		thu_songList.add(rongtoneArray[3]);

		fri_songList.add(rongtoneArray[4]);

		sat_songList.add(rongtoneArray[5]);

		sun_songList.add(rongtoneArray[6]);

	}

	private String shuffleSong(ArrayList<String> songList) {
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