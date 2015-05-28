package org.biokoframework.http.scenario;

import org.apache.commons.io.IOUtils;
import org.biokoframework.system.KILL_ME.commons.HttpMethod;

import java.io.IOException;
import java.util.Map;

/**
 * @author Mikol Faro <mikol.faro@gmail.com>
 * @date 2014-09-08
 */
public class BinaryRequestStep implements ScenarioStep {

    public final HttpMethod fHttpMethod;
    public final String fRestUrl;
    public final Map<String, String> fParameters;
    public final String fExpectedContentType;
    private final byte[] fByteArray;

    public BinaryRequestStep(HttpMethod method, String restURL, Map<String, String> headers, Map<String, String> parameters, String expectedContentType, String filename) throws IOException {
        fHttpMethod = method;
        fRestUrl = restURL;
//        fHeaders = headers;
        fParameters = parameters;
        fExpectedContentType = expectedContentType;

        if (filename != null) {
            fByteArray = IOUtils.toByteArray(getClass().getResource("/" + filename));
        } else {
            fByteArray = null;
        }
    }
}
