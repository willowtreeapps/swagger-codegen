#import "SWGDate.h"
#import "SWGCategory.h"

@implementation SWGCategory

-(id)categoryId: (NSNumber *) categoryId
    name: (NSString *) name { 
    
    _categoryId = categoryId;
    _name = name;
    
    return self;
}
-(id) initWithValues:(NSDictionary*)dict
{
    self = [super init];
    if(self) {
        _categoryId = dict[@"id"];
        _name = dict[@"name"];
        
    }
    return self;
}

-(NSDictionary*) asDictionary {
    NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
    
    
    if(_categoryId != nil)
        dict[@"id"] = _categoryId;
    
    
    
    
    if(_name != nil)
        dict[@"name"] = _name;
    
    
    
    NSDictionary* output = [dict copy];
    return output;
}

@end
