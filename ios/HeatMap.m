#import "HeatMap.h"

@implementation HeatMap

RCT_EXPORT_MODULE()

- (UIView *)view
{
    return [[HeatMapWrapper alloc] init];
}

@end
