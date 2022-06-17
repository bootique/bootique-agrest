package io.bootique.agrest.jakarta.junit5;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.bootique.jetty.junit5.JettyTester;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @since 2.0
 */
public class AgResponseMatcher {

    private static final Pattern NUMERIC_ID_MATCHER = Pattern.compile("\"id\":([\\d]+)");

    private final Response response;
    private String idPlaceholder;

    private String responseContent;
    private byte[] responseBinContent;

    public AgResponseMatcher(Response response) {
        this.response = Objects.requireNonNull(response);
    }

    /**
     * Returns the first "id" field encoded in JSON.
     *
     * @return the first "id" field encoded in JSON or null
     */
    // TODO: since there may be many "id" fields in the hierarchy, it would probably make more sense to parse JSON
    //  and look for ID at the top level
    public Long getId() {
        Matcher matcher = NUMERIC_ID_MATCHER.matcher(getContentAsString());
        return matcher.find() ? Long.valueOf(matcher.group(1)) : null;
    }

    protected String buildExpectedJson(long total, String... jsonObjects) {
        StringBuilder expectedJson = new StringBuilder("{\"data\":[");

        if (jsonObjects.length > 0) {
            for (String o : jsonObjects) {
                expectedJson.append(o).append(",");
            }

            // remove last comma
            expectedJson.deleteCharAt(expectedJson.length() - 1);
        }

        expectedJson.append("],\"total\":")
                .append(total)
                .append("}");
        return expectedJson.toString();
    }

    public String getContentAsString() {

        if (responseBinContent != null) {
            throw new IllegalStateException("Response already read as binary");
        }

        // cache read content, as Response won't allow to read it twice..
        if (responseContent == null) {
            responseContent = response.readEntity(String.class);
        }

        return responseContent;
    }

    public byte[] getContentAsBytes() {
        if (responseContent != null) {
            throw new IllegalStateException("Response already read as String");
        }

        // cache read content, as Response won't allow to read it twice..
        if (responseBinContent == null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            try (InputStream in = response.readEntity(InputStream.class)) {

                byte[] buffer = new byte[8192];
                int read;
                while ((read = in.read(buffer, 0, buffer.length)) >= 0) {
                    out.write(buffer, 0, read);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            this.responseBinContent = out.toByteArray();
        }

        return responseBinContent;
    }

    public AgResponseMatcher assertOk() {
        JettyTester.assertOk(response);
        return this;
    }

    public AgResponseMatcher assertOk(String message) {
        JettyTester.assertOk(response, message);
        return this;
    }

    public AgResponseMatcher assertCreated() {
        JettyTester.assertCreated(response);
        return this;
    }

    public AgResponseMatcher assertCreated(String message) {
        JettyTester.assertCreated(response, message);
        return this;
    }

    public AgResponseMatcher assertBadRequest() {
        JettyTester.assertBadRequest(response);
        return this;
    }

    public AgResponseMatcher assertBadRequest(String message) {
        JettyTester.assertBadRequest(response, message);
        return this;
    }

    public AgResponseMatcher assertUnauthorized() {
        JettyTester.assertUnauthorized(response);
        return this;
    }

    public AgResponseMatcher assertUnauthorized(String message) {
        JettyTester.assertUnauthorized(response, message);
        return this;
    }

    public AgResponseMatcher assertForbidden() {
        JettyTester.assertForbidden(response);
        return this;
    }

    public AgResponseMatcher assertForbidden(String message) {
        JettyTester.assertForbidden(response, message);
        return this;
    }

    public AgResponseMatcher assertNotFound() {
        JettyTester.assertNotFound(response);
        return this;
    }

    public AgResponseMatcher assertNotFound(String message) {
        JettyTester.assertNotFound(response, message);
        return this;
    }

    public AgResponseMatcher assertStatus(Response.Status expectedStatus) {
        JettyTester.assertStatus(response, expectedStatus.getStatusCode());
        return this;
    }

    public AgResponseMatcher assertMediaType(MediaType expected) {
        assertEquals(expected, response.getMediaType());
        return this;
    }

    public AgResponseMatcher assertMediaType(String expected) {
        assertEquals(MediaType.valueOf(expected), response.getMediaType());
        return this;
    }

    /**
     * Replaces the first found id value in the result with a known placeholder, this allowing to compare JSON coming
     * with an unknown id.
     */
    public AgResponseMatcher replaceId(String idPlaceholder) {
        this.idPlaceholder = idPlaceholder;
        return this;
    }

    public AgResponseMatcher assertContent(String expected) {
        String actual = getContentAsString();
        String normalized = idPlaceholder != null
                ? NUMERIC_ID_MATCHER.matcher(actual).replaceFirst("\"id\":" + idPlaceholder)
                : actual;

        assertEquals(expected, normalized, "Response contains unexpected JSON");
        return this;
    }

    public AgResponseMatcher assertContent(long total, String... jsonObjects) {
        return assertContent(buildExpectedJson(total, jsonObjects));
    }

    public AgResponseMatcher assertTotal(long total) {

        String string = getContentAsString();
        JsonNode rootNode;
        try {
            rootNode = new ObjectMapper().readTree(string);
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON", e);
        }
        assertNotNull(rootNode, "No response data");

        JsonNode totalNode = rootNode.get("total");
        assertNotNull(totalNode, "No 'total' info");

        assertEquals(total, totalNode.asLong(), "Unexpected total");

        return this;
    }
}
