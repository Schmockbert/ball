package fhfl.mogensen.ball;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener{

CustomDrawableView mCustomDrawableView = null;
ShapeDrawable myCircle = new ShapeDrawable();
ShapeDrawable myFinishSquare = new ShapeDrawable();
public float xPosition, xAcceleration,xVelocity = 0.0f;
public float yPosition, yAcceleration,yVelocity = 0.0f;
public float xmax, ymax, xcen, ycen;
private SensorManager sensorManager = null;
private Sensor accelerometer;
public float frameTime = 0.666f;
final int circleDiameter = 30;
int circleRadius = circleDiameter/2;
int randomInt;
int finishStartX = 250;
int finishEndX = 350;
int finishStartY = 650;
int finishEndY = 750;
int goalCounter = 1;
double speedModifier = 1;
String TAG = "fhfl";
int circleColour;
int finishColour;

/** Called when the activity is first created. */
@SuppressWarnings("deprecation")
@Override
public void onCreate(Bundle savedInstanceState)
{

    super.onCreate(savedInstanceState);

    //Set FullScreen & portrait
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    
    // Get a reference to a SensorManager
    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    //Get Custom Colours
    if (MainMenu.bSettings == true)
    {
    	circleColour = Customize.circlePickedColour;
    	finishColour = Customize.finishPickedColour;
    }
    else
    {
    	circleColour = Color.RED;
    	finishColour = Color.GREEN;
    }
    //Custom View
    mCustomDrawableView = new CustomDrawableView(this);
    setContentView(mCustomDrawableView);

    //Calculate Borders
    Display display = getWindowManager().getDefaultDisplay();
    xmax = (float)display.getWidth() - circleRadius;
    ymax = (float)display.getHeight() - circleRadius;
    xcen = (float)display.getWidth()/2;
    ycen = (float)display.getHeight()/2;
}

// This method will update the UI on new sensor events
@Override
public void onSensorChanged(SensorEvent event)
{
    {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && MainMenu.bRunning == true)
        {
            //Set sensor values as acceleration
            yAcceleration = event.values[1]; 
            xAcceleration = event.values[0];
            updateBall();
            Log.v(TAG, "updateBall()");
        }
    }
}

private void updateBall() {


    //Calculate new speed
    xVelocity += ((xAcceleration * frameTime)/2)*(float)speedModifier;
    Log.v(TAG, "xVelocity: " + xVelocity);
    yVelocity -= ((yAcceleration * frameTime)/2)*(float)speedModifier;
    
    //Calc distance travelled in that time
    float xS = (xVelocity/2)*frameTime;
    float yS = (yVelocity/2)*frameTime;

    //Add to position negative due to sensor 
    //readings being opposite to what we want!
    xPosition -= xS; 
    yPosition -= yS;

    if (xPosition+circleRadius >= finishStartX && xPosition+circleRadius <= finishEndX && yPosition+circleRadius >= finishStartY && yPosition+circleRadius <= finishEndY)
    {
    	newLevel();
    	Log.v(TAG, "newLevel()");
    }
    if (xPosition > xmax)
    {
        fail();
    	Toast.makeText( getApplicationContext(), "Fail!", Toast.LENGTH_SHORT).show();
    } 
    else if (xPosition < 0)
    {
        fail();
    	Toast.makeText( getApplicationContext(), "Fail!", Toast.LENGTH_SHORT).show();
    }
    if (yPosition > ymax)
    { 
        fail();
    	Toast.makeText( getApplicationContext(), "Fail!", Toast.LENGTH_SHORT).show();
    } 
    else if (yPosition < 0)
    {
        fail();
    	Toast.makeText( getApplicationContext(), "Fail!", Toast.LENGTH_SHORT).show();
    }
}

private void fail()
{
	xPosition = xcen;
	yPosition = 40;
	xVelocity = 0;
	yVelocity = 0;
	goalCounter = 1;
	speedModifier = 1;
	newLevel();
}
private void newLevel()
{
	goalCounter++;
	speedModifier = speedModifier+0.05;
	Random random = new Random();
	randomInt = random.nextInt(350);
	Log.d(TAG, " RNG: " + randomInt);

	if (goalCounter <= 60)
		{
		finishStartX = randomInt;
		finishEndX = randomInt+((circleDiameter*3)-(goalCounter-1));
		finishStartY = 400+randomInt;
		finishEndY = (400+randomInt)+((circleDiameter*3)-(goalCounter-1));
		}
	else
	{
		finishStartX = randomInt;
		finishEndX = randomInt+circleDiameter;
		finishStartY = 400+randomInt;
		finishEndY = (400+randomInt)+circleDiameter;
	}
	xPosition = xcen;
	yPosition = 50;
	
}
// I've chosen to not implement this method
@Override
public void onAccuracyChanged(Sensor arg0, int arg1)
{
    // TODO Auto-generated method stub
}

@Override
protected void onResume()
{
    super.onResume();
    sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME); 
}

@Override
protected void onPause()
{
    gamePaused();
    super.onPause();
}

public void gamePaused()
{
	Toast.makeText( getApplicationContext(), "Game paused", Toast.LENGTH_SHORT).show();
	Log.d(TAG, " gamePaused()");
	// unregister sensor listener
	sensorManager.unregisterListener(this);
}

public class CustomDrawableView extends View
{
    public CustomDrawableView(Context context)
    {
        super(context);
        myCircle = new ShapeDrawable(new OvalShape());
        myCircle.getPaint().setColor(circleColour);
        myFinishSquare = new ShapeDrawable(new RectShape());
        myFinishSquare.getPaint().setColor(finishColour);

    }

    @Override
	protected void onDraw(Canvas canvas)
    {
        myFinishSquare.setBounds(finishStartX, finishStartY, finishEndX, finishEndY);
        myFinishSquare.draw(canvas);
    	myCircle.setBounds((int)xPosition, (int)yPosition, (int)xPosition + circleDiameter, (int)yPosition + circleDiameter);
        myCircle.draw(canvas);
        // paint to write text with
        Paint paint = new Paint(); 
        paint.setStyle(Style.FILL);  
        paint.setColor(Color.BLACK);
        paint.setTextSize(18);
        canvas.drawText("Goal Counter: " + (goalCounter-1) + " Debug: RNG: " + randomInt + " speedModifier: " + speedModifier, 0, 25, paint);
        invalidate();
    }
}

@Override
public void onConfigurationChanged(Configuration newConfig) {
    // TODO Auto-generated method stub
    super.onConfigurationChanged(newConfig);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
}

@Override
public boolean onCreateOptionsMenu(Menu menu)
{
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item)
{
    // Handle item selection
    switch (item.getItemId())
    {
        case R.id.reset:
            fail();
        	Toast.makeText( getApplicationContext(), "Game reset", Toast.LENGTH_SHORT).show();
            return true;
        case R.id.mainmenu:
        	onPause();
        	setContentView(R.layout.activity_main);        	
        	return true;
        case R.id.help:
            //showHelp();
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
}
}