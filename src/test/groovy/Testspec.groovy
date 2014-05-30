import spock.lang.*
import geb.*
import geb.spock.*
import org.openqa.selenium.*
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.*

class Testspec extends GebReportingSpec   {

def SEObj
def DKObj

def setup(){
    SEObj = new testDataService("?customerCountry=se")
    DKObj = new testDataService("?customerCountry=dk&customerType=consumer")
     }
    
       /* def js( String script ){
        (driver as JavascriptExecutor).executeScript( script )
     } */
private def getAlert() {
        try {
            driver.switchTo().alert()
        } catch(NoAlertPresentException  ) {
            null
        }
    }



 def "login to Care" () {

      when:
      to DkCustomer, DKObj.getboId()

      then:
      title == "CARE (testing)"
    }   
 def "Add and update Interaction" () {     
 
        def date = new Date()
        
        when:
        to SeCustomer, 815291397578409580607582194060
        def InteractionForm = waitFor{ $('form#interaction-form') }
       
        $('form#interaction-form textarea#interaction-note').value("s" + date.format('HHmmss'))
       // $("select.span option:last-child").attr("selected","selected")  
        $('form#interaction-form div.interaction-side button.btn').click()
        sleep(1000)

        and:
        js.exec("location.reload()")
        waitFor{ $('div.tabbable') }
        $('div.tabbable a[href="#Interactions"]').click()
        def Interactions = waitFor{ $('div#customer-interactions') }
        
        and:
        assert $('div#interactions-list tr:first-child td.note').text().trim() == "s" + date.format('HHmmss')

        and:
        $('div#interactions-list tr.interaction:first-child td.note').click()
        def InteractionNote = waitFor{ $('div#interactions-list .interaction_note') }
        $('div#interactions-list .interaction_note span.value').click()

        and:
        $('div.ui-dialog form.edit_interaction textarea.span4:enabled').value(date.format('HHmmss'))
        $('div.ui-dialog button.btn:first-child').click()
        sleep(1000)
        
        and:
        js.exec("location.reload()")
        waitFor{ $('div.tabbable') } 
        $('div.tabbable a[href="#Interactions"]').click()
        waitFor{ $('div#interactions-list table.table') }
       
        then:
        assert $('div#interactions-list tr:first-child td.note').text().trim() == date.format('HHmmss')

     }    

     def "Change Customer Email" () {   

     def date = new Date()           
        
        when:
        to DkCustomer, DKObj.getboId()
        def CustomerModule = waitFor{ $('#customer-info') }
        def newEmailAddress = 'test' + date.format('HHmmss') + '@testsson.com'

        and:
        $('#customer-info p[data-enable-menu] span.value.editable').click()
        $('#customer-info p[data-enable-menu] span.value.editable i.icon-edit').click()
        def EmailAddress = waitFor{ $('body > div[aria-describedby="updatePrimaryEmailAddressForm"]') }

        and:
        $('body > div[aria-describedby="updatePrimaryEmailAddressForm"] input#email_address').value(newEmailAddress)
        $('.ui-dialog .ui-dialog-buttonpane .btn:first-child').click() 
        sleep(1000)
    
        and:
        js.exec("location.reload()") 
        waitFor{ $('#customer-info') }   

        then:
        assert $('#customer-info p[data-enable-menu] span.value').text() == newEmailAddress
     }     

   /*    def "Add and Update Case for Se Customer" () {             
        
        when:
        to SeCustomer, 815291397578409580607582194060
        def Tabtable = waitFor{ $('div.tabbable') }

        and:
        $('div.tabbable a[href="#Cases"]').click()
        def AddCase = waitFor{ $('button#add_new_case') }
        $('button#add_new_case').click()
         
        and:
        $('#formCaseType').caseCategory = "3SC"
        $('#formCaseSubType').caseType= "3SC00"
        $('#formCaseDetails').caseDetail= "3SC01" 

        and:
        def options = $('#caseMsisdn option')
        msisdn = options[(System.currentTimeMillis() % options.size()) as int].value()
        $('#formCaseMsisdn #caseMsisdn').value(msisdn)

        and:
        $('#formCaseDevice #caseDevice').value('F00556')
        $('#formCaseStatus #caseStatus').value('OPEN')
        $('#formCaseProviderGroup').caseProviderGroup= "19"
        $('#formCaseNotes').value("Notes")
        waitFor(10){ $('div#customer-cases button[data-trigger*="submit-case-form"]').click() }
        sleep(2000)
         

        //For assertion 
        and:
        js.exec("location.reload(true)")
        waitFor{ $('div.tabbable') }
        $('div.tabbable a[href="#Cases"]').click()
        waitFor{ $('button#add_new_case') }

        then:
        title == "CARE (staging)"
        assert $('#Cases .expander:first-child td:nth-child(4)').text() == "3Service Center"
        assert $('#Cases .expander:first-child td:nth-child(5)').text() == "Hänvisad KS"  

        and:
        def CaseId = $('#customer-cases .stylish tbody tr:first-child td:nth-child(7)').text()
        $('#customer-cases .stylish tbody tr:first-child').click()
        $('#customer-cases .stylish tr.collapsable i.icon-edit:first-child').click()

        and:
        $('#customer-cases #caseStatus').value('RESL')
        waitFor(10){ $('div#customer-cases button[data-trigger*="submit-case-form"]').click() }
        sleep(2000)


        and:
        browser.driver.executeScript("history.go(-1)")
        waitFor{ $('div.tabbable') }
        $('div.tabbable a[href="#Cases"]').click()
        waitFor{ $('button#add_new_case') }


        then:
        title == "CARE (staging)"
        assert $('#Cases .stylish tbody tr#'+CaseId+' td:nth-child(3)').text() == "closed"

        }    */  

        def "Add Case for Dk Customers" () {

        when:
        to DkCustomer, DKObj.getboId()
        def Tabtable = waitFor{ $('div.tabbable') }

        and:
        $('div.tabbable a[href="#Cases"]').click()
        def AddCase = waitFor{ $('button#add_new_case') }
        $('button#add_new_case').click()

        and:
        $('#formCaseQuickCode select[name=caseQuickCode]').value('508')
        sleep(1000)

        and:
        def options = $('#caseMsisdn option')
        def msisdn = options[(System.currentTimeMillis() % options.size()) as int].value()
        $('#formCaseMsisdn #caseMsisdn').value(msisdn)

        and:
        $('#formCaseDevice #caseDevice').value('F20258')
        $('#formCaseStatus #caseStatus').value('OPEN')
        $('#formCaseProviderGroup #caseProviderGroup').value('DKBACK')

        and:
        $('#fuYYYY').value('2014')
        $('#fuMM').value('06')
        $('#fuDD').value('06')

        and:
        $('#formCaseNotes').value('Notes')
        waitFor(10){ $('div#customer-cases button[data-trigger*="submit-case-form"]').click() }
        sleep(2000)

        and:
        js.exec("location.reload()")
        waitFor{ $('div.tabbable') }
        $('div.tabbable a[href="#Cases"]').click()
        waitFor{ $('button#add_new_case') }

        then:
        title == "CARE (testing)"
        assert $('#Cases .expander:first-child td:nth-child(4)').text() == "Terminaler"
        assert $('#Cases .expander:first-child td:nth-child(5)').text() == "Smartphone" 
         
        }   


      /*  def "Update Bonus Status" ()  {    // Can be improved
       
        when:
        to DkCustomer,DKObj.getboId()
        def CustomerModule = waitFor{ $('#customer-info') }

        and:
        $('td#customer-info div.row-fluid:nth-child(3) span.value.editable').click()
        $('td#customer-info div.row-fluid:nth-child(3) span.value.editable i.icon-edit').click()
        waitFor{ $('div.ui-dialog')}

        and:
        $('#update3BonusStatus').programStatus = "Active"
        //$('.ui-dialog #update3BonusStatus input[type="checkbox"]').attr("checked" ,true)
        $('.ui-dialog .ui-dialog-buttonpane .btn:first-child').click()
        sleep(3000)

        and:
        js.exec("location.reload()")
        waitFor{ $('#customer-info') }

        then:
        title == "CARE (testing)"
        assert $('td#customer-info div.row-fluid:nth-child(3) span.value.editable').text() == "Active"
        }   */

       def "Create New customer address" () {        
        boId()
        when:
        to SeCustomer, SEObj.getboId()
        def CustomerModule = waitFor{ $('#customer-info') }

        and:
        $('#customer-info span.address.value.editable').click()
        $('#customer-info span.address.value.editable i.icon-edit').click()
        def CustomerAddress = waitFor{ $('.ui-dialog #updateAddressPopup') }

        and:
        $('.ui-dialog #updateAddressPopup .uber-form-actions').switchAddressForm_="new" 
        $('.ui-dialog #updateAddressPopup [name="careOf"]').value('CareOf')
        $('.ui-dialog #updateAddressPopup [name="street"]').value('street')
        $('.ui-dialog #updateAddressPopup [name="postal"]').value('123 45')
        $('.ui-dialog #updateAddressPopup [name="city"]').value('city')

        and:
        $('.ui-dialog .ui-dialog-buttonpane button.btn:first-child').click()
        sleep(2000)

        and:
        js.exec("location.reload()")
        waitFor{ $('#customer-info') }          

        then:
        title == "CARE (testing)"
        assert $('#customer-info span.address.value.editable').text().trim().replace("\n", "") == "street123 45 city" 

     }   

/*   Customer Module Ends */

/* Account Module starts  */


  /*   def "Create Billing address" () {         //Done:)

        when:
        to Loginurl
        def CustomerModule = waitFor{ $('.module.standard.account') } 

        and:
        $('#billingAddress').click()
        $('#billingAddress i',1).click()
        def CustomerAddress = waitFor{ $('#customer-info div#address_modal[aria-hidden="false"]') }

        and:
        $('#customer-info div#address_modal[aria-hidden="false"] .btn-group button:nth-child(2)').click()
        $('#customer-info div#address_modal[aria-hidden="false"] .new_address input[name="careOf"]').value('test1')
        $('#customer-info div#address_modal[aria-hidden="false"] .new_address input[name="street"]').value('test12')
        $('#customer-info div#address_modal[aria-hidden="false"] .new_address input[name="postal"]').value('123 45')
        $('#customer-info div#address_modal[aria-hidden="false"] .new_address input[name="city"]').value('test1234')
        $('form#customer_address div.modal-footer button:nth-child(1)').click()
        sleep(1000)

        and:
        js.exec("location.reload()")
        waitFor{ $('.module.standard.account') }

        then:
        title == "CARE (testing)"
        assert $('#billingAddress').text() =="test12, 123 45 test1234"

      }         */

    /*    def "Change Billing address" () {         //Unable to check the radio button

        when:
        to Loginurl
        def CustomerModule = waitFor{ $('.module.standard.account') } 

        and:
        $('#billingAddress').click()
        $('#billingAddress i.icon-edit').click()
        waitFor{ $('#updateAddressPopup div.active') }

        and:
        

        then:
        title == "CARE (testing)"

     }   */


     /*   Account Module Ends */

/* SubScription Module starts */


   /* def "Change End user" ()  {     // Done

    def date = new Date()

        when:
        to Customer,654438788693747626203336136307,"subscription",57598076
        def SubscriptionModule = waitFor{ $('div.submodule.subscription-details .row-fluid:nth-child(3) .value.editable') }
        def FirstName = "S" + date.format('HHmmss') 
        def LastName = "K" + date.format('HHmmss')

        and:
        $('div.submodule.subscription-details .row-fluid:nth-child(3) .value.editable').click()
        //$('div.submodule.subscription-details .row-fluid:nth-child(3) .value.editable i',1).click()

        and:
        $('.newEndUser input[name="endUserFirstName"]').value(FirstName)
        $('.newEndUser input[name="endUserLastName"]').value(LastName)
        $('.newEndUser').endUserGender="M"
        $('.newEndUser input[name="endUserYYYY"]').value('1989')
        $('.newEndUser').endUserMM="05"
        $('.newEndUser').endUserDD="2"
        $('.newEndUser input[name="endUserCareOf"]').value('Test')
        $('.newEndUser input[name="endUserStreet"]').value('Test123')
        $('.newEndUser input[name="endUserPostal"]').value('123 45')
        $('.newEndUser input[name="endUserCity"]').value('Hyderabad')
        $('.newEndUser input[name="endUserEmail"]').value('Sai.Krishna@tre.se')
        $('.ui-dialog .modal-footer button:nth-child(1)').click()
        sleep(3000)
     
        and:
        driver.switchTo().alert().accept()
                
        then:
        title == "CARE (testing)"
    }    

        def "Change Tariff" ()  {     // Done

        when:
        to Customer,654438788693747626203336136307,"subscription",57598076
        def SubscriptionModule = waitFor{ $('div.submodule.subscription-details .row-fluid:nth-child(3) .value.editable') }

        and:
        $('div.submodule.subscription-details [title*="Change tariff plan"]').click()
        $('div.submodule.subscription-details [title*="Change tariff plan"] i.icon-edit').click()
        waitFor { $('.ui-dialog') }
        sleep(1000)

        and:
        def NewTariff = $('p.tariffPlan option:nth-child(1)').text()
        $('.ui-dialog .modal-footer button.btn:first-child').click()
        sleep(3000)
        
        and:
        driver.switchTo().alert().accept()
        
        then:
        title == "CARE (testing)"  
        
      }      

        def "Change UICCID" ()  {          // Uiccid How??              

        when:
        to Loginurl
        def SubscriptionModule = waitFor{ $('div.submodule.subscription-details .row-fluid:nth-child(3) .value.editable') }

        and:
        $('div.submodule.subscription-details [title*="Change UICCID"]').click()
        $('div.submodule.subscription-details [title*="Change UICCID"] i.icon-edit').click()
        waitFor { $('.uiccidForm') }
        
        and:
        //$('.uiccidForm .uiccid [name="uiccid"]').value("123")
        $('.uiccidForm .modal-footer button.btn:last-child').click()
        sleep(3000)

        and:
        driver.switchTo().alert().accept()
        
        then:
        title == "CARE (testing)"
        
      }  */   



 /*     def "Change Directory Services for Se Subscription" ()  {     // Done SE

        when:
        to Customer,654438788693747626203336136307,"subscription",57590143
        def SubscriptionModule = waitFor{ $('div.submodule.subscription-details .row-fluid:nth-child(3) .value.editable') }

        and:
        $('div.submodule.subscription-details .row-fluid:nth-child(3) [title*="Change Directory Service"]').click()
        $('div.submodule.subscription-details .row-fluid:nth-child(3) [title*="Change Directory Service"] i.icon-edit').click()
        waitFor { $('.ui-dialog') }

        and:
        if( $('.ui-dialog p.directoryService input#SP00001').attr('checked') ){
       
            $('p.directoryService').dirServ = "SP00002"
        }
        else if($('.ui-dialog p.directoryService input#SP00002').attr('checked')) { 
            
            $('p.directoryService').dirServ = "SP00001"

        }  

       and:
       $('.ui-dialog .modal-footer button.btn:first-child').click()
       sleep(3000)

       and:
       driver.switchTo().alert().accept()
        

        then:
        title == "CARE (testing)"    
      }       */
   
    /*  def "Change Directory Services For Dk Subscription" ()  {     

        when:
        to Customer,307472258452497929136694185326,"subscription",30758984
        def SubscriptionModule = waitFor{ $('div.submodule.subscription-details .row-fluid:nth-child(3) .value.editable') }

        and:
        $('div.submodule.subscription-details .row-fluid:nth-child(3) [title*="Change Directory Service"]').click()
        $('div.submodule.subscription-details .row-fluid:nth-child(3) [title*="Change Directory Service"] i.icon-edit').click()
        waitFor { $('.ui-dialog') }

       and:
       if( $('.ui-dialog p.directoryService input#S40026').attr('checked') ){
       
            $('p.directoryService').dirServ = "S40027"
        }
        else if($('.ui-dialog p.directoryService input#S40026').attr('unchecked')) { 
            
            $('p.directoryService').dirServ = "S40026"
        } 

       and:
       $('.ui-dialog .modal-footer button.btn:first-child').click()
       sleep(3000)

       and:
       driver.switchTo().alert().accept()

       then:
        title == "CARE (testing)"    
       }  */



    /*   def "Single Service Request" ()  {        //Awesome

       when:
        to Customer,654438788693747626203336136307,"subscription",57593089
        def SubscriptionModule = waitFor{ $('div.customer-child-container .tabbable') }

        and:
        $('div.customer-child-container .tabbable li:nth-child(2)').click()
        waitFor{ $('div.customer-child-container .tabbable .portfolio') }

        and:
        $('.tabbable .portfolio').product="S00030"
        waitFor{ $('.portfolioForm .executionDate') }

        and:
        $('fieldset.executionDate button.btn').click()
        sleep(3000)

        and:
        js.exec("location.reload()")
        waitFor{ $('div.customer-child-container .tabbable') }
        $('div.customer-child-container .tabbable li:nth-child(1)').click()
        waitFor{ $('div.customer-child-container .tabbable div#Orders_57593089') }

        then:
        title == "CARE (testing)"
        assert $('tbody.orders tr:first-child td.clickable.expander:nth-child(2)').text() == "Multiple Service Act/Deact"
        assert $('tbody.orders tr:first-child td.clickable.expander:nth-child(4)').text() == "Hold"

    }    

    def "cancelOrderRequest" () {    // Done. It is related with above test case

        when:
        to Customer,654438788693747626203336136307,"subscription",57593089
        def SubscriptionModule = waitFor{ $('div.customer-child-container .tabbable') }

        and:
        $('div.customer-child-container .tabbable li:nth-child(1)').click()
        waitFor{ $('.tabbable .tab-content') }

        and:
        $('.tab-content .orders i.icon-minus-sign').click()
        sleep(2000)

        and:
        js.exec("location.reload()")
        waitFor{ $('div.customer-child-container .tabbable') }
        $('div.customer-child-container .tabbable li:nth-child(1)').click()

        then:
        title == "CARE (testing)"
        assert $('tbody.orders tr:first-child td.clickable.expander:nth-child(4)').text() != "Hold"

}  */
   
}  