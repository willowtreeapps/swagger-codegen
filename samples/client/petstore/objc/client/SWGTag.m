#import "SWGDate.h"
#import "SWGTag.h"

@implementation SWGTag

-(id)tagId: (NSNumber *) tagId
    name: (NSString *) name { 
    
    _tagId = tagId;
    _name = name;
    
    return self;
}
-(id) initWithValues:(NSDictionary*)dict
{
    self = [super init];
    if(self) {
        _tagId = dict[@"id"];
        _name = dict[@"name"];
        
    }
    return self;
}

-(NSDictionary*) asDictionary {
    NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
    
    
    if(_tagId != nil)
        dict[@"id"] = _tagId;
    
    
    
    
    if(_name != nil)
        dict[@"name"] = _name;
    
    
    
    NSDictionary* output = [dict copy];
    return output;
}

@end
