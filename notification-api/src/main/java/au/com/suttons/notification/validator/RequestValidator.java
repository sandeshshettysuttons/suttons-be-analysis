package au.com.suttons.notification.validator;

import au.com.suttons.notification.mapper.ResourceAlias;
import au.com.suttons.notification.resource.error.RestApiException;
import au.com.suttons.notification.util.DonUtil;
import au.com.suttons.notification.util.ListUtil;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response.Status;

public class RequestValidator
{
    /**
     * Validates the given expand query. Firstly it will validate the syntax, then make sure that all
     * resource names and fields are correct.
     *
     * @param expand - The expand query entered by the client.
     * @param request - The {@link HttpServletRequest} used to make the request.
     *
     *
     * @return The original expand query.
     *
     * @throws au.com.suttons.notification.resource.RestApiException If the expand query fails any part of validation.
     */
    public static String validateExpandQuery(String expand, HttpServletRequest request)
    {
        if (expand == null) {
            return null;
        }

        RestApiException badRequest = new RestApiException(Status.BAD_REQUEST.getStatusCode(), null);

        /**
         *  Validate the entire expressions syntax first
         */
        if (!DonUtil.isValid(expand)) {
            badRequest.setMessage("The expand query is incorrectly formed.");
            badRequest.setDeveloperMessage(String.format("The expand query was found to be malformed. Please check the " +
                    "syntax and try again. Expand query: %s", expand));

            throw badRequest;
        }

        Map<String, String> expandMap = DonUtil.parse(expand);

        for (Map.Entry<String, String> entry : expandMap.entrySet()) {
            String resourceName = entry.getKey();
            String fieldNames = entry.getValue();

            if (!resourceName.equals("*")) {
                Class resourceClass;

                /**
                 *  Validate that the resource name used is valid for the requested resource type
                 *  (i.e. 'department' is a valid resource name, but 'department' is NOT a valid resource name when expanded on an Appointment resource
                 */
                try {
                    // This will throw an exception if 'resourceName' is not valid
                    resourceClass = ResourceAlias.valueOf(resourceName).clazz;

                    ResourceAlias requestedResourceAlias = RequestValidator.getResourceAliasFromRequest(request);

                    if (requestedResourceAlias != null) {
                        boolean resourceFound = false;

                        if (!resourceName.equals(requestedResourceAlias.name())) {
                            for (Field responseField : requestedResourceAlias.clazz.getDeclaredFields()) {
                                String fieldType = (responseField.getType().equals(List.class)) ? ListUtil.getGenericTypeOfList(responseField) : responseField.getType().getSimpleName();

                                if (fieldType.equals(resourceClass.getSimpleName())) {
                                    resourceFound = true;
                                }
                            }

                            if (!resourceFound) {
                                // Throw a generic exception (this will be caught by the catch block and turned into the correct exception)
                                throw new RestApiException(Status.BAD_REQUEST.getStatusCode(), null);
                            }
                        }
                    }
                }
                catch (Exception e) {
                    badRequest.setMessage("An invalid resource name was specified in the requested expand query.");
                    badRequest.setDeveloperMessage(String.format("A resource name specified in the expand query does not " +
                            "exist. The resource name '%s' does not exist.", resourceName));

                    throw badRequest;
                }

                /**
                 *  Validate that the each field name is valid for the resource class
                 */
                fieldNames = fieldNames.replaceAll(" ", "");

                for (String fieldName : fieldNames.split(",")) {
                    if (!fieldName.equals("*")) {
                        boolean fieldFound = false;

                        for (Field resourceField : resourceClass.getDeclaredFields()) {
                            if (resourceField.getName().equals(fieldName)) {
                                fieldFound = true;
                            }
                        }

                        if (!fieldFound) {
                            badRequest.setMessage("An invalid field name was specified in the requested expand query.");
                            badRequest.setDeveloperMessage(String.format("A field name specified in the expand query does not " +
                                    "exist on the given resource. The field name '%s' does not exist on resource '%s'.", fieldName, resourceName));

                            throw badRequest;
                        }
                    }
                }
            }
        }

        return expand;
    }

    private static ResourceAlias getResourceAliasFromRequest(HttpServletRequest request)
    {
        String[] resourceChunks = request.getRequestURI().split("/");

        for (int i = resourceChunks.length - 1; i >= 0; i--) {
            String chunk = resourceChunks[i];

            if (ResourceAlias.fromResourceType(chunk) != null) {
                return ResourceAlias.fromResourceType(chunk);
            }
        }

        return null;
    }
}
