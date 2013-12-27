package fhfl.mogensen.ball;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenu extends Activity
{
	Button start, settings;

	static boolean bRunning = false;
	static boolean bSettings = false;
	String TAG = "fhfl";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);

	    //Load Main Menu
	    setContentView(R.layout.activity_main);
	    //Reference UI-Elements
	    start = (Button) findViewById(R.id.start);
	    settings = (Button) findViewById(R.id.settings);
	    
	    //Start-listener
	    start.setOnClickListener(new OnClickListener()
		{ 
			@Override
			public void onClick(View v)
			{   
				Log.d(TAG,"onClick(): Start");
				Intent startGame = new Intent(v.getContext(), MainActivity.class);
				startActivity(startGame);
				bRunning = true;
				start.setText("Resume Game");
			}
				
		});
	    
	    //Settings-listener
	    settings.setOnClickListener(new OnClickListener()
		{ 
			@Override
			public void onClick(View v)
			{   
				Log.d(TAG,"onClick(): Settings");
				Intent customize = new Intent(v.getContext(), Customize.class);
				bSettings = true;
				startActivity(customize);
			}
				
		});
	}
}
