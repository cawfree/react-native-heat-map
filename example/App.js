import React, { Component } from 'react';
import { Platform, StyleSheet, Text, View } from 'react-native';
import HeatMap from 'react-native-heat-map';

export default class App extends Component<{}> {
  state = {
    max: 1.0,
  };
  componentDidMount() {
    const t = new Date().getTime();
    setInterval(
      () => {
        this.setState(
          {
            max: Math.sin(((new Date().getTime() - t) / 1000) * Math.PI * 2),
          },
        );
      },
      0,
    );
  }
  render() {
    const { max } = this.state;
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
        />
      </View>
    );
  }
}
