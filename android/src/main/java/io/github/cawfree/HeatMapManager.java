package io.github.cawfree;

import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
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

  @ReactProp(name = "region")
  public final void setRegion(final HeatMap pHeatMap, final ReadableMap pReadableMap) {
    // Have we been supplied with a valid Region?
    if (pReadableMap != null && pReadableMap.hasKey("latitude") && pReadableMap.hasKey("longitude") && pReadableMap.hasKey("latitudeDelta") && pReadableMap.hasKey("longitudeDelta")) {
      pHeatMap.setRegion(
        new double[] {
          pReadableMap.getDouble("latitude"),
          pReadableMap.getDouble("longitude"),
          pReadableMap.getDouble("latitudeDelta"),
          pReadableMap.getDouble("longitudeDelta"),
        }
      );
    } else {
      // Remove the Region.
      pHeatMap.setRegion(null);
    }
  }

  @ReactProp(name = "data")
  public final void setData(final HeatMap pHeatMap, final ReadableArray pReadableArray) {
    if (pReadableArray == null) {
      return;
    }
    // Allocate a HashMap to contain the converted data.
    final List<HeatMap.Spread> lSpreads = new ArrayList<>();
    // Iterate the ReadableArray.
    for (int i = 0; i < pReadableArray.size(); i += 1) {
      // Fetch the point data.
      final ReadableArray lReadableArray = pReadableArray.getArray(i);
      // Allocate a HeatMap.Spread.
      lSpreads.add(
        new HeatMap.Spread(
          (float)lReadableArray.getDouble(0), // x (longitude)
          (float)lReadableArray.getDouble(1), // y (latitude)
          (float)lReadableArray.getDouble(2)  // z (intensity)
        )
      );
    }
    // Buffer into the HeatMap.
    pHeatMap.setSpreads(lSpreads);
  }

}
