package main_activity_package;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import app.getup.R;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SetAlarmActivity extends Activity {
	private RelativeLayout relative_layout;

	private Button set_alarm_button, cancel_alarm_button, setting_button;
	private ToggleButton on_off_toggleButton;
	private TextView alarm_time_textView;
	private TimePicker time_picker;

	private AlarmManager alarm_manager;
	private PendingIntent alarm_pendingIntent;

	private int alarm_hour;
	private int alarm_min;

	private boolean alarm_is_on;

	private SharedPreferences local_user_information;
	private SharedPreferences.Editor local_user_editor;
	private String PREFS_NAME = "AlarmInfo";

	private List<String> ringtoneList;
	private Integer defaultRingTone;
	private boolean isAmPm;

	private final String rawPath = "android.resource://main_activity_package//"
			+ R.raw.song_fri1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		setContentView(R.layout.set_alarm_activity_layout);

		setBackGround();

		local_user_information = this.getSharedPreferences(PREFS_NAME, 0);
		local_user_editor = local_user_information.edit();
		if (local_user_information.getString("ringtone_day1", "default").equals(
				"default")) {
			for (int i = 1; i <= 7; i++) {
				local_user_editor.putString("ringtone_day" + i, rawPath);
				local_user_editor.commit();
			}

		}

		alarm_hour = local_user_information.getInt("alarm_hour", 100);
		alarm_min = local_user_information.getInt("alarm_min", 100);
		alarm_is_on = local_user_information.getBoolean("alarm_is_on", true);
		isAmPm = local_user_information.getBoolean("isAmPm", true);

		set_alarm_button = (Button) findViewById(R.id.set_alarm_button);
		cancel_alarm_button = (Button) findViewById(R.id.cancel_alarm_button);
		setting_button = (Button) findViewById(R.id.setting_button);
		on_off_toggleButton = (ToggleButton) findViewById(R.id.on_off_toggleButton);
		alarm_time_textView = (TextView) findViewById(R.id.alarm_time_textView);

		if (alarm_hour == 100) {
			cancel_alarm_button.setVisibility(View.INVISIBLE);
			on_off_toggleButton.setClickable(false);
			set_alarm_button.setVisibility(View.VISIBLE);
		}
		else {
			cancel_alarm_button.setVisibility(View.VISIBLE);
			on_off_toggleButton.setClickable(true);
			set_alarm_button.setVisibility(View.INVISIBLE);
		}

		displayAlarmTime();

		set_alarm_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
						SetAlarmActivity.this);
				LayoutInflater factory = LayoutInflater
						.from(SetAlarmActivity.this);
				View deleteDialogView = factory.inflate(
						R.layout.time_picker_dialog_layout, null);
				dialogBuilder.setView(deleteDialogView);

				time_picker = (TimePicker) deleteDialogView
						.findViewById(R.id.time_picker);

				dialogBuilder.setCancelable(true);
				dialogBuilder.setTitle("Set Alarm Time");
				dialogBuilder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								String alarm_time = timeToString(
										time_picker.getCurrentHour(),
										time_picker.getCurrentMinute(), isAmPm);

								Animation fadeIn = new AlphaAnimation(0, 1);
								fadeIn.setInterpolator(new DecelerateInterpolator());
								fadeIn.setDuration(1000);
								alarm_time_textView.setAnimation(fadeIn);
								alarm_time_textView.setText(alarm_time);

								local_user_editor = local_user_information
										.edit();

								local_user_editor.putInt("alarm_hour",
										time_picker.getCurrentHour());
								local_user_editor.putInt("alarm_min",
										time_picker.getCurrentMinute());

								local_user_editor.commit();

								setUpAlarmClock(time_picker.getCurrentHour(),
										time_picker.getCurrentMinute(), true);

								cancel_alarm_button.setVisibility(View.VISIBLE);
								on_off_toggleButton.setVisibility(View.VISIBLE);
								set_alarm_button.setVisibility(View.INVISIBLE);
							}

						});

				dialogBuilder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});

				AlertDialog dialog = dialogBuilder.create();
				dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
				dialog.show();

			}
		});

		cancel_alarm_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
						SetAlarmActivity.this);
				dialogBuilder.setTitle("Cancel Alarm");
				dialogBuilder.setMessage("Do you want to cancel this alarm?");
				dialogBuilder.setCancelable(true);
				dialogBuilder.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								cancelAlarmClock(0);

								local_user_editor = local_user_information
										.edit();

								local_user_editor.putInt("alarm_hour", 100);
								local_user_editor.putInt("alarm_min", 100);

								local_user_editor.commit();

								Animation fadeIn = new AlphaAnimation(0, 1);
								fadeIn.setInterpolator(new DecelerateInterpolator());
								fadeIn.setDuration(1000);
								alarm_time_textView.setAnimation(fadeIn);

								alarm_time_textView.setText("Set an alarm!");
								cancel_alarm_button
										.setVisibility(View.INVISIBLE);
								on_off_toggleButton
										.setVisibility(View.INVISIBLE);
								set_alarm_button.setVisibility(View.VISIBLE);
								alarm_time_textView.setAlpha((float) 1);
								on_off_toggleButton.setChecked(true);

							}
						});

				dialogBuilder.setNegativeButton("No",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.cancel();
							}
						});

				AlertDialog dialog = dialogBuilder.create();
				dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
				dialog.show();

			}
		});

		setting_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SetAlarmActivity.this);
				builder.setTitle("Choose Music")
						.setItems(
								new CharSequence[] { "Monday", "Tuesday",
										"Wednesday", "Thursday", "Friday",
										"Saturday", "Sunday", "Cancel" },
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										if (which == 7) {
											dialog.dismiss();
										}
										else {
											Intent intent = new Intent();
											intent.setAction(
													Intent.ACTION_GET_CONTENT)
													.setType("audio/*");

											startActivityForResult(
													Intent.createChooser(
															intent,
															"Choose sound file"),
													6);
											dayIndex = which;

										}
									}
								}).create().show();
			}
		});

		on_off_toggleButton
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							alarm_hour = local_user_information.getInt(
									"alarm_hour", 100);
							alarm_min = local_user_information.getInt(
									"alarm_min", 100);
							if (alarm_hour != 100) {

								setUpAlarmClock(alarm_hour, alarm_min, false);

								Animation animationFadeIn = AnimationUtils
										.loadAnimation(getBaseContext(),
												R.anim.fade_in);
								alarm_time_textView
										.startAnimation(animationFadeIn);
								alarm_time_textView.setAlpha((float) 1);

								local_user_editor = local_user_information
										.edit();
								local_user_editor.putBoolean("alarm_is_on",
										true);
								local_user_editor.commit();
							}

							// alarm_time_textView.setText("alarm open");
						}
						else {
							PendingIntent pendingIntent = cancelAlarmClock(0);

							if (pendingIntent != null) {

								Animation animationFadeIn = AnimationUtils
										.loadAnimation(getBaseContext(),
												R.anim.fade_in);
								alarm_time_textView
										.startAnimation(animationFadeIn);
								alarm_time_textView.setAlpha((float) 0.4);

								local_user_editor = local_user_information
										.edit();
								local_user_editor.putBoolean("alarm_is_on",
										false);
								local_user_editor.commit();
							}

						}
					}

				});

		if (alarm_is_on) {
			on_off_toggleButton.setChecked(true);
		}
		else {
			on_off_toggleButton.setChecked(false);
		}

		alarm_time_textView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isAmPm) {
					isAmPm = false;
					local_user_editor = local_user_information.edit();
					local_user_editor.putBoolean("isAmPm", false);
					local_user_editor.commit();
					alarm_hour = local_user_information.getInt("alarm_hour",
							100);
					alarm_min = local_user_information.getInt("alarm_min", 100);
					Animation animationFadeIn = AnimationUtils.loadAnimation(
							getBaseContext(), R.anim.fade_in);
					alarm_time_textView.startAnimation(animationFadeIn);
					alarm_time_textView.setText(timeToString(alarm_hour,
							alarm_min, isAmPm));
				}
				else {
					isAmPm = true;
					local_user_editor = local_user_information.edit();
					local_user_editor.putBoolean("isAmPm", true);
					local_user_editor.commit();
					alarm_hour = local_user_information.getInt("alarm_hour",
							100);
					alarm_min = local_user_information.getInt("alarm_min", 100);
					Animation animationFadeIn = AnimationUtils.loadAnimation(
							getBaseContext(), R.anim.fade_in);
					alarm_time_textView.startAnimation(animationFadeIn);
					alarm_time_textView.setText(timeToString(alarm_hour,
							alarm_min, isAmPm));
				}
			}
		});

	}

	private int dayIndex;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK && requestCode == 6) {
			Uri i = data.getData(); // getDATA
			String s = i.getPath(); // getPath
			File k = new File(s); // set File from path

			if (s != null) { // (file.exists

				ContentValues values = new ContentValues();
				values.put(MediaStore.MediaColumns.DATA, k.getAbsolutePath());
				values.put(MediaStore.MediaColumns.TITLE, "ring");
				values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
				values.put(MediaStore.MediaColumns.SIZE, k.length());
				values.put(MediaStore.Audio.Media.ARTIST, R.string.app_name);
				values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
				values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
				values.put(MediaStore.Audio.Media.IS_ALARM, true);
				values.put(MediaStore.Audio.Media.IS_MUSIC, false);

				Uri uri = MediaStore.Audio.Media.getContentUriForPath(k
						.getAbsolutePath());
				getContentResolver().delete(
						uri,
						MediaStore.MediaColumns.DATA + "=\""
								+ k.getAbsolutePath() + "\"", null);
				Uri newUri = getContentResolver().insert(uri, values);

				local_user_editor.putString("ringtone_day" + dayIndex + 1, newUri.toString());
				local_user_editor.commit();
				/*
				 * try { RingtoneManager.setActualDefaultRingtoneUri(
				 * SetAlarmActivity.this, RingtoneManager.TYPE_RINGTONE,
				 * newUri); } catch (Throwable t) {
				 * 
				 * }
				 */
			}
		}
	}

	private Date nowTime, alarmTime;

	private long calculateTriggerTime(int hour, int min) {
		Calendar nowCalendar = Calendar.getInstance();
		nowTime = new Date(nowCalendar.get(Calendar.YEAR) - 1900,
				nowCalendar.get(Calendar.MONTH),
				nowCalendar.get(Calendar.DAY_OF_MONTH),
				nowCalendar.get(Calendar.HOUR_OF_DAY),
				nowCalendar.get(Calendar.MINUTE),
				nowCalendar.get(Calendar.SECOND));
		alarmTime = new Date(nowCalendar.get(Calendar.YEAR) - 1900,
				nowCalendar.get(Calendar.MONTH),
				nowCalendar.get(Calendar.DAY_OF_MONTH), hour, min,
				nowCalendar.get(Calendar.SECOND));

		if (alarmTime.after(nowTime)) {
			return alarmTime.getTime() - nowTime.getTime();
		}
		else {
			alarmTime = new Date(nowCalendar.get(Calendar.YEAR) - 1900,
					nowCalendar.get(Calendar.MONTH),
					nowCalendar.get(Calendar.DAY_OF_MONTH) + 1, hour, min,
					nowCalendar.get(Calendar.SECOND));

			return alarmTime.getTime() - nowTime.getTime();
		}

	}

	private void setUpAlarmClock(int hour, int min, boolean isSetup) {

		alarm_manager = (AlarmManager) getBaseContext().getSystemService(
				Context.ALARM_SERVICE);
		Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
		alarm_pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0,
				intent, 0);

		long triggerTime = calculateTriggerTime(hour, min);
		/*
		 * Calendar calendar = Calendar.getInstance();
		 * calendar.setTimeInMillis(System.currentTimeMillis());
		 * calendar.set(Calendar.HOUR_OF_DAY, hour);
		 * calendar.set(Calendar.MINUTE, min);
		 */

		int triggerHour = (int) (triggerTime) / 1000 / 60 / 60;

		int triggerMin = (int) (triggerTime) % (1000 * 60 * 60) / 1000 / 60;

		String triggerTimeString = "Alarm starts " + triggerHour + " hours "
				+ triggerMin + " mins " + "from now.";

		if (isSetup) {

			Toast.makeText(getBaseContext(), triggerTimeString,
					Toast.LENGTH_LONG).show();

		}

		alarm_manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime() + triggerTime,
				AlarmManager.INTERVAL_DAY, alarm_pendingIntent);

	}

	private PendingIntent cancelAlarmClock(int alarmID) {
		Intent myIntent = new Intent(getBaseContext(), AlarmReceiver.class);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getBaseContext(), alarmID, myIntent, 0);

		if (pendingIntent != null) {
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			alarmManager.cancel(pendingIntent);
		}

		return pendingIntent;

	}

	private void displayAlarmTime() {
		if (alarm_hour == 100) {
			alarm_time_textView.setText("Set an alarm!");
		}
		else {
			alarm_time_textView.setText(timeToString(alarm_hour, alarm_min,
					isAmPm));
		}

	}

	private String timeToString(int hour, int min, boolean isAmPm) {

		Calendar nowCalendar = Calendar.getInstance();
		alarmTime = new Date(nowCalendar.get(Calendar.YEAR) - 1900,
				nowCalendar.get(Calendar.MONTH),
				nowCalendar.get(Calendar.DAY_OF_MONTH) + 1, hour, min,
				nowCalendar.get(Calendar.SECOND));

		if (isAmPm) {
			DateFormat dateFormat = new SimpleDateFormat("h:mm a");
			return dateFormat.format(alarmTime.getTime());
		}
		else {
			DateFormat dateFormat = new SimpleDateFormat("H:mm");
			return dateFormat.format(alarmTime.getTime());
		}

	}

	private void setBackGround() {
		relative_layout = (RelativeLayout) findViewById(R.id.set_alarm_relativeLayout);
		Calendar c = Calendar.getInstance();
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		switch (dayOfWeek) {
		case 2:
			relative_layout.setBackgroundColor(Color.rgb(255, 95, 80));
			break;
		case 3:
			relative_layout.setBackgroundColor(Color.rgb(182, 209, 93));
			break;
		case 4:
			relative_layout.setBackgroundColor(Color.rgb(127, 156, 176));
			break;
		case 5:
			relative_layout.setBackgroundColor(Color.rgb(121, 189, 143));
			break;
		case 6:
			relative_layout.setBackgroundColor(Color.rgb(255, 151, 79));
			break;
		case 7:
			relative_layout.setBackgroundColor(Color.rgb(251, 151, 133));
			break;
		case 1:
			relative_layout.setBackgroundColor(Color.rgb(255, 176, 59));
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		moveTaskToBack(true);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// preventing default implementation previous to
			// android.os.Build.VERSION_CODES.ECLAIR
			moveTaskToBack(true);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
