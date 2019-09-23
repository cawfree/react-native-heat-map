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
        
        _weights = [NSMutableArray arrayWithCapacity:1];
        _points = [NSMutableArray arrayWithCapacity:1];
        
        [self addSubview:_imageView];
    }
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    [self shouldRenderHeatMap];
}

- (void)shouldUpdateData:(NSArray*)data {
    NSUInteger count = [data count];
    [_points removeAllObjects];
    [_weights removeAllObjects];
    for (NSUInteger index = 0; index < count ; index++) {
        NSArray *n = [data objectAtIndex:index];
        float x = [[n objectAtIndex:0] doubleValue];
        float y = [[n objectAtIndex:1] doubleValue];
        float intensity = [[n objectAtIndex:2] doubleValue];
        
        [_points addObject:@(CGPointMake(x, y))];
        [_weights addObject:@(intensity)];
        
//        NSLog(@"got x %f, y %f, intensity %f", 100, 100, intensity);
        
        
//        id x = [data objectAtIndex:index];
        
//        CGFloat x = ;
//        for(int i = 0; i < 10; i++) {
//            [myArray addObject:@(i)];
//        }

//        NSLog(@"got data %d", index);
        //[self doSomethingWith:[myArray objectAtIndex:index]];
    }
}

- (void)shouldRenderHeatMap {
    float boost = 1.0f;
    float width = _imageView.bounds.size.width;
    float height = _imageView.bounds.size.height;
    
    if (width > 0 && height > 0) {
        CGRect heatMapWithRect = CGRectMake(0, 0, width, height);
        
//        _weights = @[@20];
//        _points = [NSArray arrayWithObjects:[], nil];
        
        UIImage *heatmap = [LFHeatMap heatMapWithRect:heatMapWithRect
                                                boost:boost
                                               points:_points
                                              weights:_weights];
        [_imageView setImage:heatmap];
    } else {
        [_imageView setImage:nil];
    }
    
    
}

@end
