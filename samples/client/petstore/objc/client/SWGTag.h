#import <Foundation/Foundation.h>
#import "SWGObject.h"

@interface SWGTag : SWGObject

@property(nonatomic) NSNumber *tagId;  
@property(nonatomic) NSString *name;  

- (id)tagId:(NSNumber *)tagId name:(NSString *)name;
- (id)initWithValues:(NSDictionary *)dict;
- (NSDictionary *)asDictionary;

@end
