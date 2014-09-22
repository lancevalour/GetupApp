package main_activity_package;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;









import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.getup.R;

public class ColorUnlockActivity extends Activity{

	private LinearLayout relative_layout;
	private Button button1, button2, button3, button4, button5, button6, button7, button8, button9;
	private TextView textView;
	private int color1, color2, color3, color4, color5, color6, color7, color8, color9;
	private ArrayList<Button> buttonList = new ArrayList<Button>();
	private ArrayList<Integer> colorList = new ArrayList<Integer>();
	private ArrayList<String> textList = new ArrayList<String>();
	private HashMap<Integer, String> colorNameMap = new HashMap<Integer, String>();

	private int numOfShows = 8;
	private int currentShow;

	private SharedPreferences local_user_information;
	private SharedPreferences.Editor local_user_editor;
	private String PREFS_NAME = "AlarmInfo";



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		setContentView(R.layout.color_unlock_activity_layout);

		setBackGround();


		local_user_information =  this.getSharedPreferences(PREFS_NAME,0);
		currentShow = local_user_information.getInt("current_show", 1);


		initiateComponent();


		setTextContent();


		setButtonColor();


		setButtonAction();

		for (int i = 0; i < buttonList.size(); i++){
			buttonList.get(i).setText("");
		}


		if (currentShow == 1){

			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ColorUnlockActivity.this);
			dialogBuilder.setTitle("Color Pick");

			dialogBuilder.setMessage("Pick the right color as the text shows.");
			dialogBuilder.setCancelable(true);
			dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});

			AlertDialog dialog = dialogBuilder.create();
			dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
			dialog.show();
		}



	}

	private void initiateComponent(){

		button1 = (Button) findViewById(R.id.color_unlock_button1);
		button2 = (Button) findViewById(R.id.color_unlock_button2);
		button3 = (Button) findViewById(R.id.color_unlock_button3);
		button4 = (Button) findViewById(R.id.color_unlock_button4);
		button5 = (Button) findViewById(R.id.color_unlock_button5);
		button6 = (Button) findViewById(R.id.color_unlock_button6);
		button7 = (Button) findViewById(R.id.color_unlock_button7);
		button8 = (Button) findViewById(R.id.color_unlock_button8);
		button9 = (Button) findViewById(R.id.color_unlock_button9);

		textView = (TextView) findViewById(R.id.color_unlock_textView);


		color1 = Color.rgb(111, 176, 127);
		color2 = Color.rgb(252, 176, 60);
		color3 = Color.rgb(161, 35, 70);
		color4 = Color.rgb(140, 140, 140);
		color5 = Color.rgb(240, 120, 140);
		color6 = Color.rgb(255, 29, 35);
		color7 = Color.rgb(150, 75, 0);
		color8 = Color.rgb(242, 106, 27);
		color9 = Color.rgb(122, 163, 204);

		colorNameMap.put(color1, "GREEN");
		colorNameMap.put(color2, "YELLOW");
		colorNameMap.put(color3, "PURPLE");
		colorNameMap.put(color4, "GRAY");
		colorNameMap.put(color5, "PINK");
		colorNameMap.put(color6, "RED");
		colorNameMap.put(color7, "BROWN");
		colorNameMap.put(color8, "ORANGE");
		colorNameMap.put(color9, "BLUE");



		buttonList.add(button1);
		buttonList.add(button2);
		buttonList.add(button3);
		buttonList.add(button4);
		buttonList.add(button5);
		buttonList.add(button6);
		buttonList.add(button7);
		buttonList.add(button8);
		buttonList.add(button9);

		colorList.add(color1);
		colorList.add(color2);
		colorList.add(color3);
		colorList.add(color4);
		colorList.add(color5);
		colorList.add(color6);
		colorList.add(color7);
		colorList.add(color8);
		colorList.add(color9);


		textList.add("1");
		textList.add("2");
		textList.add("3");
		textList.add("4");
		textList.add("5");
		textList.add("6");
		textList.add("7");
		textList.add("8");
		textList.add("9");



	}


	private void setButtonColor(){
		long seed = System.nanoTime();
		Collections.shuffle(buttonList, new Random(seed));
		//Collections.shuffle(colorList, new Random(seed));

		for (int i = 0; i < buttonList.size(); i++){
			buttonList.get(i).setText(colorNameMap.get(colorList.get(i)));
			buttonList.get(i).setBackgroundColor(colorList.get(i));
		}

	}

	/*	private int rand;*/

	private void setTextContent(){
		Random r = new Random();
		int rand = r.nextInt(textList.size()) + 0;

		textView.setText(colorNameMap.get(colorList.get(rand)));

	}

	private void setButtonAction(){
		for (int i = 0; i < buttonList.size(); i++){
			if (buttonList.get(i).getText().equals(textView.getText())){
				buttonList.get(i).setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						if (currentShow != numOfShows){

							local_user_editor = local_user_information.edit();
							local_user_editor.putInt("current_show", currentShow + 1);
							local_user_editor.commit();


							Intent intent = new Intent(getBaseContext(), ColorUnlockActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
							finish();
						}
						else{


							local_user_editor = local_user_information.edit();
							local_user_editor.remove("current_show");
							local_user_editor.commit();


							Intent intent = new Intent("main_activity_package.MESSAGEACTIVITY");
							startActivity(intent);
							overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
							finish();
						}


					}
				});
			}
		}
	}


	public KeyguardManager keyguardManager;
	public KeyguardLock lock;

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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		/*	Intent intent = new Intent(getBaseContext(), LightUnlockActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);*/
		/*lock.reenableKeyguard();*/

	}

	private void setBackGround(){
		relative_layout = (LinearLayout) findViewById(R.id.color_unlock_LinearLayout);
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
}
