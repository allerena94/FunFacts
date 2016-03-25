package edu.villanova.csc5930.funfacts;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FunFacts extends AppCompatActivity implements SensorEventListener {

    float[] nearBartley;
    float[] nearLibrary;
    float[] nearCeer;
    float[] nearMendel;

    String[] bartley = new String[8];
    String[] library = new String[5];
    String[] ceer = new String [8];
    String[] mendel = new String[6];

    double compare;
    int count = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 1000;
    private long lastUpdate = 0;

    private TextView fact;
    private Sensor mSensor;
    private SensorManager mSensorManager;
    private LocationManager locManager;
    private LocationListener locListener;

    /* Villanova Building Locations */

    // Ceer
    double cLat = 40.0366404;
    double cLong = -75.3457346;

    // Bartley
    double bLat = 40.0345950;
    double bLong = -75.3383853;

    // Mendel
    double mLat = 40.0378151;
    double mLong = -75.3419795;

    // Falvey
    double fLat = 40.0374690;
    double fLong = -75.3425460;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun_facts);

        fact = (TextView) findViewById(R.id.funfact);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                /* Fun Facts Implementation */

                // Set Fun Facts
                library[0] = "Falvey Library is home to the Idea Accelerator, designed to increase cross-college interaction within the Villanova community and a multidisciplinary approach to generating and advancing new ideas.";
                library[1] = "Villanova constructed a virtual reality CAVE facility in Falvey with a $1.67 million grant from the National Science Foundation.";
                library[2] = "Falvey Memoria Library receives over 600,000 visits each year.";
                library[3] = "Falvey Library is named after Rev. Daniel P. Falvey, OSA, who was Librarian from 1940 to 1962.";
                library[4] = "Falvey Memoria Library receives over 600,000 visits each year.";

                bartley[0] = "The Villanova School of Business was founded in 1922 by Father Joseph Bartley.";
                bartley[1] = "Bartley Hall was constructed in 1960, remodeled and expanded in 2002, and renovations plans are in the works for 2015.";
                bartley[2] = "Bartley Hall has a state-of-the-art financial technology center that has real-time and historic financial data and can be used to simulate a trading room.";
                bartley[3] = "Accounting and Finance are the two most popular majors at The Villanova School of Business.";
                bartley[4] = "Bartley Hall is home to Villanova's ICE (Innovation, Creativy and Entrpreenureship) Center.";
                bartley[5] = "The Villanova School of Business undergraduate business program is ranked #24 in the nation by Bloomberg Businessweek.";
                bartley[6] = "In 2022, the Villanova School of Business will be 100 years old.";
                bartley[7] = "All undergraduate business students are required to take a course in business analyatics and a course in information technology.";

                ceer[0] = "CEER stands for the Center for Engineering Edcuation and Research. It opened in 1998 and sports impressive labs such as a wind tunnel, an anechoic chamber and 3D printing and modeling.";
                ceer[1] = "Villanova's School of Engineering is ranked in the top 15 in the country by US News & World Report.";
                ceer[2] = "The College of Engineering has 5 undergraduate and 8 graduate degrees (5 of which are available online). An undergraduate engineering student can choose a 5 year bachelors/masters degree.";
                ceer[3] = "John Hennessy was called \"the godfather of Silicon Valley\" by Marc Andreessen. Hennessy is the current president of Stanford University and began his academic career as a Villanova electrical engineer.";
                ceer[4] = "Astronaut Andrew Allen graduated Villanova with a degree in mechanical engineering. Allen flew 3 Space Shuttle missions.";
                ceer[5] = "Executive Vice President and Chief Technology Officer of Verizon Communications, Tony Melone, graduated Villanova with a degree in electrical engineering.  Moelone is the 2015 College of Engineering Ward Endowed Lecture Series speaker.";
                ceer[6] = "Jamie Hyneman, co-host of the Discovery Channel’s show \"MythBusters\", received an honorary Doctor of Engineering degree from Villanova.";
                ceer[7] = "David Collins, known as the \"Father of the Barcode Industry\", graduated Villanova with a degree in civil engineering.";

                mendel[0] = "James Peniston sculpted the statue of Gregor Mendel. Peniston is known for his statue of Benjamin Franklin in Philadelphia and the WWII monument in Washington, D.C.";
                mendel[1] = "Mendel Science Center is named after geneticist and Augustinian monk Gregor Mendel. Mendel is known for having demonstrated inheritance of traits in peas during the mid-1800s.";
                mendel[2] = "On the 4th floor of Mendel Science Center is the Observatory that is equiped with three 14ft. telescopes.";
                mendel[3] = "Biology is the largest undergraduate science major at Villanova.";
                mendel[4] = "Computer Science is the largest graduate science program at Villanova.";
                mendel[5] = "The College of Liberal Arts and Sciences uses Mendel Science Center to house all the science faculty and science labs on campus. The College offers 39 majors, including 8 science majors.";

                // Initialize float array
                nearBartley = new float[1];
                nearLibrary = new float[1];
                nearMendel = new float [1];
                nearCeer = new float[1];

                // Initialize other variables
                String c = "ceer";
                String b = "bartley";
                String f = "falvey";
                String m = "mendal";
                String total =  "";
                String N;
                int n;

                // Compute difference between each of the Villanova buildings and current location
                location.distanceBetween(bLat, bLong, location.getLatitude(),
                        location.getLongitude(), nearBartley);
                String distance_from_bartley = Float.toString(nearBartley[0]);

                location.distanceBetween(fLat, fLong, location.getLatitude(),
                        location.getLongitude(), nearLibrary);
                String distance_from_library = Float.toString(nearLibrary[0]);

                location.distanceBetween(mLat, mLong, location.getLatitude(),
                        location.getLongitude(), nearMendel);
                String distance_from_mendal = Float.toString(nearMendel[0]);

                location.distanceBetween(cLat, cLong, location.getLatitude(),
                        location.getLongitude(), nearCeer);
                String distance_from_Ceer = Float.toString(nearCeer[0]);

                // Check to see which building you are closest to
                compare = nearBartley[0];
                if(compare > nearLibrary[0]){
                    compare = nearLibrary[0];
                }
                if(compare > nearMendel[0]){
                    compare = nearMendel[0];
                }
                if(compare > nearCeer[0]){
                    compare = nearCeer[0];
                }

                // Check to see if not too far away; If too far, no fun fact is given; Otherwise, fun fact of building is given
                if(compare < 100) {
                    if (compare == nearBartley[0]) {
                        //Toast.makeText(MainActivity.this, "You are " + distance_from_bartley + " meters away from Bartley", Toast.LENGTH_SHORT).show();
                        n = (int) (Math.random() * 8);
                        fact.setText(bartley[n]);
                        n = 0;
                    } else if (compare == nearLibrary[0]) {
                        //Toast.makeText(MainActivity.this, "You are " + distance_from_library + " meters away from Library", Toast.LENGTH_SHORT).show();
                        n = (int) (Math.random() * 5);
                        fact.setText(library[n]);
                        n = 0;
                    } else if (compare == nearMendel[0]) {
                        //Toast.makeText(MainActivity.this, "You are " + distance_from_mendal + " meters away from Mendel", Toast.LENGTH_SHORT).show();
                        n = (int) (Math.random() * 6);
                        fact.setText(mendel[n]);
                        n = 0;
                    } else if (compare == nearCeer[0]) {
                        //Toast.makeText(MainActivity.this, "You are " + distance_from_Ceer + " meters away from Ceer", Toast.LENGTH_SHORT).show();
                        n = (int) (Math.random() * 8);
                        fact.setText(ceer[n]);
                        n = 0;
                    }
                }

                else {
                    fact.setText("You are not in any of the four locations. No Fun Fact for you!");
                }

                /* Current Location */
                //lat.setText(getResources().getString(R.string.latitude) + location.getLatitude());
                //lon.setText(getResources().getString(R.string.longitude) + location.getLongitude());

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

                //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                //startActivity(intent);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
                return;
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
                    locManager.removeUpdates(locListener);
                }

                Toast.makeText(FunFacts.this, Integer.toString(count++), Toast.LENGTH_SHORT).show();
            }

            last_x = x;
            last_y = y;
            last_z = z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not used
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}