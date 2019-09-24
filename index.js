import React from 'react';
import PropTypes from 'prop-types';
import { PixelRatio, requireNativeComponent, View } from 'react-native';
import normalizeColor from 'react-native/Libraries/Color/normalizeColor.js';

const NativeHeatMap = requireNativeComponent('HeatMap', null);

class HeatMap extends React.Component {
  setNativeProps(...rest) {
    const { nativeHeatMap } = this.refs;
    return nativeHeatMap
      .setNativeProps(
        ...rest,
      );
  }
  render() {
    const {
      data,
      radius,
      region,
      ...extraProps
    } = this.props;
    const transformedData = !region ? (
      data.map(
        ([ x, y, ...extras ]) => (
          [
            x * PixelRatio.get(),
            y * PixelRatio.get(),
            ...extras,
          ]
        ),
      )
    ) : data;
    return (
      <NativeHeatMap
        {...extraProps}
        ref="nativeHeatMap"
        radius={radius * PixelRatio.get()}
        data={transformedData}
        region={region}
      />
    );
  }
}

HeatMap.propTypes = {
  ...View.propTypes,
  max: PropTypes.number,
  radius: PropTypes.number,
  gradient: PropTypes.shape({}),
  data: PropTypes.shape({}),
  minOpacity: PropTypes.number,
  region: PropTypes.shape({}),
};

HeatMap.defaultProps = {
  ...View.defaultProps,
  max: 1.0,
  radius: 25.0,
  gradient: {
    0.4: 'blue',
    0.6: 'cyan',
    0.7: 'green',
    0.8: 'yellow',
    1.0: 'red',
  },
  data: [
    [
      100,
      100,
      1,
    ],
  ],
  minOpacity: 0.05,
  region: {},
};

export default HeatMap;
