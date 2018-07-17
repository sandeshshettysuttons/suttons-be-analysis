package au.com.suttons.notification.report;

public enum DocumentResources {

    //EG:
    vehicleInventoryPDFReport("VehicleInventoryPDFReport.rptdesign","Vehicle_Inventory_Report.pdf"), // PDF
    vehicleInventoryXLSReport("VehicleInventoryXLSReport.rptdesign","Vehicle_Inventory_Report.xls"), // EXCEL
    vehicleQR("VehicleQR.rptdesign","Vehicle_QR.pdf"), // Read from recource folder
    vehicleTag("VehicleTag.rptdesign","Vehicle_Tag.pdf", true); // Read from external(NetworkLocation) folder

	public final static String documentFileStore = "document/";

	public final String fileName;
	public final String downloadName;
    public final boolean fromNetworkLocation;


    DocumentResources(String fileName, String downloadName) {
    	this.fileName = fileName;
    	this.downloadName = downloadName;
    	this.fromNetworkLocation = false;
    }

    DocumentResources(String fileName, String downloadName, boolean fromNetworkLocation) {
        this.fileName = fileName;
        this.downloadName = downloadName;
        this.fromNetworkLocation = fromNetworkLocation;
    }

    public String getResourceLocation() {
    	return documentFileStore + this.fileName;
    }

    public String getDownloadName() {
    	return this.downloadName;
    }
}
