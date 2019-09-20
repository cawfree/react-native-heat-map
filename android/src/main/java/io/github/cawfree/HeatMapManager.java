package io.github.cawfree;

import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableNativeMap;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Collections;

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

  @ReactProp(name = "minOpacity")
  public final void setMinOpacity(final HeatMap pHeatMap, final float pMinOpacity) {
    // Assign the MinOpacity.
    pHeatMap.setMinOpacity(pMinOpacity);
  }

  @ReactProp(name = "radius")
  public final void setRadius(final HeatMap pHeatMap, final float pRadius) {
    // Assign the Radius.
    pHeatMap.setRadius(Math.max(pRadius, 1f));
  }

  //@ReactProp(name = "gradient")
  //public final void setGradient(final HeatMap pHeatMap, final ReadableMap pReadableMap) {
  //  if (pReadableMap == null) {
  //    return;
  //  }
  //}

  //@ReactProp(name = "data)
  //public final void setData(final HeatMap pHeatMap, final ReadableMap pReadableMap) {
  //  if (pReadableMap == null) {
  //    return;
  //  }
  //  //// Allocate a HashMap to contain the converted data.
  //  //final Map<Float, HeatMap.Spread> lHashMap = new HashMap<>();
  //  //// Iterate the ReadableMap.
  //  //for (final Map.Entry<String, Object> lEntry : pReadableMap.getEntryIterator()) {
  //  //  // Fetch the Key.
  //  //  final String lKey = lEntry.getKey();
  //  //  // Get the ReadableArray.
  //  //  final ReadableArray lReadableArray = pReadableMap.getArray(lKey);
  //  //}
  //}

}
