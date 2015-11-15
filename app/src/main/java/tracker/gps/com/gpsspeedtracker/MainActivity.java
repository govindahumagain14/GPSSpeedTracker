package tracker.gps.com.gpsspeedtracker;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayDeque;

public class MainActivity extends Activity {

    public static ArrayDeque arrayDeque;
    public static int averageSpeed = 0;
    private MyGraphView myGraphView;
    private TextView textViewCurrentSpeed;
    private TextView textViewAverageSpeed;
    private TextView textViewOverallTime;
    private Button buttonTrack;
    private LocationManager manager;
    private MySpeedListener mySpeedListener;
    private int recordedPoints;
    int minTimeToListenSpeed = 1000;
    float minDistanceToTravel = 5;
    private int secondsStartedTracking = 0;
    private Criteria locationCriteria;
    private int totalSpeed;
    Handler handler = new Handler();
    private boolean trackingStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initResources();
    }

    private void initResources() {
        arrayDeque = new ArrayDeque(100);
        buttonTrack = (Button) findViewById(R.id.track);
        textViewCurrentSpeed = (TextView) findViewById(R.id.current_speed);
        textViewOverallTime = (TextView) findViewById(R.id.overall_time);
        myGraphView = (MyGraphView) findViewById(R.id.graph);
        textViewAverageSpeed = (TextView) findViewById(R.id.average_speed);
        setGraphHeight();
        mySpeedListener = new MySpeedListener(MainActivity.this);
        locationCriteria = new Criteria();
        buttonTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!trackingStarted) {
                    keepTracking();
                } else {
                    stopTracking();
                }
            }
        });
    }

    private void setGraphHeight() {
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                myGraphView.getLayoutParams();
        params.height = display.getWidth();
        myGraphView.setLayoutParams(params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void stopTracking() throws SecurityException {
        manager.removeUpdates(mySpeedListener);
        manager = null;
        trackingStarted = false;
        buttonTrack.setText(getResources().getString(R.string.start_tracking));
        myGraphView.invalidate();
    }

    private void keepTracking() throws SecurityException {
        locationCriteria.setPowerRequirement(Criteria.POWER_LOW);
        locationCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationCriteria.setSpeedRequired(true);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String bestProvider = manager.getBestProvider(locationCriteria, false);
        manager.requestLocationUpdates(bestProvider, minTimeToListenSpeed, minDistanceToTravel, mySpeedListener);

        buttonTrack.setText(getResources().getString(R.string.stop_tracking));
        trackingStarted = true;
        startTimeValue();
    }

    public void setCurrentSpeed(int currentSpeed) {
        recordedPoints++;
        totalSpeed = totalSpeed + currentSpeed;
        averageSpeed = totalSpeed / recordedPoints;
        arrayDeque.add(currentSpeed);
        textViewAverageSpeed.setText(getResources().getString(R.string.average_speed) + " "
                + averageSpeed + " " + getResources().getString(R.string.kmhr));
        textViewCurrentSpeed.setText(getResources().getString(R.string.current_speed) + " "
                + currentSpeed + " " + getResources().getString(R.string.kmhr));
        if (arrayDeque.size() > 100) {
            arrayDeque.poll();
        }
        myGraphView.invalidate();
    }

    void startTimeValue() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (trackingStarted) {
                    secondsStartedTracking++;
                    textViewOverallTime.setText(getResources().getString(R.string.overall_time)
                            + " " + secondsStartedTracking + getResources().getString(R.string.s));
                    startTimeValue();
                }
            }
        }, 1000);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
