#import "SWGStoreApi.h"
#import "SWGFile.h"
#import "SWGApiClient.h"
#import "SWGOrder.h"



@implementation SWGStoreApi
static NSString * basePath = @"http://petstore.swagger.wordnik.com/v2";

+(SWGStoreApi*) apiWithHeader:(NSString*)headerValue key:(NSString*)key {
    static SWGStoreApi* singletonAPI = nil;

    if (singletonAPI == nil) {
        singletonAPI = [[SWGStoreApi alloc] init];
        [singletonAPI addHeader:headerValue forKey:key];
    }
    return singletonAPI;
}

+(void) setBasePath:(NSString*)path {
    basePath = path;
}

+(NSString*) getBasePath {
    return basePath;
}

-(SWGApiClient*) apiClient {
    return [SWGApiClient sharedClientFromPool:basePath];
}

-(void) addHeader:(NSString*)value forKey:(NSString*)key {
    [[self apiClient] setHeaderValue:value forKey:key];
}

-(id) init {
    self = [super init];
    [self apiClient];
    return self;
}

-(void) setHeaderValue:(NSString*) value
           forKey:(NSString*)key {
    [[self apiClient] setHeaderValue:value forKey:key];
}

-(unsigned long) requestQueueSize {
    return [SWGApiClient requestQueueSize];
}


-(NSNumber*) placeOrderWithCompletionBlock:(SWGOrder *) body        
        completionHandler: (void (^)(SWGOrder * output, NSError* error))completionBlock
         {

    id m_body = body;
    NSMutableString* requestUrl = [NSMutableString stringWithFormat:@"%@/store/order", basePath];

    // remove format in URL if needed
    if ([requestUrl rangeOfString:@".{format}"].location != NSNotFound)
        [requestUrl replaceCharactersInRange: [requestUrl rangeOfString:@".{format}"] withString:@".json"];

    

    NSString* requestContentType = @"application/json";
    NSString* responseContentType = @"application/json";

    NSMutableDictionary* queryParams = [[NSMutableDictionary alloc] init];
    
    NSMutableDictionary* headerParams = [[NSMutableDictionary alloc] init];
    

    id bodyDictionary = nil;
    
    if(m_body != nil && [m_body isKindOfClass:[NSArray class]]){
        NSMutableArray * objs = [[NSMutableArray alloc] init];
        for (id dict in (NSArray*)m_body) {
            if([dict respondsToSelector:@selector(asDictionary)]) {
                [objs addObject:[(SWGObject*)dict asDictionary]];
            }
            else{
                [objs addObject:dict];
            }
        }
        bodyDictionary = objs;
    }
    else if([m_body respondsToSelector:@selector(asDictionary)]) {
        bodyDictionary = [(SWGObject*)m_body asDictionary];
    }
    else if([m_body isKindOfClass:[NSString class]]) {
        // convert it to a dictionary
        NSError * error;
        NSString * str = (NSString*)m_body;
        NSDictionary *JSON =
            [NSJSONSerialization JSONObjectWithData:[str dataUsingEncoding:NSUTF8StringEncoding]
                                            options:NSJSONReadingMutableContainers
                                              error:&error];
        bodyDictionary = JSON;
    }
    else if([m_body isKindOfClass: [SWGFile class]]) {
        requestContentType = @"form-data";
        bodyDictionary = m_body;
    }
    else{
        NSLog(@"don't know what to do with %@", m_body);
    }

    
    
    

    SWGApiClient* client = [SWGApiClient sharedClientFromPool:basePath];

    

    
    
    
    return [client dictionary:requestUrl 
                              method:@"POST" 
                         queryParams:queryParams 
                                body:bodyDictionary 
                        headerParams:headerParams
                  requestContentType:requestContentType
                 responseContentType:responseContentType
                     completionBlock:^(NSDictionary *data, NSError *error) {
                        if (error) {
                            completionBlock(nil, error);
                            
                            return;
                        }
                        
                        SWGOrder *result = nil;
                        if (data) {
                            result = [[SWGOrder    alloc]initWithValues: data];
                        }
                        completionBlock(result , nil);
                        
                    }];
    

}

-(NSNumber*) getOrderByIdWithCompletionBlock:(NSString *) orderId        
        completionHandler: (void (^)(SWGOrder * output, NSError* error))completionBlock
         {

    id m_body = nil;
    NSMutableString* requestUrl = [NSMutableString stringWithFormat:@"%@/store/order/{orderId}", basePath];

    // remove format in URL if needed
    if ([requestUrl rangeOfString:@".{format}"].location != NSNotFound)
        [requestUrl replaceCharactersInRange: [requestUrl rangeOfString:@".{format}"] withString:@".json"];

    [requestUrl replaceCharactersInRange: [requestUrl rangeOfString:[NSString stringWithFormat:@"%@%@%@", @"{", @"orderId", @"}"]] withString: [SWGApiClient escape:orderId]];
    

    NSString* requestContentType = @"application/json";
    NSString* responseContentType = @"application/json";

    NSMutableDictionary* queryParams = [[NSMutableDictionary alloc] init];
    
    NSMutableDictionary* headerParams = [[NSMutableDictionary alloc] init];
    

    id bodyDictionary = nil;
    
    
    

    SWGApiClient* client = [SWGApiClient sharedClientFromPool:basePath];

    

    
    
    
    return [client dictionary:requestUrl 
                              method:@"GET" 
                         queryParams:queryParams 
                                body:bodyDictionary 
                        headerParams:headerParams
                  requestContentType:requestContentType
                 responseContentType:responseContentType
                     completionBlock:^(NSDictionary *data, NSError *error) {
                        if (error) {
                            completionBlock(nil, error);
                            
                            return;
                        }
                        
                        SWGOrder *result = nil;
                        if (data) {
                            result = [[SWGOrder    alloc]initWithValues: data];
                        }
                        completionBlock(result , nil);
                        
                    }];
    

}

-(NSNumber*) deleteOrderWithCompletionBlock:(NSString *) orderId        
        
        completionHandler: (void (^)(NSError* error))completionBlock {

    id m_body = nil;
    NSMutableString* requestUrl = [NSMutableString stringWithFormat:@"%@/store/order/{orderId}", basePath];

    // remove format in URL if needed
    if ([requestUrl rangeOfString:@".{format}"].location != NSNotFound)
        [requestUrl replaceCharactersInRange: [requestUrl rangeOfString:@".{format}"] withString:@".json"];

    [requestUrl replaceCharactersInRange: [requestUrl rangeOfString:[NSString stringWithFormat:@"%@%@%@", @"{", @"orderId", @"}"]] withString: [SWGApiClient escape:orderId]];
    

    NSString* requestContentType = @"application/json";
    NSString* responseContentType = @"application/json";

    NSMutableDictionary* queryParams = [[NSMutableDictionary alloc] init];
    
    NSMutableDictionary* headerParams = [[NSMutableDictionary alloc] init];
    

    id bodyDictionary = nil;
    
    
    

    SWGApiClient* client = [SWGApiClient sharedClientFromPool:basePath];

    

    
    
    
    
    return [client stringWithCompletionBlock:requestUrl 
                                             method:@"DELETE" 
                                        queryParams:queryParams 
                                               body:bodyDictionary 
                                       headerParams:headerParams
                                 requestContentType: requestContentType
                                responseContentType: responseContentType
                                    completionBlock:^(NSString *data, NSError *error) {
                        if (error) {
                            completionBlock(error);
                            return;
                        }
                        completionBlock(nil);
                    }];
    
    

}



@end