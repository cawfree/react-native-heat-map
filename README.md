# react-native-heat-map

## Getting started

`$ npm install react-native-heat-map --save`

### Mostly automatic installation

`$ react-native link react-native-heat-map`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-heat-map` and add `HeatMap.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libHeatMap.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import io.github.cawfree.HeatMapPackage;` to the imports at the top of the file
  - Add `new HeatMapPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-heat-map'
  	project(':react-native-heat-map').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-heat-map/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-heat-map')
  	```


## Usage
```javascript
import HeatMap from 'react-native-heat-map';

// TODO: What to do with the module?
HeatMap;
```
