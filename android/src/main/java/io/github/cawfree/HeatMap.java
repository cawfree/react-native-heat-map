package io.github.cawfree;

import com.facebook.react.module.annotations.ReactModule;

import android.os.Bundle;  
import android.app.Activity;  
import android.view.Menu;  
import android.content.Context;  
import android.graphics.Canvas;  
import android.graphics.Bitmap;  
import android.graphics.LinearGradient;  
import android.graphics.Color;  
import android.graphics.Paint;  
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.view.View;  
import android.view.ViewGroup.LayoutParams;

import android.util.Log;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Collections;

@ReactModule(name = HeatMapManager.REACT_CLASS)
public class HeatMap extends View {  

  /* Base Declarations. */
  private static String TAG = "HeatMap";

  /** A logging helper method. */
  private static void log(final String pMessage) {
    Log.d(TAG, pMessage);
  }

  /** Represents the parameters of a spread. */
  private static final class Spread {
    /* Member Variables.  */
    private final float mX;
    private final float mY;
    private final float mIntensity;
    /* Constructor. */
    private Spread(final float pX, final float pY, final float pIntensity) {
      // Initialize Member Variables.
      this.mX         = pX;
      this.mY         = pY;
      this.mIntensity = pIntensity;
    }
    /* Getters. */
    private final float getX() {
      return this.mX;
    }
    private final float getY() {
      return this.mY;
    }
    private final float getIntensity() {
      return this.mIntensity;
    }
  } 

  /* Static Declarations. */
  private static final float              DEFAULT_BLUR        = 15f;
  private static final float              DEFAULT_MAX         = 1f;
  private static final float              DEFAULT_RADIUS      = 25f;
  private static final float              DEFAULT_MIN_OPACITY = 0.05f;
  private static final Map<Float, String> DEFAULT_GRADIENT = Collections
      .unmodifiableMap(
        new HashMap<Float, String>() { {
          this.put(Float.valueOf(0.4f), "blue");
          this.put(Float.valueOf(0.6f), "cyan");
          this.put(Float.valueOf(0.7f), "lime");
          this.put(Float.valueOf(0.8f), "yellow");
          this.put(Float.valueOf(1.0f), "red");
        }}
      );

  private static final Bitmap radius(final float pRadius, final float pBlur) {
    final float       lRadius = pRadius + pBlur;
    final Bitmap      lBitmap = Bitmap.createBitmap(Math.round(lRadius) * 2, Math.round(lRadius) * 2, Bitmap.Config.ARGB_8888);
    final Canvas      lCanvas = new Canvas(lBitmap);
    final Paint lPaint = new Paint();

    lPaint.setStyle(
      Paint.Style.FILL
    );

    //lPaint.setColor(Color.BLUE);

    lPaint.setShadowLayer(
      pBlur,
      0,
      0,
      //lRadius * 2,
      //lRadius * 2,
      Color.BLACK
    );

    lCanvas.drawCircle(
      Math.round(lRadius),
      Math.round(lRadius),
      pRadius,
      lPaint
    );

    log("detected"+lRadius+" and blur "+pBlur);

    //// arc(x, y, r, sAngle, eAngle, counterClockwise)
    //lCanvas.drawArc(
    //  0,
    //  0,
    //  lRadius * 2,
    //  lRadius * 2,
    //  0,
    //  (float)(Math.PI * 2),
    //  false, // useCenter vs counterClockwise...
    //  lPaint
    //);
    // Return the Bitmap.
    return lBitmap;
  } 

  /** Generates a Bitmap which contains the evaluated Gradient. */
  private static final Bitmap gradient(final Map<Float, String> pGradient) {
    // Declare Member Variables.
    final Bitmap        lBitmap    = Bitmap.createBitmap(1, 256, Bitmap.Config.ARGB_8888);
    final Canvas        lCanvas    = new Canvas(lBitmap);
    final List<Integer> lColors    = new ArrayList<>(pGradient.size());
    final List<Float>   lPositions = new ArrayList<>(pGradient.size());
    // Fetch the Map Entries.
    final List<Float> lKeys = new ArrayList<>(pGradient.keySet());
    // TODO: sort these entries!!!
    Collections
      .sort(
        lKeys
      );
    // Iterate the entries.
    for (int i = 0; i < lKeys.size(); i += 1) {
      // Fetch the entry.
      final Float lKey    = lKeys.get(i);
      final String lValue = pGradient.get(lKey);
      // TODO: convert string to integer color
      //lColors.add(lValue);
      lColors.add(Color.RED); // TODO: need to do this for real
      lPositions.add(lKey);
    }
    final int[]   lMappedColors    = new int[lColors.size()];
    final float[] lMappedPositions = new float[lPositions.size()];
    // Iterate the entries.
    for (int i = 0; i < lKeys.size(); i += 1) {
      // Buffer the primitive implementations.
      lMappedColors[i] = (int)lColors.get(i);
      lMappedPositions[i] = (float)lPositions.get(i);
    }
    final LinearGradient lLinearGradient = new LinearGradient(
      0,
      0,
      1, // TODO was 0
      256,
      lMappedColors,
      lMappedPositions,
      // TODO: correct tile mode to use?
      TileMode.CLAMP
    );
    // Allocate a new Paint instance.
    final Paint lPaint = new Paint();
    // Define the LinearGradient for the Paint's shader.
    lPaint.setShader(lLinearGradient);
    lPaint.setStyle(Paint.Style.FILL);
    lCanvas.drawRect(
      0,
      0,
      1,
      256,
      lPaint
    );
    return lBitmap;
  }

  /** Renders the HeatMap. */
  private static final void draw(final Canvas pCanvas, final Bitmap pBitmap, final Map<String, Spread> pSpreads, final Map<Float, String> pGradient, final float pRadius, final float pMax, final float pMinOpacity, final float pBlur) {
    // Allocate the circle to render against.
    final Bitmap lCircle = HeatMap.radius(
      pRadius,
      pBlur
    );
    final Bitmap lGradient = HeatMap.gradient(
      pGradient
    );
    // Allocate the Paint instance.
    final Paint lPaint = new Paint(); 
    // Clear the Canvas.
    //pCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    // Draw a greyscale heatmap by placing a blurred circle at each data point.
    for (final Entry<String, Spread> lEntry : pSpreads.entrySet()) {
      // Fetch the point data.
      final Spread lPoint = lEntry.getValue();
      // Compute the Alpha to render the circle at.
      final int lAlpha = Math.round(Math.min(Math.max(lPoint.getIntensity() / pMax, pMinOpacity), 1) * 255);
      // Set the Alpha.
      lPaint.setAlpha(lAlpha);
      pCanvas
        .drawBitmap(
          lCircle,
          // TODO: top left corner defines render position
          lPoint.getX() - pRadius,
          lPoint.getY() - pRadius,
          // Draw using the supplied Paint.
          lPaint
        );
    }
    // Release the allocated Bitmaps.
    lCircle.recycle();
    lGradient.recycle();
  }

  /* Member Variables. */
  private       int                 mCanvasWidth;
  private       int                 mCanvasHeight;
  private       float               mMax;
  private       float               mRadius;
  private final Map<Float, String>  mGradient;
  private final Map<String, Spread> mSpreads;
  private       float               mMinOpacity;
  private       float               mBlur;

  /* Constructor. */
  public HeatMap(final Context pContext) {
    // Implement the parent.
    super(pContext);
    // Initialize Member Variables.
    this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    this.mCanvasWidth  = 0;
    this.mCanvasHeight = 0;
    this.mMax          = HeatMap.DEFAULT_MAX;
    this.mRadius       = HeatMap.DEFAULT_RADIUS;
    this.mGradient     = HeatMap.DEFAULT_GRADIENT;
    this.mSpreads      = new HashMap<>();
    this.mMinOpacity   = HeatMap.DEFAULT_MIN_OPACITY;
    this.mBlur         = HeatMap.DEFAULT_BLUR;

    this.getSpreads().put("test", new Spread(100f, 100f, 0.5f));

  }

  @Override
  protected void onSizeChanged(final int pWidth, final int pHeight, final int pPrevWidth, final int pPrevHeight) {
    this.setCanvasWidth(pWidth);
    this.setCanvasHeight(pHeight);
    super.onSizeChanged(
      pWidth,
      pHeight,
      pPrevWidth,
      pPrevHeight
    );
  }

  @Override 
  protected void onDraw(final Canvas pCanvas) {
    super.onDraw(pCanvas);  

    Paint paint = new Paint();  
    paint.setStyle(Paint.Style.FILL);  
    paint.setColor(Color.CYAN);  
    pCanvas.drawPaint(paint);  


    if (this.getCanvasWidth() > 0 && this.getCanvasHeight() > 0) {
      // TODO: should verify the width and height before attempting to do this.
  
      final Bitmap lBitmap = Bitmap.createBitmap(this.getCanvasWidth(), this.getCanvasHeight(), Bitmap.Config.ARGB_8888);
      final Canvas lCanvas = new Canvas(lBitmap);
  
      // TODO: Delegate Pain object for performance.
  
      // Render the Heatmap to the local Canvas.
      HeatMap.draw(
        lCanvas,
        lBitmap,
        this.getSpreads(),
        this.getGradient(),
        this.getRadius(),
        this.getMax(),
        this.getMinOpacity(),
        this.getBlur()
      );
  
      // Write the resulting bitmap to the global Canvas.
      pCanvas.drawBitmap(
        lBitmap,
        0,
        0,
        new Paint()
      );

      // Recycle the allocated Bitmap.
      lBitmap.recycle();
    }
  }

  /* Getters and Setters. */
  private final void setCanvasWidth(final int pCanvasWidth) {
    this.mCanvasWidth = pCanvasWidth;
  }

  private final int getCanvasWidth() {
    return this.mCanvasWidth;
  }

  private final void setCanvasHeight(final int pCanvasHeight) {
    this.mCanvasHeight = pCanvasHeight;
  }

  private final int getCanvasHeight() {
    return this.mCanvasHeight;
  }

  private final Map<String, Spread> getSpreads() {
    return this.mSpreads;
  }

  private final Map<Float, String> getGradient() {
    return this.mGradient;
  }

  private final float getRadius() {
    return this.mRadius;
  }

  private final float getMax() {
    return this.mMax;
  }

  private final float getMinOpacity() {
    return this.mMinOpacity;
  }

  private final float getBlur() {
    return this.mBlur;
  }

}  
