#import "SWGDate.h"
#import "SWGPet.h"

@implementation SWGPet

-(id)petId: (NSNumber *) petId
    category: (SWGCategory *) category
    name: (NSString *) name
    photoUrls: (NSArray *) photoUrls
    tags: (NSArray *) tags
    status: (NSString *) status { 
    
    _petId = petId;
    _category = category;
    _name = name;
    _photoUrls = photoUrls;
    _tags = tags;
    _status = status;
    
    return self;
}
-(id) initWithValues:(NSDictionary*)dict
{
    self = [super init];
    if(self) {
        _petId = dict[@"id"];
        
        id category_dict = dict[@"category"];
        if(category_dict != nil)
            _category = [[SWGCategory  alloc]initWithValues:category_dict];
        
        _name = dict[@"name"];
        _photoUrls = dict[@"photoUrls"];
        
        id tags_dict = dict[@"tags"];
        if([tags_dict isKindOfClass:[NSArray class]]) {
            NSMutableArray * objs = [[NSMutableArray alloc] initWithCapacity:[(NSArray*)tags_dict count]];
            if([(NSArray*)tags_dict count] > 0) {
                for (NSDictionary* dict in (NSArray*)tags_dict) {
                    SWGTag* d = [[SWGTag  alloc] initWithValues:dict];
                    [objs addObject:d];
                }
                _tags = [[NSArray alloc] initWithArray:objs];
            }
            else {
                _tags = [[NSArray alloc] init];
            }
        }
        else {
            _tags = [[NSArray alloc] init];
        }
        
        _status = dict[@"status"];
        
    }
    return self;
}

-(NSDictionary*) asDictionary {
    NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
    
    
    if(_petId != nil)
        dict[@"id"] = _petId;
    
    
    
    if(_category != nil){
        
        
        if(_category && [_category isKindOfClass:[SWGDate class]]) {
            NSString * dateString = [(SWGDate*)_category toString];
            if(dateString){
            dict[@"category"] = dateString;
            }
        }
        else {
            if(_category != nil)
            dict[@"category"] = [(SWGObject*)_category asDictionary];
        }
        
    }
    
    
    
    if(_name != nil)
        dict[@"name"] = _name;
    
    
    
    
    
    if(_photoUrls != nil) {
        if([_photoUrls isKindOfClass:[NSArray class]]) {
            dict[@"_photoUrls"] = [[NSArray alloc] initWithArray: (NSArray*) _photoUrls copyItems:true];
        }
        else if([_photoUrls isKindOfClass:[NSDictionary class]]) {
            dict[@"photoUrls"] = [[NSDictionary alloc] initWithDictionary:(NSDictionary*)_photoUrls copyItems:true];
        }
    }
    
    
    if(_tags != nil){
        
        if([_tags isKindOfClass:[NSArray class]]){
        NSMutableArray * array = [[NSMutableArray alloc] init];
            for( SWGTag *tags in _tags) {
                [array addObject:[(SWGObject*)tags asDictionary]];
            }
            dict[@"tags"] = array;
        }
        
        
    }
    
    
    
    if(_status != nil)
        dict[@"status"] = _status;
    
    
    
    NSDictionary* output = [dict copy];
    return output;
}

@end
