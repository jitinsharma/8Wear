package com.sharma.jitin.a8wear;

import android.animation.Animator;
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
import android.os.Handler;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.CardFrame;
import android.support.wearable.view.WatchViewStub;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
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
    BoxInsetLayout boxInsetLayout;
    CardFrame cardFrame;

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
                boxInsetLayout = (BoxInsetLayout)stub.findViewById(R.id.box_inset);
                cardFrame = (CardFrame)stub.findViewById(R.id.card);
                drawable = ballImage.getDrawable();
                color = R.color.black;
                displayText(getResources().getString(R.string.shake_initial));
                cardFrame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        response();
                    }
                });
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

        float force = (float) Math.sqrt((x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH));
        long actualTime = event.timestamp;
        if (force >= 2) //
        {
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;
            response();
        }
    }

    public void response(){
        Resources resources = getResources();
        String[] outputs = resources.getStringArray(R.array.outputs);

        final String random = (outputs[new Random().nextInt(outputs.length)]);

        if(random.contentEquals("Ask again later")||random.contentEquals("Reply hazy try again")
                ||random.contentEquals("Better not tell you now")||random.contentEquals("Cannot predict now")
                ||random.contentEquals("Concentrate and ask again")){
            if (color != R.color.my_blue) {
                boxInsetLayout.setBackground(resources.getDrawable(R.drawable.imgae_b));
            }
            color = R.color.my_blue;
            displayTextWithDelay(random);
        }
        else if(random.contentEquals("Don't count on it")||random.contentEquals("My reply is no")
                ||random.contentEquals("My sources say no")||random.contentEquals("Outlook not so good")
                ||random.contentEquals("Very doubtful")){
            if (color != R.color.my_red) {
                boxInsetLayout.setBackground(resources.getDrawable(R.drawable.image_r));
            }
            color = R.color.my_red;
            displayTextWithDelay(random);
        }
        else{
            if (color != R.color.my_green) {
                boxInsetLayout.setBackground(resources.getDrawable(R.drawable.image_g));
            }
            color = R.color.my_green;
            displayTextWithDelay(random);
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
        mTextSwitcher.setFactory(this);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            Animation in = AnimationUtils.loadAnimation(this,
                    android.R.anim.slide_in_left);
            Animation out = AnimationUtils.loadAnimation(this,
                    android.R.anim.slide_out_right);
            in.setDuration(200);
            mTextSwitcher.setInAnimation(in);
            mTextSwitcher.setOutAnimation(out);
        }
        else{
            int cx = cardFrame.getWidth() / 2;
            int cy = cardFrame.getHeight() / 2;
            float finalRadius = (float) Math.hypot(cx, cy);
            Animator anim = null;
            anim = ViewAnimationUtils.createCircularReveal(cardFrame, cx, cy, 0, finalRadius);
            cardFrame.setVisibility(View.VISIBLE);
            anim.start();
        }
        mTextSwitcher.setText(text);
    }

    public void displayTextWithDelay(final String text){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                displayText(text);
            }
        },200);
    }
}
