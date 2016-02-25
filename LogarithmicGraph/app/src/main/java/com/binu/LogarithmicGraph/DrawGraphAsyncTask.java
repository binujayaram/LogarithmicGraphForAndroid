/**
 * @file DrawGraphAsyncTask.java
 * @author Binu Jayaram <binujayaram@hotmail.com>
 * @version 1.0
 * @section LICENSE
 * <p/>
 * Copyright (c) Binu Jayaram (www.binujayaram.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * <p/>
 * This class can plot draw the Logarithmic background and plot
 */


package com.binu.LogarithmicGraph;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.math.BigDecimal;


/**
 * Created by binu.jayaram on 2/11/2016.
 */

public class DrawGraphAsyncTask extends AsyncTask {

   /*********Constants**********/
    /**
     * Major grid lines on the logarithmic screen
     */
    public static final float MAJOR_GRIDLINE_POINTS[] = {20, 100, 1000, 10000, 20000};

    /**
     * Minor grid lines on the logarithmic screen
     */
    public static final float MINOR_GRIDLINE_POINTS[] = {30, 40, 50, 60, 70, 80, 90, 200, 300, 400, 500, 600, 700, 800, 900, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000};

    public static final int MAX_FREQUENCY = 20000;
    public static final int MIN_FREQUENCY = 20;

    /*Minimum gain limit for target sound options*/
    public static final float GAIN_MIN = -15;

    /*Maximum gain limit for target sound options*/
    public static final float GAIN_MAX = +15;

    public static final int Y_AXIS_INTERVAL = 5;


    /*********End**********/


    private Context mContext;
    private int mViewHeight, mViewWidth;
    private ImageView mImageView;
    private BitmapDrawable mDrawableBackground, mDrawableForeground;
    private Animation popIn;
    private boolean mIsEditPlotOnly = false;
    private boolean mIsShowLabels = false;
    private int mLabelTextSize = 40;
    private boolean mIsAnimationEnabled = false;
    private float PLOT_THICKNESS = 3.0f;
    private int[] X_values;
    private int[] Y_values;
    private double[] mLogScaledX_values;



    /**
     * Constructor
     *
     * @param aContext    current application context
     * @param aHeight     height of the plot area
     * @param aWidth      width of the plot area
     * @param X_values
     * @param Y_values
     * @param aImageView  imageview where the graph is drawn
     */

    public DrawGraphAsyncTask(Context aContext, int aHeight, int aWidth, int[] X_values, int[] Y_values, ImageView aImageView) {

        mContext = aContext;
        mViewHeight = aHeight;
        mViewWidth = aWidth;
        this.X_values = X_values;
        this.Y_values = Y_values;
        mImageView = aImageView;

    }


    @Override
    protected Object doInBackground(Object[] params) {

            Bitmap bitmapBackground = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.ARGB_8888);

            Canvas canvasBackground = new Canvas(bitmapBackground);
            Paint paint = new Paint();
            paint.setStrokeWidth(1f);
            int starty = 0;
            int endy = mViewHeight;
            canvasBackground.drawColor(Color.BLACK);
            double ratio = Math.pow(Math.E, Math.log(MAX_FREQUENCY / MIN_FREQUENCY) / mViewWidth);
            mLogScaledX_values = new double[mViewWidth];
            for (int i = 0; i < mViewWidth; i++) {
                if (i == 0) {
                    mLogScaledX_values[i] = 20;
                } else {
                    mLogScaledX_values[i] = mLogScaledX_values[i - 1] * ratio;
                }
            }

            //Major lines
            for (int i = 0; i < MAJOR_GRIDLINE_POINTS.length; i++) {
                paint.setColor(ContextCompat.getColor(mContext, R.color.graph_grid_color));
                float xStart = (float) findNearestNumberPosition(mLogScaledX_values, MAJOR_GRIDLINE_POINTS[i]);
                float textSize = (getPixelDensity(mContext) * mLabelTextSize) / 480;
                paint.setTextSize(textSize);
                Log.i("Density", "" + getPixelDensity(mContext));
                if (Math.round(MAJOR_GRIDLINE_POINTS[i]) == 20) {
                    if (isShowLabels())
                        canvasBackground.drawText(getFormattedLabel(MAJOR_GRIDLINE_POINTS[i]), xStart + (getPixelDensity(mContext) * 10) / 480, endy - (getPixelDensity(mContext) * 10) / 480, paint);
                } else if (Math.round(MAJOR_GRIDLINE_POINTS[i]) == 20000) {
                    if (isShowLabels())
                        canvasBackground.drawText(getFormattedLabel(MAJOR_GRIDLINE_POINTS[i]), xStart - (getPixelDensity(mContext) * 70) / 480, endy - (getPixelDensity(mContext) * 10) / 480, paint);
                } else {
                    if (isShowLabels())
                        canvasBackground.drawText(getFormattedLabel(MAJOR_GRIDLINE_POINTS[i]), xStart - (getPixelDensity(mContext) * 30) / 480, endy - (getPixelDensity(mContext) * 10) / 480, paint);
                    canvasBackground.drawLine(xStart, starty, xStart + 1, endy, paint);
                }
            }

            //Minor lines
            for (int i = 0; i < MINOR_GRIDLINE_POINTS.length; i++) {
                paint.setColor(ContextCompat.getColor(mContext, R.color.graph_grid_color_dull));
                float xStart = (float) findNearestNumberPosition(mLogScaledX_values, MINOR_GRIDLINE_POINTS[i]);
                canvasBackground.drawLine(xStart, starty, xStart + 1, endy, paint);

                if (isShowLabels()) {
                    paint.setColor(ContextCompat.getColor(mContext, R.color.graph_grid_color));
                    if (MINOR_GRIDLINE_POINTS[i] == 50 || MINOR_GRIDLINE_POINTS[i] == 500 || MINOR_GRIDLINE_POINTS[i] == 5000)
                        canvasBackground.drawText(getFormattedLabel(MINOR_GRIDLINE_POINTS[i]), xStart - (getPixelDensity(mContext) * 30) / 480, endy - (getPixelDensity(mContext) * 10) / 480, paint);
                }
            }


            float[] Y_points = calculateGraphYAxis();
            float div = mViewHeight / (Y_points.length - 1);
            //Level lines
            for (int i = 0; i < Y_points.length - 1; i++) {
                paint.setColor(ContextCompat.getColor(mContext, R.color.graph_grid_color_dull));
                canvasBackground.drawLine(0, div * (i + 1), mViewWidth, (div * (i + 1)) + 1, paint);
            }

            //Level labels
            if (isShowLabels()) {
                for (int i = 0; i < Y_points.length; i++) {
                    paint.setColor(ContextCompat.getColor(mContext, R.color.graph_grid_color));
                    if (i == 0)
                        canvasBackground.drawText("dB", 0, (div * i) + (getPixelDensity(mContext) * 50) / 480, paint);
                    else if (i == Y_points.length - 1)
                        canvasBackground.drawText("", 0, div * i, paint);
                    else
                        canvasBackground.drawText("" + Math.round(Y_points[i]), 0, div * i, paint);
                }
            }

            BitmapDrawable drawable = new BitmapDrawable(mContext.getResources(), bitmapBackground);
            mDrawableBackground = drawable;



        //Plotting the curve points

        Bitmap bitmapForeground = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.ARGB_8888);
        Canvas canvasForeground = new Canvas(bitmapForeground);
        Paint plotPaint = new Paint();
        plotPaint.setStyle(Paint.Style.STROKE);
        plotPaint.setStrokeCap(Paint.Cap.ROUND);
        plotPaint.setStrokeWidth(PLOT_THICKNESS);
        plotPaint.setAntiAlias(true);
        plotPaint.setColor(ContextCompat.getColor(mContext, R.color.graph_plot_color));

        for (int i = 0; i < X_values.length; i++) {
//            canvasForeground.drawCircle(i, mViewHeight - mPlotPoint[i], 2f, plotPaint);
            /*float startX, float startY, float stopX, float stopY,
            @NonNull Paint paint*/
            float startX = (float) getGetPixelValueForX(X_values[i]);
            float startY = mViewHeight - getPixelValueFromdB((int)GAIN_MAX,(int) GAIN_MIN, mViewHeight, Y_values[i]);
            float stopX;
            float stopY;
            if (i == X_values.length - 1) {
                stopX = (float) getGetPixelValueForX(X_values[i]);
                stopY = mViewHeight - getPixelValueFromdB((int)GAIN_MAX,(int) GAIN_MIN, mViewHeight, Y_values[i]);
            } else {
                stopX = (float) getGetPixelValueForX(X_values[i+1]);
                stopY = mViewHeight - getPixelValueFromdB((int)GAIN_MAX,(int) GAIN_MIN, mViewHeight, Y_values[i+1]);
            }

            canvasForeground.drawLine(startX, startY, stopX, stopY, plotPaint);

        }

        BitmapDrawable drawableFore = new BitmapDrawable(mContext.getResources(), bitmapForeground);
        mDrawableForeground = drawableFore;
        return null;
    }

    @Override
    protected void onPreExecute() {
        popIn = AnimationUtils.loadAnimation(mContext, R.anim.pop_in);
    }


    @Override
    protected void onPostExecute(Object o) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            mImageView.setBackground(mDrawableBackground);
        else
            mImageView.setBackgroundDrawable(mDrawableBackground);

        mImageView.setImageDrawable(mDrawableForeground);
        if (isAnimationEnabled())
            mImageView.startAnimation(popIn);
    }

    /**
     * The numbers in thousands is formatted by replacing '000' with 'K'
     * Works best only for frequency
     *
     * @param num number greater than 1000
     * @return
     */
    public String getFormattedLabel(float num) {
        String formattedString = "" + Math.round(num);
        if (num >= 1000) {
            int x = Math.round(num);
            formattedString = "" + (x / 1000) + "K";
        } else if (Math.round(num) == 20) {
            formattedString = 20 + " Hz";
        }
        return formattedString;
    }

    /**
     * Is Show Label
     *
     * @return true: labels are drawn | false: labels not drawn
     */
    public boolean isShowLabels() {
        return mIsShowLabels;
    }

    /**
     * Set Show Labels
     *
     * @param mIsShowLabels true: show labels | false: don't show labels
     */
    public void setShowLabels(boolean mIsShowLabels) {
        this.mIsShowLabels = mIsShowLabels;
    }

    /**
     * Is Animation Enables
     *
     * @return true: animation is enabled | false: animation disabled
     */
    public boolean isAnimationEnabled() {
        return mIsAnimationEnabled;
    }

    /**
     * Set Animation Enabled
     *
     * @param mIsAnimationEnabled true: animation is enabled | false: animation is disabled
     */
    public void setAnimationEnabled(boolean mIsAnimationEnabled) {
        this.mIsAnimationEnabled = mIsAnimationEnabled;
    }


    /**
     * Find nearest number to the position
     *
     * @param array array of generated X-axis label points
     * @param myNumber nearest value of this number in the array as to be found
     * @return Double the neare st value position
     */
    public static double findNearestNumberPosition(double[] array,double myNumber)
    {

        double min=0,max=0,nearestPos, minPos = 0, maxPos = 0;

        for(int i=0;i<array.length;i++)
        {
            if(array[i]<myNumber)
            {
                if(min==0)
                {
                    min=array[i];
                    minPos = i;
                }
                else if(array[i]>min)
                {
                    min=array[i];
                    minPos = i;
                }
            }
            else if(array[i]>myNumber)
            {
                if(max==0)
                {
                    max=array[i];
                    maxPos = i;
                }
                else if(array[i]<max)
                {
                    max=array[i];
                    maxPos = i;
                }
            }
            else
            {
                //return array[i];
                return i;
            }
        }

        if(Math.abs(myNumber-min)<Math.abs(myNumber-max))
        {
            nearestPos=minPos;
        }
        else
        {
            nearestPos=maxPos;
        }

        return nearestPos;
    }

    /**
     * Get Pixel Density of the device
     * @param aContext Applicaiton context
     * @return Density of display in int
     */
    public static int getPixelDensity(Context aContext){
        DisplayMetrics metrics = aContext.getResources().getDisplayMetrics();
        return (int)(metrics.density * 160f);
    }


    /**
     * Calculate Graph's Y-axis labels based on MIN and MAX gain provided
     *
     * @return Array of Y-values
     */
    public static float[] calculateGraphYAxis(){
        int total = (int) (Math.abs(GAIN_MIN)+Math.abs(GAIN_MAX));
        int totalPartions = total/Y_AXIS_INTERVAL;
        float[] pointY = new float[totalPartions+1];
       /* for(int i=0;i<pointY.length;i++){
            pointY[i] = Constants.SPEAKER_CORRECTION_GAIN_MIN + (i*5);
            Log.i("Y-Points", "" + i + "," + pointY[i]);
        }*/

        for(int i=pointY.length-1;i>=0;i--){
            pointY[i] = GAIN_MAX - (i*Y_AXIS_INTERVAL);
            Log.i("Y-Points", "" + i + "," + pointY[i]);
        }

        return pointY;
    }


    /**
     * Get dB scale value from the pixel level value
     *
     * Scales pixel value to respective dB value
     *
     * @param aToMax Given MAX gain
     * @param aToMin Given MIN gain
     * @param aHeight height of the plot area
     * @param touchValue actual pixel value
     * @return
     */

    /**
     * Get dB scale value from the pixel level value
     *
     * Scales pixel value to respective dB value
     *
     * @param aFromMax
     * @param aFromMin
     * @param aHeight
     * @param touchValue
     * @return
     */


    public static float getPixelValueFromdB(int aFromMax, int aFromMin, int aHeight, float touchValue){
        int fromMin = aFromMin;
        int fromMax = aFromMax;
        int toMin = 0;
        int toMax = aHeight;
        // Log.d("Scale: ", "Height: "+aHeight+"\nTouchPos: "+touchValue+"\ndB: "+round(((touchValue - fromMin) * (toMax - toMin) / (fromMax - fromMin) + toMin), 2));
        return round(((touchValue - fromMin) * (toMax - toMin) / (fromMax - fromMin) + toMin), 2);
    }



    /**
     * Round to certain number of decimals
     *
     * @param d
     * @param decimalPlace
     * @return
     */
    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    double getGetPixelValueForX(int aValue){
        return findNearestNumberPosition(mLogScaledX_values, aValue);
    }

    int getYForTheLocation(){
        return 0;
    }

}


