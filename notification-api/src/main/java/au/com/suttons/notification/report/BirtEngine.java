package au.com.suttons.notification.report;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import au.com.suttons.notification.config.AppConfig;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.*;
import org.eclipse.core.internal.registry.RegistryProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.suttons.notification.model.ErrorCode;
import au.com.suttons.notification.resource.error.RestApiException;

/**
 * Wraps an underlying document rendering engine to produce PDF output for a response.  The intent of this response is
 * to produce printable documents.  The rendering engine is BIRT, and it generates output documents using pre-defined BIRT
 * report documents.
 */
public class BirtEngine {

    private static final Logger logger = LoggerFactory.getLogger(BirtEngine.class);

    private static IReportEngine documentEngine;
    public final static String REPORT_TYPE_PDF = "pdf";
    public final static String REPORT_TYPE_EXCEL = "xls";

    static {
        EngineConfig config = new EngineConfig();
        try {
            RegistryProviderFactory.releaseDefault();
            Platform.startup(config);
            IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
            documentEngine = factory.createReportEngine(config);
        } catch (BirtException ex) {
            logger.error("Failed to initialise the BIRT report platform.  Document rendering will fail.", ex);
        }
   	}

    //cannot be instantiated
    private BirtEngine() {
    }

    public static void renderDocument(DocumentResources reportResource, Map<String, Object> parameters, OutputStream destination, String type) {
        renderDocument(reportResource, parameters, destination, type, null);
    }

    public static void renderDocument(DocumentResources reportResource, Map<String, Object> parameters, OutputStream destination, String type, Long departmentId) {

        InputStream inputStream;

        if (reportResource.fromNetworkLocation) {
            inputStream = readReportDesignFromNetwork(reportResource, departmentId);

        } else {
            ClassLoader classLoader = IReportEngine.class.getClassLoader();
            inputStream = classLoader.getResourceAsStream(reportResource.getResourceLocation());
        }

        // Check if report file exists and is accessible
        if (inputStream == null) {
            logger.error(String.format(
                    "The report file %s does not exist.  Cannot generate the %s document.",
                    reportResource.getResourceLocation(),
                    reportResource.getDownloadName()));
            throw RestApiException.getInternalErrorException(
                    ErrorCode.PRN_I_002,
                    new Object[] { reportResource.getDownloadName() },
                    new Object[] { reportResource.getDownloadName() });
        }

        // Generate the report
        try {

            generateDocument(inputStream, parameters, destination, type);

        } catch (EngineException e) {
            logger.error(String.format(
                    "A BIRT engine error occurred while generating a %s document.  %s",
                    reportResource.getDownloadName(),
                    e.getMessage()));
            throw RestApiException.getInternalErrorException(ErrorCode.PRN_I_004);
        } catch (Throwable e) {
            logger.error(String.format(
                    "A exception error occurred while generating a %s document.  %s",
                    reportResource.getDownloadName(),
                    e.getMessage()));
            throw RestApiException.getInternalErrorException(
                    ErrorCode.PRN_I_007,
                    new Object[] { reportResource.getDownloadName()},
                    new Object[] { reportResource.getDownloadName() });
        }
    }

    private static void generateDocument(InputStream reportDesign, Map<String, Object> parameters, OutputStream destination, String type) throws Exception {

        if (documentEngine == null) {
            throw RestApiException.getInternalErrorException(ErrorCode.PRN_I_001);
        }

        // Check if report file exists and is accessible
        if (reportDesign == null) {
            logger.error(String.format(
                    "The report design file does not exist.  Cannot generate the document."));
            throw RestApiException.getInternalErrorException(ErrorCode.PRN_I_008);
        }

        // Generate the report
        IReportRunnable report = documentEngine.openReportDesign(reportDesign);
        IRunAndRenderTask task = documentEngine.createRunAndRenderTask(report);

        task.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, BirtEngine.class.getClassLoader());
        task.setParameterValues(parameters);

        type = REPORT_TYPE_EXCEL.equals(type) ? type : REPORT_TYPE_PDF;

        RenderOption option = REPORT_TYPE_EXCEL.equals(type) ? new EXCELRenderOption() : new PDFRenderOption();
        option.setOutputFormat(type);
        option.setOutputStream(destination);
        task.setRenderOption(option);

        task.run();
        task.close();
    }

    private static String getReportDesignNetworkPath(DocumentResources reportResource, Long departmentId) {
        String subLocation = reportResource.documentFileStore + (departmentId!=null ? departmentId.longValue()+"/" : "" ) + reportResource.fileName;
        return AppConfig.networkFileStorageLocation + "/" + subLocation;
    }

    private static InputStream readReportDesignFromNetwork(DocumentResources reportResource, Long departmentId) {

        if (departmentId != null) {

            try {
                return new FileInputStream(getReportDesignNetworkPath(reportResource, departmentId));
            } catch (FileNotFoundException e) {
                logger.error(String.format(
                        "The report file %s does not exist for department ID %s.",
                        reportResource.getResourceLocation(),
                        departmentId.toString()));
            }

        }

        try {
            return new FileInputStream(getReportDesignNetworkPath(reportResource, null));
        } catch (FileNotFoundException e) {
            logger.error(String.format(
                    "The report file %s does not exist.  Cannot generate the %s document.",
                    reportResource.getResourceLocation(),
                    reportResource.getDownloadName()));
            throw RestApiException.getInternalErrorException(
                    ErrorCode.PRN_I_002,
                    new Object[] { reportResource.getDownloadName() },
                    new Object[] { reportResource.getDownloadName() });
        }

    }

    public static void destroy() {
        documentEngine.destroy();
        Platform.shutdown();
        RegistryProviderFactory.releaseDefault();
    }
}