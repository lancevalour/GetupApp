package main_activity_package;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.getup.R;

public class MessageActivity extends Activity{
	private TextView message_textView;
	private RelativeLayout relative_layout;
	private ArrayList<String> messageList = new ArrayList<String>(); 

	private Button wake_button;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		setContentView(R.layout.message_activity_layout);


		getBaseContext().stopService(new Intent(getBaseContext(), PlayMusicService.class));
		//AlarmReceiver.kl.reenableKeyguard();
		//	AlarmReceiver.wakeLock.release();

		setBackGround();

		setMessage();

		wake_button = (Button) findViewById(R.id.wake_button);

		wake_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animation animationFadeIn = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in);
				message_textView.startAnimation(animationFadeIn);
				message_textView.setText("Get your dream up.");
				
				Handler handler = new Handler(); 
			    handler.postDelayed(new Runnable() { 
			         public void run() { 
			        	finish();
			         } 
			    }, 5000); 
				
				
			}
		});



		NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		nMgr.cancel(1);


		/*vibrator.cancel();*/

		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

	}

	private void setMessage(){
		message_textView = (TextView) findViewById(R.id.message_layout_textView);


		messageList.add("Two roads diverged in a wood, and I¡ªI took the one less traveled by, and that has made all the difference.");
		messageList.add("I attribute my success to this: I never gave or took any excuse.");
		messageList.add("Your time is limited, so don¡¯t waste it living someone else¡¯s life.");
		messageList.add("The mind is everything. What you think you become.");
		messageList.add("The best time to plant a tree was 20 years ago. The second best time is now.");
		messageList.add("Twenty years from now you will be more disappointed by the things that you didn¡¯t do than by the ones you did do, so throw off the bowlines, sail away from safe harbor, catch the trade winds in your sails.  Explore, Dream, Discover. ");
		messageList.add("Either you run the day, or the day runs you.");
		messageList.add("Go confidently in the direction of your dreams. Live the life you have imagined.");
		messageList.add("We can easily forgive a child who is afraid of the dark; the real tragedy of life is when men are afraid of the light.");
		messageList.add("The best revenge is massive success.");
		messageList.add("Your time is limited, so don¡¯t waste it living someone else¡¯s life.");
		messageList.add("Whatever the mind of man can conceive and believe, it can achieve.");
		messageList.add("Don't count each day, make each day count.");
		messageList.add("You can¡¯t fall if you don¡¯t climb. But there¡¯s no joy in living your whole life on the ground.");
		messageList.add("I would rather die of passion than of boredom.");
		messageList.add("Knowing is not enough; we must apply. Being willing is not enough; we must do.");
		messageList.add("You can't give this world to those you look down to.");


		Random r = new Random();
		int rand = r.nextInt(messageList.size()) + 0;

		message_textView.setText(messageList.get(rand));


	}




	private void setBackGround(){
		relative_layout = (RelativeLayout) findViewById(R.id.message_RelativeLayout);
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		/*
		Intent intent = new Intent(getBaseContext(), MessageActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);*/

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		AlarmReceiver.wakeLock.release();
		LightUnlockActivity.wakeLock.release();
	}



	/*	@Override
	public void onAttachedToWindow()
	{  
		KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
		lock.disableKeyguard();
	}*/



}
