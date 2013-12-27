package fhfl.mogensen.ball;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Customize extends Activity implements OnItemSelectedListener
{
int[] colours;
int[] speed;
Spinner circleColourPicker;
Spinner finishColourPicker;
Spinner backgroundColourPicker;
Spinner speedPicker;
Spinner livesPicker;
static int circlePickedColour;
static int finishPickedColour;
static int backgroundPickedColour;
static int pickedSpeed;
static int pickedLives;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);

	    //Load Main Menu
	    setContentView(R.layout.settings);
	    
	    //Reference UI-elements
	    circleColourPicker = (Spinner) findViewById(R.id.circleColourPicker);
	    finishColourPicker = (Spinner) findViewById(R.id.finishColourPicker);
	    backgroundColourPicker = (Spinner) findViewById(R.id.backgroundColourPicker);
	    speedPicker = (Spinner) findViewById(R.id.speedPicker);
	    livesPicker = (Spinner) findViewById(R.id.livesPicker);
	    circleColourPicker.setOnItemSelectedListener(this);
	    finishColourPicker.setOnItemSelectedListener(this);
	    backgroundColourPicker.setOnItemSelectedListener(this);
	    speedPicker.setOnItemSelectedListener(this);
	    livesPicker.setOnItemSelectedListener(this);
	        
	    //Create an ArrayAdapter using the string array and a default spinner layout
	    ArrayAdapter<CharSequence> coloursAdapter = ArrayAdapter.createFromResource(this, R.array.colours_array, android.R.layout.simple_spinner_item);
	    ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(this, R.array.speed_array, android.R.layout.simple_spinner_item);
	    ArrayAdapter<CharSequence> livesAdapter = ArrayAdapter.createFromResource(this, R.array.lives_array, android.R.layout.simple_spinner_item);
	    //Specify the layout to use when the list of choices appears
	    coloursAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    livesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    // Apply the adapter to the spinner
	    circleColourPicker.setAdapter(coloursAdapter);
	    finishColourPicker.setAdapter(coloursAdapter);
	    backgroundColourPicker.setAdapter(coloursAdapter);
	    speedPicker.setAdapter(difficultyAdapter);
	    livesPicker.setAdapter(livesAdapter);
	    livesPicker.setSelection(2);
	    speedPicker.setSelection(0);
	    circleColourPicker.setSelection(5);
	    finishColourPicker.setSelection(3);
	    backgroundColourPicker.setSelection(7);
        colours = new int[] {0xff000000, 0xff0000ff, 0xff00ffff, 0xff00ff00, 0xffff00ff,
        		0xffff0000, 0xffffff00, 0x00000000};
        speed = new int[] {1, 2, 3, 4, 5};
	}

    @Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id)
    	{
        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.circleColourPicker)
        {
        	circlePickedColour = colours[pos];                
        }
        if(spinner.getId() == R.id.finishColourPicker)
        {
          	finishPickedColour = colours[pos];
        }
        if(spinner.getId() == R.id.backgroundColourPicker)
        {
        	backgroundPickedColour = colours[pos];
        	if (backgroundPickedColour == circlePickedColour || backgroundPickedColour == finishPickedColour)
        	{
        		backgroundColourPicker.setSelection(7);
        		Toast.makeText(getApplicationContext(), "Background Colour can not be the same as circle or finish colour. Defaulted.", Toast.LENGTH_LONG).show();
        	}
        }
        if(spinner.getId() == R.id.speedPicker)
        {
        	pickedSpeed = speed[pos];
        }
        if(spinner.getId() == R.id.livesPicker)
        {
        	pickedLives = speed[pos];
        }
        }

    @Override
	public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
