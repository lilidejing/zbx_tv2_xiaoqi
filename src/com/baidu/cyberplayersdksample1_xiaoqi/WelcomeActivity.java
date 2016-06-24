package com.baidu.cyberplayersdksample1_xiaoqi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class WelcomeActivity extends Activity {

	ImageView welcomeImage;
	private static final int SPLASH_DISPLAY_LENGHT=4000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.welcome);
		welcomeImage=(ImageView)this.findViewById(R.id.welcomeImage);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent mainIntent = new Intent(WelcomeActivity.this,
						MainActivity.class);
				WelcomeActivity.this.startActivity(mainIntent);
				WelcomeActivity.this.finish();
			}

		}, SPLASH_DISPLAY_LENGHT);
	}

	
	
}
