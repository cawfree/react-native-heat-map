import React, { Component } from 'react';
import { PixelRatio, Animated, PanResponder, Alert, TouchableOpacity, Platform, StyleSheet, Text, View } from 'react-native';
import HeatMap from 'react-native-heat-map';

const data = [];

export default class App extends Component<{}> {
  state = {
    animValue: new Animated.Value(
      0,
    ),
    max: 1.0,
    radius: 60,
    data: [
      [
        -3.0118499, // longitude
        53.4139281, // latitude
        10, // intensity
      ],
      //[
      //  100,
      //  100,
      //  10,
      //],
      //[
      //  200,
      //  200,
      //  10,
      //],
    ],
    panResponder: PanResponder
    .create(
      {
        onStartShouldSetPanResponder: () => true,
        onMoveShouldSetPanResponder: () => true,
        onPanResponderMove: ({ nativeEvent }) => {
          const { changedTouches } = nativeEvent;
          //data.push(
          //  ...changedTouches
          //    .map(
          //       ({ locationX, locationY }) => {
          //         return [
          //           locationX * PixelRatio.get(),
          //           locationY * PixelRatio.get(),
          //           1,
          //         ];
          //       },
          //     ),
   
          //);
          //this.refs.heatMap
          //  .setNativeProps(
          //    {
          //      data: [...data],
          //      radius: 60 * PixelRatio.get(),
          //    },
          //  );
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
  componentDidMount() {
    const { heatMap } = this.refs;
    //const { animValue } = this.state;
    //animValue.addListener(
    //  ({ value }) => (
    //    heatMap
    //      .setNativeProps(
    //        {
    //          radius: value * 60,
    //          max: value,
    //        },
    //      )
    //  ),
    //);
    //Animated
    //  .timing(
    //    animValue,
    //    {
    //      toValue: 1,
    //      useNativeDriver: true,
    //      duration: 3000,
    //    },
    //  )
    //  .start();
    //const t = new Date().getTime();
    //setInterval(
    //  () => {

    //    //this.setState(
    //    //  {
    //    //    radius: (this.state.radius + 1) % 60,
    //    //    data: [
    //    //      ...this.state.data,
    //    //      [
    //    //        Math.random() * 100,
    //    //        Math.random() * 100,
    //    //        1,
    //    //      ],
    //    //    ],
    //    //  },
    //    //);
    //  },
    //  0,
    //);
  }
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
          ref="heatMap"
          {...panResponder.panHandlers}
          style={{
            flex: 1,
          }}
          max={max}
          radius={radius}
          data={data} 
          region={{
            longitude: -3.0118499,
            latitude: 53.4139281,
            latitudeDelta: 0.2,
            longitudeDelta: 0.2,
          }}
        />
      </View>
    );
  }
}


