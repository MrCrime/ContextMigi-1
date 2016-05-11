package com.github.migi_1.ContextApp;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import com.jme3.app.AndroidHarness;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class HelloActivity extends AndroidHarness implements SensorEventListener{

        private SensorManager mSensorManager;
        private Sensor mSensor;
    
        public HelloActivity(){
        // Set the application class to run
        appClass = "com.github.migi_1.ContextApp.Main";
        // Try ConfigType.FASTEST; or ConfigType.LEGACY if you have problems
//        eglConfigType = ConfigType.BEST;
        // Exit Dialog title & message
//        exitDialogTitle = "Exit?";
//        exitDialogMessage = "Press Yes";
//        // Enable verbose logging
////        eglConfigVerboseLogging = false;
//        // Choose screen orientation
////        screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//        // Enable MouseEvents being generated from TouchEvents (default = true)
//        mouseEventsEnabled = true;
//        // Set the default logging level (default=Level.INFO, Level.ALL=All Debug Info)
//        LogManager.getLogManager().getLogger("").setLevel(Level.INFO);
    }

         @Override  
    protected void onResume()  
    {  
        super.onResume();
        //mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_FASTEST);
    }
        
        @Override  
    public void onCreate(Bundle savedInstanceState)  
    {  
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); 
    }

    @Override
    public void onSensorChanged(SensorEvent se) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}