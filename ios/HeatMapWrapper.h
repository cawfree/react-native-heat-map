#ifndef HeatMapWrapper_h
#define HeatMapWrapper_h

#import <UIKit/UIKit.h>
#import <LFHeatMap.h>

@interface HeatMapWrapper : UIView

@property (strong, nonatomic) UIImageView *imageView;
@property (strong, nonatomic) NSDictionary *region;
@property (strong, nonatomic) NSArray *data;

- (void)shouldRenderHeatMap;
- (void)shouldUpdateData:(NSArray*)data;
- (void)shouldUpdateRegion:(NSDictionary*)region;

@end

#endif
