#import "SWGDate.h"
#import "SWGCategory.h"

@implementation SWGCategory

- (instancetype)initWithCategoryId:(NSNumber *)categoryId name:(NSString *)name
{
    self = [super init];
    if (self)
    {
        _categoryId = categoryId;
        _name = name;
        
    }

    return self;
}

- (id)initWithValues:(NSDictionary*)dict
{
    NSNumber * categoryId = dict[@"id"];
    NSString * name = dict[@"name"];
    
    return [self initWithCategoryId:categoryId name:name];
}

- (NSDictionary *)asDictionary {
    NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
    
    if(_categoryId != nil)
    {
        dict[@"id"] = _categoryId;
    }
    
    if(_name != nil)
    {
        dict[@"name"] = _name;
    }
    NSDictionary* output = [dict copy];
    return output;
}

@end
