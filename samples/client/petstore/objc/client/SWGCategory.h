#import <Foundation/Foundation.h>
#import "SWGObject.h"

@interface SWGCategory : SWGObject

@property(nonatomic) NSNumber *categoryId;  
@property(nonatomic) NSString *name;  

- (instancetype)initWithCategoryId:(NSNumber *)categoryId name:(NSString *)name NS_DESIGNATED_INITIALIZER;
- (id)initWithValues:(NSDictionary *)dict;
- (NSDictionary *)asDictionary;

@end
