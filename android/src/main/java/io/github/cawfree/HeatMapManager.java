package io.github.cawfree;

import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableNativeMap;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

@ReactModule(name = HeatMapManager.REACT_CLASS)
public class HeatMapManager extends SimpleViewManager<HeatMap> {

  public static final String REACT_CLASS = "HeatMap";

  @Override
  public final HeatMap createViewInstance(final ThemedReactContext pThemedReactContext) {
    return new HeatMap(pThemedReactContext);
  }
  
  @Override
  public final String getName() {
    return REACT_CLASS;
  }

  @ReactProp(name = "max")
  public final void setMax(final HeatMap pHeatMap, final float pMax) {
    // Assign the Max value.
    pHeatMap.setMax(pMax);
  }

}
