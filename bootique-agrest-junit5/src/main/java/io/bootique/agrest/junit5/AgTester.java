package io.bootique.agrest.junit5;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
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
        Invocation.Builder requestBuilder = target.request();
        requestCustomizer.accept(requestBuilder);
        return requestBuilder;
    }
}
