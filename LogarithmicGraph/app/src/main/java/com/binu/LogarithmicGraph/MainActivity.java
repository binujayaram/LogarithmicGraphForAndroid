package com.binu.LogarithmicGraph;

import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private  Point[] mPlotPoints;
    private int[] X_Values = {20, 100, 300, 550, 625, 1056, 2085, 3566, 8021, 11222, 13446, 17899, 19865};
    private int[] Y_Values = {-10,-2, -3, 1, 2, 6, 3, 1, -1, -3, -2, 1, 3};

    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                loadGraph();
            }
        }, 100);
    }

    void init(){
        mImageView = (ImageView)findViewById(R.id.imageView);

    }

    void loadGraph(){
        mPlotPoints = new Point[X_Values.length];
        for(int i = 0;i<X_Values.length;i++){
            mPlotPoints[i] = new Point(X_Values[i],Y_Values[i]);
        }
        DrawGraphAsyncTask asyncTaskBefore = new DrawGraphAsyncTask(getApplicationContext(), mImageView.getHeight(), mImageView.getWidth(), X_Values, Y_Values, mImageView);
        asyncTaskBefore.setAnimationEnabled(true);
        asyncTaskBefore.setShowLabels(true);
        asyncTaskBefore.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
