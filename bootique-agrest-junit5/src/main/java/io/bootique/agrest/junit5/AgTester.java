package io.bootique.agrest.junit5;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * DSL for sending Agrest requests and asserting responses.
 */
public class AgTester {

    private WebTarget target;
    private Consumer<Invocation.Builder> requestCustomizer;

    public static AgTester request(WebTarget target) {
        return new AgTester(target);
    }

    protected AgTester(WebTarget target) {
        this.target = Objects.requireNonNull(target);
        this.requestCustomizer = b -> {
        };
    }

    public AgTester customizeRequest(Consumer<Invocation.Builder> requestCustomizer) {
        this.requestCustomizer = Objects.requireNonNull(requestCustomizer);
        return this;
    }

    public AgResponseMatcher get() {
        return onResponse(request().get());
    }

    public AgResponseMatcher put(String data) {
        Objects.requireNonNull(data);
        Response r = request().put(Entity.entity(data, MediaType.APPLICATION_JSON_TYPE));
        return onResponse(r);
    }

    /**
     * Runs a POST request, submitting the data string "application/json" type.
     */
    public AgResponseMatcher post(String data) {
        Objects.requireNonNull(data);
        Response r = request().post(Entity.entity(data, MediaType.APPLICATION_JSON_TYPE));
        return onResponse(r);
    }

    /**
     * Runs a POST request, submitting the contents of the form as "application/x-www-form-urlencoded" type.
     *
     * @since 3.0
     */
    public AgResponseMatcher post(Form form) {
        Objects.requireNonNull(form);
        Response r = request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
        return onResponse(r);
    }

    /**
     * @since 2.0.B1
     */
    public AgResponseMatcher delete() {
        return onResponse(request().delete());
    }

    protected static AgResponseMatcher onResponse(Response response) {
        return new AgResponseMatcher(response);
    }

    protected Invocation.Builder request() {
        Invocation.Builder requestBuilder = target.request();
        requestCustomizer.accept(requestBuilder);
        return requestBuilder;
    }
}
