#import <Foundation/Foundation.h>
#import "SWGObject.h"

@interface SWGUser : SWGObject

@property(nonatomic) NSNumber *userId;  
@property(nonatomic) NSString *username;  
@property(nonatomic) NSString *firstName;  
@property(nonatomic) NSString *lastName;  
@property(nonatomic) NSString *email;  
@property(nonatomic) NSString *password;  
@property(nonatomic) NSString *phone;  
@property(nonatomic) NSNumber *userStatus;  /* User Status  */

- (id)userId:(NSNumber *)userId username:(NSString *)username firstName:(NSString *)firstName lastName:(NSString *)lastName email:(NSString *)email password:(NSString *)password phone:(NSString *)phone userStatus:(NSNumber *)userStatus;
- (id)initWithValues:(NSDictionary *)dict;
- (NSDictionary *)asDictionary;

@end
