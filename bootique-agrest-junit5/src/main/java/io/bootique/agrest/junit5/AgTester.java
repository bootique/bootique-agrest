package io.bootique.agrest.junit5;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

/**
 * DSL for sending Agrest requests and asserting responses. Can be subclassed to customize the requests
 */
public class AgTester {

    private WebTarget target;

    public static AgTester request(WebTarget target) {
        return new AgTester(target);
    }

    protected AgTester(WebTarget target) {
        this.target = Objects.requireNonNull(target);
    }

    public AgResponseAssertions get() {
        return onResponse(request().get());
    }

    public AgResponseAssertions put(String data) {
        Objects.requireNonNull(data);
        Response r = request().put(Entity.entity(data, MediaType.APPLICATION_JSON_TYPE));
        return onResponse(r);
    }

    public AgResponseAssertions post(String data) {
        Objects.requireNonNull(data);
        Response r = request().post(Entity.entity(data, MediaType.APPLICATION_JSON_TYPE));
        return onResponse(r);
    }

    protected static AgResponseAssertions onResponse(Response response) {
        return new AgResponseAssertions(response);
    }

    protected Invocation.Builder request() {
        // allows subclasses to override request creation to add things like auth headers, etc.
        return target.request();
    }
}
