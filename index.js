import React from 'react';
import PropTypes from 'prop-types';
import { requireNativeComponent, View } from 'react-native';
import normalizeColor from 'react-native/Libraries/Color/normalizeColor.js';

const NativeHeatMap = requireNativeComponent('HeatMap', null);

class HeatMap extends React.Component {
  render() {
    return (
      <NativeHeatMap
        {...this.props}
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
  data: {
    someBlob: {
      x: 100,
      y: 100,
      z: 1,
    },
  },
  minOpacity: 0.05,
};

export default HeatMap;
