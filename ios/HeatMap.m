#import "HeatMap.h"
#import "HeatMapWrapper.h"

@implementation HeatMap

RCT_EXPORT_MODULE()

- (UIView *)view
{
    return _heatMapWrapper = [[HeatMapWrapper alloc] init];
}

//RCT_EXPORT_VIEW_PROPERTY(radius, CGFloat);
//RCT_EXPORT_VIEW_PROPERTY(max, CGFloat);
//RCT_EXPORT_VIEW_PROPERTY(minOpacity, CGFloat);

RCT_CUSTOM_VIEW_PROPERTY(data, NSDictionary, HeatMapWrapper)
{
    
    NSDictionary *dictionary = [RCTConvert NSDictionary:json];
    NSLog(@"%@", dictionary);
}
//
//RCT_CUSTOM_VIEW_PROPERTY(gradient, NSDictionary, HeatMapWrapper)
//{
//    
//}

@end
