package io.github.cawfree;

import com.facebook.react.module.annotations.ReactModule;

import android.os.Bundle;  
import android.app.Activity;  
import android.view.Menu;  
import android.content.Context;  
import android.graphics.Canvas;  
import android.graphics.Bitmap;  
import android.graphics.LinearGradient;  
import android.graphics.RadialGradient;  
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

import javax.annotation.Nullable;

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
  private static final float               DEFAULT_MAX         = 1f;
  private static final float               DEFAULT_RADIUS      = 25f;
  private static final float               DEFAULT_MIN_OPACITY = 0.05f;
  private static final Map<Float, Integer> DEFAULT_GRADIENT = Collections
      .unmodifiableMap(
        new HashMap<Float, Integer>() { {
          this.put(Float.valueOf(0.4f), Color.BLUE);
          this.put(Float.valueOf(0.6f), Color.CYAN);
          this.put(Float.valueOf(0.7f), Color.GREEN);
          this.put(Float.valueOf(0.8f), Color.YELLOW);
          this.put(Float.valueOf(1.0f), Color.RED);
        }}
      );

  /** Creates a safe Bitmap. */
  private static final Bitmap createBitmap(final float pWidth, final float pHeight) {
    final int lWidth = Math.max(1, (int)Math.ceil(pWidth));
    final int lHeight = Math.max(1, (int)Math.ceil(pHeight));
    return Bitmap.createBitmap(lWidth, lHeight, Bitmap.Config.ARGB_8888);
  }

  /** Renders a monochrome gradient circle, which is rendered at each point position. */
  private static final Bitmap radius(final float pRadius) {
    // Allocate the Bitmap. (Support transparency.)
    final Bitmap      lBitmap = HeatMap.createBitmap(pRadius * 2, pRadius * 2);
    // Wrap the Bitmap in a Canvas so we can draw to it.
    final Canvas      lCanvas = new Canvas(lBitmap);
    // Allocate the Paint.
    final Paint lPaint = new Paint();
    // Ensure we render using a fill.
    lPaint.setStyle(Paint.Style.FILL);
    // Allocate a Radial Gradient for the circle radius.
    final RadialGradient lRadialGradient = new RadialGradient(
      pRadius,
      pRadius,
      pRadius,
      new int[] { Color.BLACK, Color.TRANSPARENT },
      new float[] { 0.0f, 1.0f },
      TileMode.CLAMP
    );
    // Use the RadialGradient to render the intensity of this point.
    lPaint.setShader(lRadialGradient); 
    // Render the sphere within a rectangle.
    lCanvas.drawRect(0, 0, pRadius * 2, pRadius * 2, lPaint); 
    // Return the Bitmap.
    return lBitmap;
  } 

  /** Generates a Bitmap which contains the evaluated Gradient. */
  private static final Bitmap gradient(final Map<Float, Integer> pGradient) {
    // Declare Member Variables.
    final Bitmap        lBitmap    = HeatMap.createBitmap(1, 256);
    final Canvas        lCanvas    = new Canvas(lBitmap);
    final List<Integer> lColors    = new ArrayList<>(pGradient.size());
    // Fetch the Map Entries.
    final List<Float> lKeys = new ArrayList<>(pGradient.keySet());
    // Sort the keys in order.
    Collections
      .sort(
        lKeys
      );
    // Allocate the Buffers to interpolate the gradient threshold.
    final float[] lProgress = new float[lKeys.size()];
    final int[]   lVecColors  = new int[lKeys.size()];
    // Iterate the sorted keys.
    for (int i = 0; i < lKeys.size(); i += 1) {
      // Fetch the progression.
      final Float lKey = lKeys.get(i);
      // Allocate the key.
      lProgress[i] = (float)lKey;
      // Allocate the corresponding color.
      lVecColors[i] = (int)pGradient.get(lKey);
    }
    // Allocate some Paint.
    final Paint lPaint = new Paint();
    // Allocate the Shader.
    final Shader lShader = new LinearGradient(
      0,
      0,
      1,
      256,
      lVecColors,
      lProgress,
      TileMode.CLAMP
    );
    // Use the LinearGradient.
    lPaint.setShader(lShader); 
    // Render the LinearGradient against a rectangle.
    lCanvas.drawRect(0, 0, 1, 256, lPaint); 
    // Return the allocated.
    return lBitmap;
  }

  /** Renders the HeatMap. */
  private static final void draw(final Canvas pCanvas, final Bitmap pBitmap, final Map<String, Spread> pSpreads, final Map<Float, Integer> pGradient, final float pRadius, final float pMax, final float pMinOpacity) {
    // Allocate the circle to render against.
    final Bitmap lCircle = HeatMap.radius(
      pRadius
    );
    final Bitmap lGradient = HeatMap.gradient(
      pGradient
    );

    // Declare a buffer for the interpolated pixels.
    final int[] lInterpolated = new int[256];
    // Fetch the interpolated pixels.
    lGradient.getPixels(lInterpolated, 0, 1, 0, 0, 1, 256);

    // Allocate the Paint instance.
    final Paint lPaint = new Paint(); 
    // Clear the Canvas.
    pCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    // Draw a greyscale heatmap by placing a blurred circle at each data point.
    for (final Entry<String, Spread> lEntry : pSpreads.entrySet()) {
      // Fetch the point data.
      final Spread lPoint = lEntry.getValue();
      // Compute the Alpha to render the circle at.
      final int lAlpha = Math.round(Math.min(Math.max(lPoint.getIntensity() / pMax, pMinOpacity), 1) * 255);
      // Set the Alpha.
      lPaint.setAlpha(lAlpha);
      // Bit-blit the Bitmap contents for this position into the buffer.
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
    // Allocate the Pixels.
    final int[] lPixels = new int[pBitmap.getWidth() * pBitmap.getHeight()];
    // Buffer the Pixel content of the global bitmap into the Pixels.
    pBitmap.getPixels(lPixels, 0, pBitmap.getWidth(), 0, 0, pBitmap.getWidth(), pBitmap.getHeight());
    // Colorize the Pixels.
    HeatMap.colorize(lPixels, lInterpolated);
    // Re-assign the Pixels back to the Bitmap.
    pBitmap.setPixels(lPixels, 0, pBitmap.getWidth(), 0, 0, pBitmap.getWidth(), pBitmap.getHeight());
  }

  /** Applies the gradient threshold to the Bitmap pixel data. */
  private static final void colorize(final int[] pPixels, final int[] pInterpolated) {
    // Iterate the Pixels.
    for (int i = 0; i < pPixels.length; i += 1) {
      // Fetch the current pixel.
      final int lPixel = pPixels[i];
      // Isolate the alpha component. (ARGB)
      final int lIsolated = lPixel & 0xFF000000;
      // Compute the alpha.
      final int lAlpha = 0xFF & (lIsolated >> 24);
      // Fetch the Interpolated Pixel.
      final int j = pInterpolated[lAlpha];
      //// Are we handling a rendered pixel?
      if (lAlpha > 0) {
        // Assign the color for this index.
        pPixels[i] = j;
        // Ensure the opacity is propagated.
        pPixels[i] &= (lIsolated | 0x00FFFFFF);
      }
    }
  }

  /* Member Variables. */
  private       int                 mCanvasWidth;
  private       int                 mCanvasHeight;
  private       float               mMax;
  private       float               mRadius;
  private final Map<Float, Integer> mGradient;
  private final Map<String, Spread> mSpreads;
  private       float               mMinOpacity;

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

    for (int i = 0; i < 2000; i += 1) {
      this.getSpreads().put((""+i), new Spread(
        (float)(Math.random() * 1000),
        (float)(Math.random() * 2000),
        (float)Math.random()
      ));
    }

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

    if (this.getCanvasWidth() > 0 && this.getCanvasHeight() > 0) {
      // TODO: should verify the width and height before attempting to do this.
  
      final Bitmap lBitmap = HeatMap.createBitmap(this.getCanvasWidth(), this.getCanvasHeight());
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
        this.getMinOpacity()
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

  private final Map<Float, Integer> getGradient() {
    return this.mGradient;
  }

  public final void setRadius(final float pRadius) {
    this.mRadius = pRadius;
    this.invalidate();
  }

  private final float getRadius() {
    return this.mRadius;
  }

  public final void setMax(final float pMax) {
    this.mMax = pMax;
    this.invalidate();
  }

  private final float getMax() {
    return this.mMax;
  }

  public final void setMinOpacity(final float pMinOpacity) {
    this.mMinOpacity = pMinOpacity;
    this.invalidate();
  }

  private final float getMinOpacity() {
    return this.mMinOpacity;
  }

}  
