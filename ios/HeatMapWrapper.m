#import <Foundation/Foundation.h>
#import "HeatMapWrapper.h"
#import "LFHeatMap.h"

@implementation HeatMapWrapper

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        
        CGRect imageRect = CGRectMake(0, 0, 100, 100);
        
        UIView *imageView = [[UIView alloc] initWithFrame:imageRect];
        
        [imageView setContentMode:UIViewContentModeScaleAspectFit];
        [imageView sizeToFit];
        [imageView setBackgroundColor:[UIColor colorWithRed:1.0f green:0.0f blue:0.0f alpha:1.0f]];
        [self addSubview:imageView];
//        [imageView release];
        
        
        //[self setBackgroundColor:[UIColor colorWithRed:1.0f green:0.0f blue:0.0f alpha:1.0f]];
        
//        CGRect viewRect = CGRectMake(0, 0, 100, 100);
//
//        _imageView = [[UIImageView alloc] initWithFrame:viewRect];
//
//        [_imageView setContentMode:UIViewContentModeScaleAspectFit];
//        [_imageView sizeToFit];
//
////        float width = 100;//self.bounds.size.width;
////        float height = 100;//self.bounds.size.height;
////
////        CGRect heatMapWithRect = CGRectMake(0, 0, width, height);
////        float boost = 1.0f;
////
////        NSArray *weights = @[@20];
////        NSArray *points = [NSArray arrayWithObjects:[NSValue valueWithCGPoint:CGPointMake(10, 10)], nil];
////
////        UIImage *heatmap = [LFHeatMap heatMapWithRect:heatMapWithRect boost:boost points:points weights:weights];
////
////        [_imageView setImage:heatmap];
//        [_imageView setBackgroundColor:[UIColor colorWithRed:1.0f green:0.0f blue:0.0f alpha:1.0f]];
//        [self addSubview:_imageView];
    }
    return self;
}



//
//- (void)layoutSubviews {
//    [super layoutSubviews];
//    
//    
//}

@end
