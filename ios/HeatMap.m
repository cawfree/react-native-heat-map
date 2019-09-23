#import "HeatMap.h"
#import "LFHeatMap.h"

@implementation HeatMap

RCT_EXPORT_MODULE()

- (UIView *)view
{
    
    HeatMapWrapper *heatMapWrapper = [[HeatMapWrapper alloc] init];
//    UIImageView* imgview = [[UIImageView alloc] init];
//    CGRect heatMapWithRect = CGRectMake(10, 10, 300, 400);
//    float boost = 1.0f;
//
//    NSArray *weights = @[@20];
//    NSArray *points = [NSArray arrayWithObjects:[NSValue valueWithCGPoint:CGPointMake(30.0, 180.0)], nil];
//
//    UIImage *heatmap = [LFHeatMap heatMapWithRect:heatMapWithRect boost:boost points:points weights:weights];
//
//    [imgview setImage:heatmap];
//
    
    [heatMapWrapper setContentMode:UIViewContentModeScaleAspectFit];
    [heatMapWrapper sizeToFit];
    [heatMapWrapper setBackgroundColor:[UIColor colorWithRed:1.0f green:0.0f blue:0.0f alpha:1.0f]];

    return heatMapWrapper;
}

@end
