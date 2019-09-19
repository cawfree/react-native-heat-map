package io.github.cawfree;

import android.os.Bundle;  
import android.app.Activity;  
import android.view.Menu;  
import android.content.Context;  
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.Paint;  
import android.view.View;  
import android.view.ViewGroup.LayoutParams;

public class HeatMap extends View {  

  /* Member Variables. */
  private int mWidth;
  private int mHeight;

  public HeatMap(final Context pContext) {
    super(pContext);
    this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
  }

  @Override
  protected void onSizeChanged(final int pWidth, final int pHeight, final int pPrevWidth, final int pPrevHeight) {
    this.mWidth = pWidth;
    this.mHeight = pHeight;
    super.onSizeChanged(
      pWidth,
      pHeight,
      pPrevWidth,
      pPrevHeight
    );
  }

  @Override 
  protected void onDraw(final Canvas canvas) {
    super.onDraw(canvas);  

    //canvas.drawPaint(paint2); 
    //canvas.drawColor(0xFF0000FF);

    // custom drawing code here  
    Paint paint = new Paint();  
    paint.setStyle(Paint.Style.FILL);  

    // make the entire canvas white  
    paint.setColor(Color.RED);  
    canvas.drawPaint(paint);  
    //  
    //// draw blue circle with anti aliasing turned off  
    //paint.setAntiAlias(false);  
    //paint.setColor(Color.BLUE);  
    //canvas.drawCircle(20, 20, 15, paint);  

    //// draw green circle with anti aliasing turned on  
    //paint.setAntiAlias(true);  
    //paint.setColor(Color.GREEN);  
    //canvas.drawCircle(60, 20, 15, paint);  

    //// draw red rectangle with anti aliasing turned off  
    //paint.setAntiAlias(false);  
    //paint.setColor(Color.RED);  
    //canvas.drawRect(100, 5, 200, 30, paint);  
    //               
    //          
    //paint.setStyle(Paint.Style.FILL);  
    //canvas.drawText("Graphics Rotation", 40, 180, paint);  

  }  

}  
