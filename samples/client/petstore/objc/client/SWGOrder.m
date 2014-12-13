#import "SWGDate.h"
#import "SWGOrder.h"

@implementation SWGOrder

- (instancetype)initWithOrderId:(NSNumber *)orderId petId:(NSNumber *)petId quantity:(NSNumber *)quantity shipDate:(SWGDate *)shipDate status:(NSString *)status complete:(NSNumber *)complete
{
    self = [super init];
    if (self)
    {
        _orderId = orderId;
        _petId = petId;
        _quantity = quantity;
        _shipDate = shipDate;
        _status = status;
        _complete = complete;
        
    }

    return self;
}

- (id)initWithValues:(NSDictionary*)dict
{
    NSNumber * orderId = dict[@"id"];
    NSNumber * petId = dict[@"petId"];
    NSNumber * quantity = dict[@"quantity"];
    
    id shipDate_dict = dict[@"shipDate"];
    
    SWGDate * shipDate = nil;
    if(shipDate_dict != nil)
    {
        shipDate = [[SWGDate  alloc]initWithValues:shipDate_dict];
    }
    
    NSString * status = dict[@"status"];
    NSNumber * complete = dict[@"complete"];
    
    return [self initWithOrderId:orderId petId:petId quantity:quantity shipDate:shipDate status:status complete:complete];
}

- (NSDictionary *)asDictionary {
    NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
    
    if(_orderId != nil)
    {
        dict[@"id"] = _orderId;
    }
    
    if(_petId != nil)
    {
        dict[@"petId"] = _petId;
    }
    
    if(_quantity != nil)
    {
        dict[@"quantity"] = _quantity;
    }
    
    if(_shipDate != nil)
    { 
        if(_shipDate && [_shipDate isKindOfClass:[SWGDate class]])
        {
            NSString * dateString = [(SWGDate*)_shipDate toString];
            if(dateString)
            {
                dict[@"shipDate"] = dateString;
            }
        }
        else
        {
            if(_shipDate != nil)
            {
                dict[@"shipDate"] = [(SWGObject*)_shipDate asDictionary];
            }
        }
    }
    
    if(_status != nil)
    {
        dict[@"status"] = _status;
    }
    
    if(_complete != nil)
    {
        dict[@"complete"] = _complete;
    }
    NSDictionary* output = [dict copy];
    return output;
}

@end
