package main_activity_package;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import app.getup.R;


public class StartingActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		setContentView(R.layout.starting_activity_layout);

		 Handler handler = new Handler(); 
		    handler.postDelayed(new Runnable() { 
		         public void run() { 
		        	 Intent main_view_intent = new Intent("main_activity_package.SETALARMACTIVITY");
		        	 overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
						startActivity(main_view_intent);
		         } 
		    }, 1500); 
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}
