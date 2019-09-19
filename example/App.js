import React, { Component } from 'react';
import { Platform, StyleSheet, Text, View } from 'react-native';
import HeatMap from 'react-native-heat-map';

export default class App extends Component<{}> {
  render() {
    return (
      <HeatMap
        style={{
          flex: 1,
        }}
      />
    );
  }
}
