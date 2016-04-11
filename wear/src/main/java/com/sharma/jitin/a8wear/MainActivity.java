package com.sharma.jitin.a8wear;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.Random;

public class MainActivity extends Activity implements SensorEventListener, ViewSwitcher.ViewFactory {

    private TextSwitcher mTextSwitcher;
    private SensorManager sensorManager;
    private long lastUpdate;
    int color;
    ImageView ballImage;
    Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextSwitcher = (TextSwitcher) stub.findViewById(R.id.text);
                ballImage = (ImageView)stub.findViewById(R.id.ball_icon);
                drawable = ballImage.getDrawable();
                color = R.color.black;
                displayText(getResources().getString(R.string.shake_initial));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            //mTextSwitcher.setText("sensor change captured");
            getAccelerometer(sensorEvent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelerationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = event.timestamp;
        if (accelerationSquareRoot >= 2) //
        {
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;
            response();
        }
    }

    public void response(){
        //Intent intent = new Intent(this, ResponseActivity.class);
        String[] fruits = {"It is certain","Reply hazy try again","Don't count on it",
                "It is decidedly so","Ask again later","My reply is no","Without a doubt",
                "Better not tell you now","My sources say no","Yes definitely","Cannot predict now",
                "Outlook not so good","You may rely on it","Concentrate and ask again","Very doubtful",
                "As I see it, yes","Most likely","Outlook good","Yes","Signs point to yes",
        };
        Resources resources = getResources();
        String[] outputs = resources.getStringArray(R.array.outputs);

        String random = (outputs[new Random().nextInt(outputs.length)]);
        //String random = (fruits[new Random().nextInt(fruits.length)]);
        //TextSwitcher mTextSwitcher = (TextSwitcher) findViewById(R.id.text);

        if(random.contentEquals("Ask again later")||random.contentEquals("Reply hazy try again")
                ||random.contentEquals("Better not tell you now")||random.contentEquals("Cannot predict now")
                ||random.contentEquals("Concentrate and ask again")){
            //voice_op.setTextColor(getResources().getColor(R.color.blue));
            color = R.color.my_blue;
            displayText(random);
        }
        else if(random.contentEquals("Don't count on it")||random.contentEquals("My reply is no")
                ||random.contentEquals("My sources say no")||random.contentEquals("Outlook not so good")
                ||random.contentEquals("Very doubtful")){
            color = R.color.my_red;
            displayText(random);
        }
        else{
            color = R.color.my_green;
            displayText(random);
        }
    }

    @Override
    public View makeView() {
        TextView textView = new TextView(MainActivity.this);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextSize(15);
        textView.setTextColor(getResources().getColor(color));
        textView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "Roboto-Condensed.ttf"), Typeface.BOLD);
        return textView;
    }

    public void displayText(String text){
        if (drawable instanceof Animatable){
            ((Animatable) drawable).start();
        }
        mTextSwitcher.removeAllViews();
        //SystemClock.sleep(300);
        mTextSwitcher.setFactory(this);
        Animation in = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);
        in.setDuration(200);
        mTextSwitcher.setInAnimation(in);
        mTextSwitcher.setOutAnimation(out);
        mTextSwitcher.setText(text);
    }
}
