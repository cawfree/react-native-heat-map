#ifndef HeatMapWrapper_h
#define HeatMapWrapper_h

#import <UIKit/UIKit.h>
#import <LFHeatMap.h>

@interface HeatMapWrapper : UIView

@property (strong, nonatomic) UIImageView *imageView;
@property (strong, nonatomic) NSMutableArray *points;
@property (strong, nonatomic) NSMutableArray *weights;

- (void)shouldRenderHeatMap;
- (void)shouldUpdateData:(NSArray*)data;

@end

#endif
