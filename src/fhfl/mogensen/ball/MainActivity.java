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

public class MainActivity extends Activity implements SensorEventListener
{

	CustomDrawableView mCustomDrawableView = null;
	ShapeDrawable myCircle = new ShapeDrawable();
	ShapeDrawable myFinishSquare = new ShapeDrawable();
	ShapeDrawable myBackgroundSquare = new ShapeDrawable();
	public float xPosition, xAcceleration, xVelocity = 0.0f;
	public float yAcceleration, yVelocity = 0.0f;
	public float yPosition = 100;
	public float xMax, yMax, xMaxBorder, yMaxBorder, xCen, yCen;
	private SensorManager sensorManager = null;
	private Sensor accelerometer;
	public float frameTime = 0.666f;
	final int circleDiameter = 30;
	int circleRadius = circleDiameter / 2;
	int randomInt;
	int finishStartX = 250;
	int finishEndX = 350;
	int finishStartY = 650;
	int finishEndY = 750;
	float finishModifier;
	int goalCounter = 1;
	int lives;
	int livesBonus = 20;
	double speedModifier = 1;
	String TAG = "fhfl";
	int circleColour;
	int finishColour;
	int backgroundColour;
	boolean bToast;
	private Toast toast = null;

	/** Called when the activity is first created. */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		// Set FullScreen & portrait
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Get a reference to a SensorManager
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// Get Settings
		if (MainMenu.bSettings == true)
		{
			circleColour = Customize.circlePickedColour;
			finishColour = Customize.finishPickedColour;
			backgroundColour = Customize.backgroundPickedColour;
			speedModifier = Customize.pickedSpeed;
			lives = Customize.pickedLives;
		} else
		{
			circleColour = Color.RED;
			finishColour = Color.GREEN;
			backgroundColour = 0;
			lives = 3;
		}
		// Custom View
		mCustomDrawableView = new CustomDrawableView(this);
		setContentView(mCustomDrawableView);

		// Calculate Borders
		Display display = getWindowManager().getDefaultDisplay();
		xMaxBorder = (float) display.getWidth() - circleRadius;
		yMaxBorder = (float) display.getHeight() - circleRadius;
		xMax = (float) display.getWidth();
		yMax = (float) display.getHeight();
		xCen = (float) display.getWidth() / 2;
		yCen = (float) display.getHeight() / 2;
		
		//Set Start Position
		xPosition = xCen;
	}

	// This method will update the UI on new sensor events
	@Override
	public void onSensorChanged(SensorEvent event)
	{
		{
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER
					&& MainMenu.bRunning == true)
			{
				// Set sensor values as acceleration
				yAcceleration = event.values[1];
				xAcceleration = event.values[0];
				if (Customize.pickedSpeed >= 3)
				{
					finishStartX = (finishStartX+1)+(int)finishModifier;
					finishStartY = (finishStartY+1)+(int)finishModifier;
					finishEndX = finishStartX+circleDiameter;
					finishEndY = finishStartY+circleDiameter;
				}
				updateBall();
				Log.v(TAG, "updateBall()");
			}
		}
	}

	private void updateBall()
	{

		// Calculate new speed
		xVelocity += ((xAcceleration * frameTime) / 2) * (float) speedModifier;
		Log.v(TAG, "xVelocity: " + xVelocity);
		yVelocity -= ((yAcceleration * frameTime) / 2) * (float) speedModifier;

		// Calc distance travelled in that time
		float xS = (xVelocity / 2) * frameTime;
		float yS = (yVelocity / 2) * frameTime;

		// Add to position negative due to sensor
		// readings being opposite to what we want!
		xPosition -= xS;
		yPosition -= yS;

		if (xPosition + circleRadius >= finishStartX
				&& xPosition + circleRadius <= finishEndX
				&& yPosition + circleRadius >= finishStartY
				&& yPosition + circleRadius <= finishEndY)
		{
			win();
			Log.v(TAG, "newLevel()");
		}
		if (xPosition > xMaxBorder)
		{
			fail();
		} else if (xPosition < 0)
		{
			fail();
		}
		if (yPosition > yMaxBorder)
		{
			fail();
		} else if (yPosition < 0)
		{
			yPosition = 0+circleRadius;
		}
	}

	private void win()
	{
		goalCounter++;
		speedModifier = speedModifier + 0.05;
		if (goalCounter - 1 == livesBonus)
		{
			lives++;
			livesBonus = livesBonus + 20;
			if (bToast == true)
			{
				toast.cancel();
				bToast = false;
			}
			toast = Toast.makeText(getApplicationContext(), "1up",
					Toast.LENGTH_SHORT);
			toast.show();
		}
		newLevel();
		xPosition = xCen;
		yPosition = 100;
		xVelocity = 0;
		yVelocity = 0;
	}

	private void fail()
	{
		lives--;
		xPosition = xCen;
		yPosition = 100;
		xVelocity = 0;
		yVelocity = 0;
		if (lives == 0)
		{
			gameOver();
		} else
		{
		if (bToast == true)
		{
			toast.cancel();
			bToast = false;
		}
		toast = Toast.makeText(getApplicationContext(), "Fail! " + lives
				+ ". lives remaining", Toast.LENGTH_SHORT);
		toast.show();
		bToast = true;
		}
	}

	private void newLevel()
	{
		Random random = new Random();
		randomInt = random.nextInt(350);
		Log.d(TAG, " RNG: " + randomInt);

		if (goalCounter <= 60)
		{
			finishStartX = randomInt;
			finishModifier = ((circleDiameter * 3) - (goalCounter - 1));
			finishEndX = randomInt + (int)finishModifier;
			finishStartY = 400 + randomInt;
			finishEndY = (400 + randomInt) + (int)finishModifier;
		} else
		{
			finishStartX = randomInt;
			finishEndX = randomInt + circleDiameter;
			finishStartY = 400 + randomInt;
			finishEndY = (400 + randomInt) + circleDiameter;
		}
	}
	public void reset()
	{
		goalCounter = 1;
		speedModifier = Customize.pickedSpeed - 0.05;
		if (MainMenu.bSettings == true)
		{
			lives = Customize.pickedLives;
		} else
		{
			lives = 3;
		}
		newLevel();
		if (bToast == true)
		{
			toast.cancel();
			bToast = false;
		}
		toast = Toast.makeText(getApplicationContext(), "Game reset",
				Toast.LENGTH_SHORT);
		toast.show();
		
	}
	public void gameOver()
	{
		goalCounter = 1;
		speedModifier = Customize.pickedSpeed - 0.05;
		if (MainMenu.bSettings == true)
		{
			lives = Customize.pickedLives;
		} else
		{
			lives = 3;
		}
		newLevel();
		if (bToast == true)
		{
			toast.cancel();
			bToast = false;
		}
		toast = Toast.makeText(getApplicationContext(),
				"Game over man. Game over.", Toast.LENGTH_SHORT);
		toast.show();
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
		sensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	protected void onPause()
	{
		gamePaused();
		super.onPause();
	}

	public void gamePaused()
	{
		toast = Toast.makeText(getApplicationContext(), "Game paused",
				Toast.LENGTH_SHORT);
		toast.show();
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
			myBackgroundSquare = new ShapeDrawable(new RectShape());
			myBackgroundSquare.getPaint().setColor(backgroundColour);
		}

		@Override
		protected void onDraw(Canvas canvas)
		{
			myBackgroundSquare.setBounds(0, 0, (int) xMax, (int) yMax);
			myBackgroundSquare.draw(canvas);
			myFinishSquare.setBounds(finishStartX, finishStartY, finishEndX,
					finishEndY);
			myFinishSquare.draw(canvas);
			myCircle.setBounds((int) xPosition, (int) yPosition,
					(int) xPosition + circleDiameter, (int) yPosition
							+ circleDiameter);
			myCircle.draw(canvas);

			// paint to write text with
			Paint paint = new Paint();
			paint.setStyle(Style.FILL);
			if (backgroundColour == Color.BLACK)
			{
				paint.setColor(Color.WHITE);
			} else
			{
				paint.setColor(Color.BLACK);
			}
			paint.setTextSize(18);
			canvas.drawText("Goal Counter: " + (goalCounter - 1) + " Lives: "
					+ lives, xCen - 25, 25, paint);
			invalidate();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
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
			reset();
			return true;
		case R.id.mainmenu:
			onPause();
			setContentView(R.layout.activity_main);
			return true;
		case R.id.help:
			// showHelp();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}