#import <Foundation/Foundation.h>
#import "SWGObject.h"
#import "SWGTag.h"
#import "SWGCategory.h"

@interface SWGPet : SWGObject

@property(nonatomic) NSNumber *petId;  
@property(nonatomic) SWGCategory *category;  
@property(nonatomic) NSString *name;  
@property(nonatomic) NSArray *photoUrls;  
@property(nonatomic) NSArray *tags;  
@property(nonatomic) NSString *status;  /* pet status in the store  */

- (id)petId:(NSNumber *)petId category:(SWGCategory *)category name:(NSString *)name photoUrls:(NSArray *)photoUrls tags:(NSArray *)tags status:(NSString *)status;
- (id)initWithValues:(NSDictionary *)dict;
- (NSDictionary *)asDictionary;

@end
