import React, { Component } from 'react';
import { Platform, StyleSheet, Text, View } from 'react-native';
import HeatMap from 'react-native-heat-map';

export default class App extends Component<{}> {
  render() {
    return (
      <View
        style={{
          backgroundColor: 'purple',
          flex: 1,
        }}
      >
        <HeatMap
          style={{
            flex: 1,
          }}
        />
      </View>
    );
  }
}
