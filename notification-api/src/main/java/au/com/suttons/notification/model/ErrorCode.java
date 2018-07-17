package au.com.suttons.notification.model;

import au.com.suttons.notification.util.ErrorMessageSerialiser;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(using = ErrorMessageSerialiser.class)
public enum ErrorCode
{
	//Common
    API_C_001("Your request was not processed. Please contact your system administrator.", null),
    API_C_002("Cannot disable inactive resource.", "Cannot disable. The requested resource is inactive."),
    API_C_003("Cannot enable active resource.", "Cannot enable. The requested resource is active."),
    API_C_004("Cannot create/update as the given id already exists. Id: '%s'.", "Cannot create/update as the given id already exists. Code: '%s'."),

    //Access validation
	DT_B_001("The user is not authorised to access this site.", "The user, with username %s, does not have permission to access site %s."),
    DT_B_002("Cannot create/update as the given name already exists. Name: '%s'.", "Cannot create/update as the given code already exists. Name: '%s'."),
    DT_B_003("The user is not authorised to access this resource.", "The user, with username %s, does not have permission to access the resource."),
    DT_B_004("The user is not authorised to give another user the access.", "The user, with username %s, does not have permission to give '%s' role."),
    DT_B_005("Trying to save an entity to a wrong site.", "The user, with username %s, is on '%s'. However, trying to save to '%s'."),
    DT_B_006("The user is not authorised to access this admin resource.", "The user, with username %s, does not have permission to access the admin resource"),
    DT_B_007("Cannot update status on this resource on save. Try enable / disable resource.", "Cannot enable / disable this resource, access denied."),
    DT_B_008("Cannot create/update as the given code already exists. Code: '%s'.", "Cannot create/update as the given code already exists. Code: '%s'."),

    //Department
    COM_B_001("Cannot disable '%s'. There are active depots under this company.", "Cannot disable '%s'. There are active depots under this depots."),
    COM_B_002("Cannot disable '%s'. There are active users.", "Cannot disable '%s'. There are active users."),

    //Site
    ST_B_001("Cannot disable '%s'. There are active users.", "Cannot disable '%s'. There are active users."),
    ST_B_002("Cannot enable '%s'. The parents company is not active: '%s'.", "Cannot enable '%s'. The parents company is not active: '%s'."),

    //Signature
    SG_B_001("The vehicle cannot be found.", "The vehicle cannot be found."),

    //Licence
    LIC_B_001("Cannot disable '%s'. This licence is currently in use.", "Cannot disable '%s'. This licence is currently in use."),

    //User Access
    UA_B_001("Cannot create access for site and company level at the same time.", "cannot create access for site and company level at the same time."),
    UA_B_002("Cannot assign roles. The role '%s' already exists in '%s'.", "Cannot assign roles. The role '%s' already exists in '%s'."),

    //User
    USR_B_001("Cannot create/update as the given login id already exists. Code: '%s'.", "Cannot create/update as the given login id already exists. Login Id: '%s'."),
    USR_B_002("PIN must be a 4 digit number.", "PIN must be a 4 digit number."),

    //Customer
    CUS_B_001("Cannot create/update as the given customer number already exists. Customer Number: '%s'.", "Cannot create/update as the given customer number already exists. Customer Number: '%s'."),

    //Vehicle
    VEH_B_001("Cannot delete as the given vehicle is in status '%s'.", "Cannot delete as the given vehicle is in status '%s'."),
    VEH_B_002("Cannot cancel as the given vehicle is in status '%s'.", "Cannot cancel as the given vehicle is in status '%s'."),
    VEH_B_003("Cannot create/update as the given VIN already exists for the customer. VIN: '%s'. Customer: '%s'.", "Cannot create/update as the given VIN  already exists for the customer. VIN: '%s'. Customer: '%s'."),
    VEH_B_004("Cannot create/update as the vehicle does not have a customer. VIN: '%s'.", "Cannot create/update as the vehicle does not have a customer. VIN: '%s'."),

    //Zone
    ZON_B_001("The depot cannot be found.", "The depot cannot be found."),

    //Bay
    BAY_B_001("The zone cannot be found.", "The zone cannot be found."),

    //Claim
    CLAIM_B_001("The vehicle cannot be found.", "The vehicle cannot be found."),

    //Transport request
    TRQ_B_001("The vehicle cannot be found.", "The vehicle cannot be found."),

    //Upload File
    UP_B_001("Active vehicle found with vin no.(%s)", "Active vehicle found with vin no.(%s)"),
    UP_B_002("One or More Mandatory fields are missing in the file.", "One or More Mandatory fields are missing in the file."),

    //Invoice
    INV_B_001("The invoice cannot be found.", "The invoice cannot be found."),
    INV_B_002("Cannot create/update as the given name already exists. Name: '%s'.", "Cannot create/update as the given name already exists. Name: '%s'."),
    INV_B_003("The customer cannot be found.", "The customer cannot be found."),
    INV_B_004("The month end invoices for current month have already been generated for the company.", "The month end invoices for current month has already been generated for the company."),
    INV_B_005("Cannot create month end invoices as no templates exist for this customer/company. Customer: '%s'. Template Name: '%s'.", "Cannot create month end invoices as no templates exist for this customer/company. Customer: '%s'. Template Name: '%s'."),
    INV_B_006("A month end invoice with error exists for this company. Please contact your system administrator.", "A month end invoice with error exists for this company. Please contact your system administrator."),
    INV_B_007("Cannot create/update as a template already exists for given customer / company with same name. Name: '%s'.", "Cannot create/update as a template already exists for given customer / company with same name. Name: '%s'."),

    //Delivery run
    DR_B_001("Cannot create/update as the combination of given area code and run ID already exists. Area Code: '%s'. Run ID: '%s'.", "Cannot create/update as the combination of given area code and run ID already exists. Area Code: '%s'. Run ID: '%s'."),

    //Vehicle Carrier
    VC_B_001("Cannot disable '%s'. There are active daily delivery runs attached.", "Cannot disable '%s'. There are active daily delivery runs attached."),
    VC_B_002("Cannot enable '%s'. The attached external transportation is inactive.", "Cannot enable '%s'. The attached external transportation is inactive."),

    //External Transportation
    EXT_B_001("Cannot disable '%s'. There are active vehicle carriers with open daily delivery runs attached.", "Cannot disable '%s'. There are active vehicle carriers with open daily delivery runs attached."),

    //Vehicle Transfer Request
    SWP_B_001("Customer not found.", "The customer the vehicle is to be moved to cannot be found"),
    SWP_B_002("Vehicle not found.", "The vehicle cannot be found."),
    SWP_B_003("A vehicle with the same VIN No was found in the customer to be moved to.", "A vehicle with the same VIN No was found in the customer to be moved to."),

    //Report
    PRN_I_001("The application is unable to produce PDF documents at this time.", "The BIRT platform did not initialise, so no documents can be processed."),
    PRN_I_002("The template file for generating the %s document is missing.", "The BIRT report file that generates the %d document is missing from the file system."),
    PRN_I_003("The template file for generating the %s document cannot be accessed.", "The BIRT report file that generates the %d document exists but cannot be read."),
    PRN_I_004("An internal error has prevented this document from being generated.", "A BIRT engine exception occurred.  The cause may or may not be transitory."),
    PRN_I_005("An internal error has prevented this document from being generated.", "File I/O exceptions happened."),
    PRN_I_006("An internal error has prevented this document from being generated.", "The invoice storage location path cannot be found in the system."),
    PRN_I_007("An internal error has prevented this document from being generated.", "An unknown exception occurred. "),
    PRN_I_008("The template file for generating the document cannot be accessed.", "The BIRT report file that generates the document cannot be read. ")
    ;


    private final String friendlyMsg;
    private final String developerMsg;

    ErrorCode(String friendlyMsg, String developerMsg)
    {
        this.friendlyMsg = friendlyMsg;
        this.developerMsg = developerMsg;
    }

    public String getFriendlyMsg()
    {
        return friendlyMsg;
    }

    public String formatFriendlyMsg(Object... args)
    {
        return String.format(this.friendlyMsg, args);
    }

    public String getDeveloperMsg()
    {
        return developerMsg;
    }

    public String formatDeveloperMsg(Object... args)
    {
        return String.format(this.developerMsg, args);
    }
}
