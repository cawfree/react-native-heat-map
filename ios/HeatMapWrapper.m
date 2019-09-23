#import <Foundation/Foundation.h>
#import "HeatMapWrapper.h"
#import "LFHeatMap.h"

@implementation HeatMapWrapper

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        _imageView = [[UIImageView alloc] initWithFrame:frame];
        
        _imageView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
        
        [self addSubview:_imageView];
    }
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    
    float width = _imageView.bounds.size.width;
    float height = _imageView.bounds.size.height;
    
    CGRect heatMapWithRect = CGRectMake(0, 0, width, height);
    float boost = 1.0f;
    
    NSArray *weights = @[@20];
    NSArray *points = [NSArray arrayWithObjects:[NSValue valueWithCGPoint:CGPointMake(width * 0.5f, height * 0.5f)], nil];
    
    UIImage *heatmap = [LFHeatMap heatMapWithRect:heatMapWithRect boost:boost points:points weights:weights];
    
    [_imageView setImage:heatmap];
}

@end
