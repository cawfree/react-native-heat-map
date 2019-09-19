package io.github.cawfree;

import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableNativeMap;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

public class HeatMapManager extends SimpleViewManager<View> {

  public static final String REACT_CLASS = "HeatMap";

  /* Member Variables. */ 

  @Override
  public final View createViewInstance(final ThemedReactContext pThemedReactContext) {
    return new HeatMap(pThemedReactContext);
  }
  
  @Override
  public final String getName() {
    return REACT_CLASS;
  }

}
