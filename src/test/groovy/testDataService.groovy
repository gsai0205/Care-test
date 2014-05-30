import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.*


    class testDataService {
    
    def msisdn
    def boId
    def account 
    def http 

   def  testDataService()
    {
     http = new HTTPBuilder('http://tds.it.tre.se/tds2/v0/bt1/subscription' )
     getHttpBuilder()   
    }
    def testDataService(def searchQuery)
    {
     http = new HTTPBuilder('http://tds.it.tre.se/tds2/v0/bt1/subscription'+searchQuery)
     getHttpBuilder()   
    }
    def getHttpBuilder() {
    http.get( path: 'subscription') { resp, json ->
        boId = json.subscription.customer.boId
        msisdn =json.subscription.msisdn
        account = json.subscription.account.number
        }
    } 

    def getboId() {
       return boId
        }       

    def getmsisdn() {
       return msisdn
       }  
   
   def getaccount() {
       return account
        }
}

