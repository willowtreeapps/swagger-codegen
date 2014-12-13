#import <Foundation/Foundation.h>
#import "SWGObject.h"

@interface SWGTag : SWGObject

@property(nonatomic) NSNumber *tagId;  
@property(nonatomic) NSString *name;  

- (instancetype)initWithTagId:(NSNumber *)tagId name:(NSString *)name NS_DESIGNATED_INITIALIZER;
- (id)initWithValues:(NSDictionary *)dict;
- (NSDictionary *)asDictionary;

@end
