package fhfl.mogensen.ball;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Customize extends Activity implements OnItemSelectedListener
{
public static int[] colours;
static Spinner circleColourPicker;
static Spinner finishColourPicker;
static int circlePickedColour;
static int finishPickedColour;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);

	    //Load Main Menu
	    setContentView(R.layout.settings);
	    
	    //Reference UI-elements
	    circleColourPicker = (Spinner) findViewById(R.id.circleColourPicker);
	    finishColourPicker = (Spinner) findViewById(R.id.finishColourPicker);
	    circleColourPicker.setOnItemSelectedListener(this);
	    finishColourPicker.setOnItemSelectedListener(this);
	        
	    //Create an ArrayAdapter using the string array and a default spinner layout
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.colours_array, android.R.layout.simple_spinner_item);
	    //Specify the layout to use when the list of choices appears
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    // Apply the adapter to the spinner
	    circleColourPicker.setAdapter(adapter);
	    finishColourPicker.setAdapter(adapter);
        colours = new int[] {0xff000000, 0xff0000ff, 0xff00ffff, 0xff00ff00, 0xffff00ff,
        		0xffff0000, 0xffffff00};	    
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
        else if(spinner.getId() == R.id.finishColourPicker)
        {
          	finishPickedColour = colours[pos];
        }
        }

    @Override
	public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
