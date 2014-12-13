#import "SWGDate.h"
#import "SWGUser.h"

@implementation SWGUser

- (instancetype)initWithUserId:(NSNumber *)userId username:(NSString *)username firstName:(NSString *)firstName lastName:(NSString *)lastName email:(NSString *)email password:(NSString *)password phone:(NSString *)phone userStatus:(NSNumber *)userStatus
{
    self = [super init];
    if (self)
    {
        _userId = userId;
        _username = username;
        _firstName = firstName;
        _lastName = lastName;
        _email = email;
        _password = password;
        _phone = phone;
        _userStatus = userStatus;
        
    }

    return self;
}

- (id)initWithValues:(NSDictionary*)dict
{
    NSNumber * userId = dict[@"id"];
    NSString * username = dict[@"username"];
    NSString * firstName = dict[@"firstName"];
    NSString * lastName = dict[@"lastName"];
    NSString * email = dict[@"email"];
    NSString * password = dict[@"password"];
    NSString * phone = dict[@"phone"];
    NSNumber * userStatus = dict[@"userStatus"];
    
    return [self initWithUserId:userId username:username firstName:firstName lastName:lastName email:email password:password phone:phone userStatus:userStatus];
}

- (NSDictionary *)asDictionary {
    NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
    
    
    if(_userId != nil)
        dict[@"id"] = _userId;
    
    
    
    
    if(_username != nil)
        dict[@"username"] = _username;
    
    
    
    
    if(_firstName != nil)
        dict[@"firstName"] = _firstName;
    
    
    
    
    if(_lastName != nil)
        dict[@"lastName"] = _lastName;
    
    
    
    
    if(_email != nil)
        dict[@"email"] = _email;
    
    
    
    
    if(_password != nil)
        dict[@"password"] = _password;
    
    
    
    
    if(_phone != nil)
        dict[@"phone"] = _phone;
    
    
    
    
    if(_userStatus != nil)
        dict[@"userStatus"] = _userStatus;
    
    
    
    NSDictionary* output = [dict copy];
    return output;
}

@end
