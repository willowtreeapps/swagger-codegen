#import "SWGDate.h"
#import "SWGTag.h"

@implementation SWGTag

- (instancetype)initWithTagId:(NSNumber *)tagId name:(NSString *)name
{
    self = [super init];
    if (self)
    {
        _tagId = tagId;
        _name = name;
        
    }

    return self;
}

- (id)initWithValues:(NSDictionary*)dict
{
    NSNumber * tagId = dict[@"id"];
    NSString * name = dict[@"name"];
    
    return [self initWithTagId:tagId name:name];
}

- (NSDictionary *)asDictionary {
    NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
    
    if(_tagId != nil)
    {
        dict[@"id"] = _tagId;
    }
    
    if(_name != nil)
    {
        dict[@"name"] = _name;
    }
    NSDictionary* output = [dict copy];
    return output;
}

@end
