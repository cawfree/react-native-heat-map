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
        _region = nil;
        
        [self addSubview:_imageView];
    }
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    _imageView.bounds = self.bounds;
    [self shouldRenderHeatMap];
}

- (void)shouldUpdateData:(NSArray*)data {
    _data = data;
}

- (void)shouldUpdateRegion:(NSDictionary*)region {
    if (region != nil) {
        _region = region;
    } else {
        _region = nil;
    }
}

- (float)getScreenYRelative:(float)latitudeInDegrees {
    return (float)log(tan(latitudeInDegrees / 360.0f * M_PI + M_PI / 4));
}

- (float)getScreenY:(int)mapScreenHeight withLatitudeInDegrees: (float)latitudeInDegrees withTopLatitudeRelative: (float)topLatitudeRelative withBottomLatitudeRelative: (float)bottomLatitudeRelative {
    float relativeLatitude = [self getScreenYRelative:latitudeInDegrees];
    return mapScreenHeight * (relativeLatitude - topLatitudeRelative) / (bottomLatitudeRelative - topLatitudeRelative);
}

- (float)getRadians:(float)deg {
    return (float)(deg * M_PI / 180);
}

- (float)getScreenX:(float)longitudeInDegrees withMapScreenWidth:(int)mapScreenWidth withRightLongitudeRadians:(float)rightLongitudeRadians withLeftLongitudeRadians:(float)leftLongitudeRadians {
    float longitudeInRadians = [self getRadians:longitudeInDegrees];
    return mapScreenWidth * (longitudeInRadians - leftLongitudeRadians) / (rightLongitudeRadians - leftLongitudeRadians);
}

- (void)shouldRenderHeatMap {
    
    float boost = 1.0f;
    float mapScreenWidth = _imageView.bounds.size.width;
    float mapScreenHeight = _imageView.bounds.size.height;
    
    if (mapScreenWidth > 0 && mapScreenHeight > 0 && _data != nil) {
        NSUInteger count = [_data count];
        NSMutableArray* points = [NSMutableArray arrayWithCapacity:count];
        NSMutableArray* weights = [NSMutableArray arrayWithCapacity:count];
        
        for (NSUInteger index = 0; index < count ; index++) {
            
            NSArray *n = [_data objectAtIndex:index];
            
            float x = [[n objectAtIndex:0] doubleValue];
            float y = [[n objectAtIndex:1] doubleValue];
            float intensity = [[n objectAtIndex:2] doubleValue];
            
            if (_region) {
                float latitude = [[_region valueForKey:@"latitude"] doubleValue];
                float longitude = [[_region valueForKey:@"longitude"] doubleValue];
                float latitudeDelta = [[_region valueForKey:@"latitudeDelta"] doubleValue];
                float longitudeDelta = [[_region valueForKey:@"longitudeDelta"] doubleValue];
                float topLatitude = latitude + (latitudeDelta * 0.5f);
                float bottomLatitude = latitude - (latitudeDelta * 0.5f);
                float leftLongitude = longitude - (longitudeDelta * 0.5f);
                float rightLongitude = longitude + (longitudeDelta * 0.5f);

                float topLatitudeRelatve = [self getScreenYRelative:topLatitude];
                float bottomLatitudeRelative = [self getScreenYRelative:bottomLatitude];
                float leftLongitudeRadians = [self getRadians:leftLongitude];
                float rightLongitudeRadians = [self getRadians:rightLongitude];

                float px = [self getScreenX:x withMapScreenWidth:mapScreenWidth withRightLongitudeRadians:rightLongitudeRadians withLeftLongitudeRadians:leftLongitudeRadians];

                float py = [self getScreenY:mapScreenHeight
                      withLatitudeInDegrees:y
                    withTopLatitudeRelative:topLatitudeRelatve withBottomLatitudeRelative:bottomLatitudeRelative];

                NSLog(@"got %f, %f for %f %f", px, py, x, y);

                [points addObject:@(CGPointMake(px, py))];
                [weights addObject:@(intensity)];
            } else {
                [points addObject:@(CGPointMake(x, y))];
                [weights addObject:@(intensity)];
            }
            
        }
        
        CGRect heatMapWithRect = CGRectMake(0, 0, mapScreenWidth, mapScreenHeight);
        
        UIImage *heatmap = [LFHeatMap heatMapWithRect:heatMapWithRect
                                                boost:boost
                                               points:points
                                              weights:weights];
        [_imageView setImage:heatmap];
        
    } else {
        NSLog(@"cannot render");
        [_imageView setImage:nil];
    }
    
    
}

@end
