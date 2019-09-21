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
import android.graphics.Rect;
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

  private static final int DOWNSAMPLE = 16;

  /* Base Declarations. */
  private static String TAG = "HeatMap";

  /** A logging helper method. */
  private static void log(final String pMessage) {
    Log.d(TAG, pMessage);
  }

  /** Represents the parameters of a spread. */
  public static final class Spread {
    /* Member Variables.  */
    private final float mX;
    private final float mY;
    private final float mIntensity;
    /* Constructor. */
    public Spread(final float pX, final float pY, final float pIntensity) {
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
    return Bitmap.createBitmap(lWidth, lHeight, Bitmap.Config.ARGB_4444);
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
  private static final void draw(final Canvas pCanvas, final Bitmap pBitmap, final Bitmap pCircle, final Bitmap pInterpolated, final List<Spread> pSpreads, final Map<Float, Integer> pGradient, final float pRadius, final float pMax, final float pMinOpacity, final int[] pBuffer, final int[] pPixels, final double[] pRegion) {
    // Fetch the interpolated pixels.
    pInterpolated.getPixels(pBuffer, 0, 1, 0, 0, 1, 256);
    // Allocate the Paint instance.
    final Paint lPaint = new Paint(); 
    // Clear the Canvas.
    pCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    // Compute the inverse of the maximum.
    final float lInvMax = 1 / pMax;
    // Draw a greyscale heatmap by placing a blurred circle at each data point.
    for (final Spread lPoint : pSpreads) {
      // Compute the Alpha to render the circle at.
      final int lAlpha = Math.round(
        Math.min(
          Math.max(
            lPoint.getIntensity() * lInvMax,
            pMinOpacity
          ),
          1
        ) * 255);
      // Set the Alpha.
      lPaint.setAlpha(lAlpha);
      // Should we scale the supplied co-ordinates?
      if (pRegion != null) {
        // Fetch region data.
        final double lLatitude = pRegion[0];
        final double lLongitude = pRegion[1];
        final double lLatitudeDelta = pRegion[2];
        final double lLongitudeDelta = pRegion[3];
        // Allocate a MercatorMap.
        final MercatorMap lMercatorMap = new MercatorMap(
          pBitmap.getWidth(),
          pBitmap.getHeight(),
          (float)(lLatitude + (lLatitudeDelta * 0.5f)),
          (float)(lLatitude - (lLatitudeDelta * 0.5f)),
          (float)(lLongitude - (lLongitudeDelta * 0.5f)),
          (float)(lLongitude + (lLongitudeDelta * 0.5f))
        );
        // Fetch the corresponding X and Y.
        final float lScreenX = lMercatorMap
            .getScreenX(lPoint.getX());
        final float lScreenY = lMercatorMap
            .getScreenY(lPoint.getY());
        pCanvas
          .drawBitmap(
            pCircle,
            // TODO: top left corner defines render position
            lScreenX - pRadius, // does the radius need to be scaled?
            lScreenY - pRadius,
            // Draw using the supplied Paint.
            lPaint
          );

      } else {
        // Bit-blit the Bitmap contents for this position into the buffer.
        pCanvas
          .drawBitmap(
            pCircle,
            // TODO: top left corner defines render position
            (lPoint.getX() / DOWNSAMPLE) - pRadius,
            (lPoint.getY() / DOWNSAMPLE) - pRadius,
            // Draw using the supplied Paint.
            lPaint
          );
      }
    }
    // Buffer the Pixel content of the global bitmap into the Pixels.
    pBitmap.getPixels(pPixels, 0, pBitmap.getWidth(), 0, 0, pBitmap.getWidth(), pBitmap.getHeight());
    // Colorize the Pixels.
    HeatMap.colorize(pPixels, pBuffer);
    // Re-assign the Pixels back to the Bitmap.
    pBitmap.setPixels(pPixels, 0, pBitmap.getWidth(), 0, 0, pBitmap.getWidth(), pBitmap.getHeight());
  }

  /** Applies the gradient threshold to the Bitmap pixel data. */
  private static final void colorize(final int[] pPixels, final int[] pInterpolated) {
    int lPixel;
    int lIsolated;
    int lAlpha;
    int j;
    // Iterate the Pixels.
    for (int i = 0; i < pPixels.length; i += 1) {
      // Fetch the current pixel.
      lPixel = pPixels[i];
      // Isolate the alpha component. (ARGB)
      lIsolated = lPixel & 0xFF000000;
      // Compute the alpha.
      lAlpha = 0xFF & (lIsolated >> 24);
      // Are we handling a rendered pixel?
      if (lAlpha > 0) {
        // Fetch the Interpolated Pixel.
        j = pInterpolated[lAlpha];
        // Assign the color for this index.
        pPixels[i] = j & (lIsolated | 0x00FFFFFF);
      }
    }
  }

  /* Member Variables. */
  private       int                 mCanvasWidth;
  private       int                 mCanvasHeight;
  private       float               mMax;
  private       float               mRadius;
  private final Map<Float, Integer> mGradient;
  private final List<Spread>        mSpreads;
  private       float               mMinOpacity;
  private       Bitmap              mCircle;
  private       Bitmap              mBitmap;
  private       Bitmap              mInterpolated;
  private final int[]               mBuffer;
  private       int[]               mPixels;
  private       double[]            mRegion;

  /* Constructor. */
  public HeatMap(final Context pContext) {
    // Implement the parent.
    super(pContext);
    // Enable Hardware Acceleration.
    this.setLayerType(
      View.LAYER_TYPE_HARDWARE,
      null
    );
    // Initialize Member Variables.
    this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    this.mCanvasWidth  = 0;
    this.mCanvasHeight = 0;
    this.mMax          = HeatMap.DEFAULT_MAX;
    this.mRadius       = HeatMap.DEFAULT_RADIUS;
    this.mGradient     = HeatMap.DEFAULT_GRADIENT;
    this.mSpreads      = new ArrayList<>();
    this.mMinOpacity   = HeatMap.DEFAULT_MIN_OPACITY;
    this.mCircle       = null;
    this.mBitmap       = null;
    this.mInterpolated = HeatMap.gradient(HeatMap.DEFAULT_GRADIENT);
    this.mBuffer       = new int[256];
    this.mPixels       = new int[0];
    this.mRegion       = null;
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
    /// Fetch the Circle.
    final Bitmap lCircle = this.getCircle();

    if (this.getCanvasWidth() > 0 && this.getCanvasHeight() > 0 && lCircle != null) {
      // Attempt to fetch the existing Bitmap.
      Bitmap lBitmap = this.getBitmap();
      // Do we need to make a new Bitmap?
      if (lBitmap == null || this.getCanvasWidth() / DOWNSAMPLE != lBitmap.getWidth() || this.getCanvasHeight() / DOWNSAMPLE != lBitmap.getHeight()) {
        // Assign the Bitmap;
        lBitmap = HeatMap.createBitmap(
          this.getCanvasWidth() / DOWNSAMPLE,
          this.getCanvasHeight() / DOWNSAMPLE
        );
        this.setPixels(
          new int[(this.getCanvasWidth() / DOWNSAMPLE) * (this.getCanvasHeight() / DOWNSAMPLE)]
        );
        this.setBitmap(
          lBitmap
        );
      }

      final Canvas lCanvas = new Canvas(lBitmap);
  
      // Render the Heatmap to the local Canvas.
      HeatMap.draw(
        lCanvas,
        lBitmap,
        lCircle,
        this.getInterpolated(),
        this.getSpreads(),
        this.getGradient(),
        this.getRadius() / DOWNSAMPLE,
        this.getMax(),
        this.getMinOpacity(),
        this.getBuffer(),
        this.getPixels(),
        this.getRegion()
      );
  
      // Write the resulting bitmap to the global Canvas.
      pCanvas.drawBitmap(
        lBitmap,
        new Rect(
          0,
          0,
          this.getCanvasWidth() / DOWNSAMPLE,
          this.getCanvasHeight() / DOWNSAMPLE
        ),
        new Rect(
          0,
          0,
          this.getCanvasWidth(),
          this.getCanvasHeight()
        ),
        null
      );
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

  public final void setSpreads(final List<HeatMap.Spread> pSpreads) {
    // Clear the existing Spreads.
    this.getSpreads().clear();
    // Add all the existing Spreads.
    this.getSpreads().addAll(pSpreads);
    // Redraw.
    this.invalidate();
  }

  private final List<Spread> getSpreads() {
    return this.mSpreads;
  }

  private final Map<Float, Integer> getGradient() {
    return this.mGradient;
  }

  public final void setRadius(final float pRadius) {
    this.mRadius = pRadius;
    if (this.getCircle() != null) {
      this.getCircle().recycle();
    }
    this.setCircle(
      HeatMap.radius(
        pRadius / DOWNSAMPLE
      )
    );
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

  private final void setCircle(final Bitmap pBitmap) {
    this.mCircle = pBitmap;
  }

  private final Bitmap getCircle() {
    return this.mCircle;
  }

  private final void setBitmap(final Bitmap pBitmap) {
    if (this.getBitmap() != null) {
      this.getBitmap().recycle();
    }
    this.mBitmap = pBitmap;
  }

  private final Bitmap getBitmap() {
    return this.mBitmap;
  }

  private final void setInterpolated(final Bitmap pBitmap) {
    if (this.getInterpolated() != null) {
      this.getInterpolated().recycle();
    }
    this.mInterpolated = pBitmap;
  }

  private final Bitmap getInterpolated() {
    return this.mInterpolated;
  }

  private final int[] getBuffer() {
    return this.mBuffer;
  }

  private final void setPixels(final int[] pPixels) {
    this.mPixels = pPixels;
  }

  private final int[] getPixels() {
    return this.mPixels;
  }

  public final void setRegion(final double[] pRegion) {
    this.mRegion = pRegion;
    this.invalidate();
  }

  private final double[] getRegion() {
    return this.mRegion;
  }

}  
