#import <Foundation/Foundation.h>
#import "SWGObject.h"

@interface SWGCategory : SWGObject

@property(nonatomic) NSNumber *categoryId;  
@property(nonatomic) NSString *name;  

- (id)categoryId:(NSNumber *)categoryId name:(NSString *)name;
- (id)initWithValues:(NSDictionary *)dict;
- (NSDictionary *)asDictionary;

@end
