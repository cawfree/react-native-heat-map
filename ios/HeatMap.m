#import "HeatMap.h"

@implementation HeatMap

RCT_EXPORT_MODULE()

- (UIView *)view
{
    
    HeatMapWrapper *heatMapWrapper = [[HeatMapWrapper alloc] init];
    
    [heatMapWrapper setContentMode:UIViewContentModeScaleAspectFit];
    [heatMapWrapper sizeToFit];
    [heatMapWrapper setBackgroundColor:[UIColor colorWithRed:0.0f green:1.0f blue:0.0f alpha:1.0f]];

    return heatMapWrapper;
}

@end
