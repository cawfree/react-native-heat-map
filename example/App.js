import React, { Component } from 'react';
import { Platform, StyleSheet, Text, View } from 'react-native';
import HeatMap from 'react-native-heat-map';

export default class App extends Component<{}> {
  state = {
    max: 1.0,
    radius: 1,
  };
  componentDidMount() {
    const t = new Date().getTime();
    setInterval(
      () => {
        this.setState(
          {
            radius: (this.state.radius + 1) % 60,
          },
        );
      },
      0,
    );
  }
  render() {
    const {
      max,
      radius,
    } = this.state;
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
          max={max}
          radius={radius}
        />
      </View>
    );
  }
}
