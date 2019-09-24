#import "HeatMap.h"
#import "HeatMapWrapper.h"

@implementation HeatMap

RCT_EXPORT_MODULE()

- (UIView *)view
{
    return _heatMapWrapper = [[HeatMapWrapper alloc] init];
}

RCT_CUSTOM_VIEW_PROPERTY(region, NSDictionary, HeatMapWrapper)
{
    NSDictionary *dict = [RCTConvert NSDictionary:json];
    if (dict && dict[@"latitude"] && dict[@"longitude"] && dict[@"latitudeDelta"] && dict[@"longitudeDelta"]) {
        [_heatMapWrapper shouldUpdateRegion:dict];
    } else {
        [_heatMapWrapper shouldUpdateRegion:nil];
    }
    [_heatMapWrapper shouldRenderHeatMap];
}

RCT_CUSTOM_VIEW_PROPERTY(data, NSMutableArray, HeatMapWrapper)
{
    NSArray *array = [RCTConvert NSArray:json];
    [_heatMapWrapper shouldUpdateData:array];
    [_heatMapWrapper shouldRenderHeatMap];
}

RCT_CUSTOM_VIEW_PROPERTY(radius, CGFloat, HeatMapWrapper)
{
    [_heatMapWrapper shouldRenderHeatMap];
}

RCT_CUSTOM_VIEW_PROPERTY(max, CGFloat, HeatMapWrapper)
{
    [_heatMapWrapper shouldRenderHeatMap];
}

RCT_CUSTOM_VIEW_PROPERTY(minOpacity, CGFloat, HeatMapWrapper)
{
    [_heatMapWrapper shouldRenderHeatMap];
}

@end
