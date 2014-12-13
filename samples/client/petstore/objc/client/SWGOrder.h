#import <Foundation/Foundation.h>
#import "SWGObject.h"
#import "SWGDate.h"

@interface SWGOrder : SWGObject

@property(nonatomic) NSNumber *orderId;  
@property(nonatomic) NSNumber *petId;  
@property(nonatomic) NSNumber *quantity;  
@property(nonatomic) SWGDate *shipDate;  
@property(nonatomic) NSString *status;  /* Order Status  */
@property(nonatomic) NSNumber *complete;  

- (id)orderId:(NSNumber *)orderId petId:(NSNumber *)petId quantity:(NSNumber *)quantity shipDate:(SWGDate *)shipDate status:(NSString *)status complete:(NSNumber *)complete;
- (id)initWithValues:(NSDictionary *)dict;
- (NSDictionary *)asDictionary;

@end
