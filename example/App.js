import React, { Component } from 'react';
import { PanResponder, Alert, TouchableOpacity, Platform, StyleSheet, Text, View } from 'react-native';
import HeatMap from 'react-native-heat-map';

export default class App extends Component<{}> {
  state = {
    max: 1.0,
    radius: 60,
    data: [
      [
        100,
        100,
        10,
      ],
      [
        200,
        200,
        10,
      ],
    ],
    panResponder: PanResponder
    .create(
      {
        onStartShouldSetPanResponder: () => true,
        onMoveShouldSetPanResponder: () => true,
        onPanResponderMove: ({ nativeEvent }) => {
          const { changedTouches } = nativeEvent;
          this.setState(
            {
              data: [
                ...this.state.data,
                ...changedTouches
                .map(
                  ({ locationX, locationY }) => {
                    return [
                      locationX,
                      locationY,
                      1,
                    ];
                  },
                ),
              ],
            },
          );
        },
        onPanResponderRelease: () => this.setState({
          data: [],
        }),
      },
    ),
  };
  //componentDidMount() {
  //  const t = new Date().getTime();
  //  setInterval(
  //    () => {
  //      this.setState(
  //        {
  //          radius: (this.state.radius + 1) % 60,
  //          data: [
  //            ...this.state.data,
  //            [
  //              Math.random() * 100,
  //              Math.random() * 100,
  //              1,
  //            ],
  //          ],
  //        },
  //      );
  //    },
  //    0,
  //  );
  //}
  render() {
    const {
      max,
      radius,
      data,
      panResponder,
    } = this.state;
    return (
      <View
        style={{
          backgroundColor: 'purple',
          flex: 1,
        }}
      >
        <HeatMap
          {...panResponder.panHandlers}
          style={{
            flex: 1,
          }}
          max={max}
          radius={radius}
          data={data}
        />
      </View>
    );
  }
}
