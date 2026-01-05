package com.cybershrek.tools;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class HandyClient {

    private final HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();
    private final HttpRequest.Builder builder = HttpRequest.newBuilder()
            .timeout(Duration.ofMinutes(30))
            .header("Content-Type", "application/json");

    //-----//
    // URL //
    //-----//
    public HandyClient url(String url) {
        builder.uri(URI.create(url));
        return this;
    }

    //---------//
    // HEADERS //
    //---------//
    public HandyClient headers(Map<String, String> headers){
        headers.forEach(builder::header);
        return this;
    }
    public HandyClient header(String name, String value) {
        builder.header(name, value);
        return this;
    }

    //------//
    // BODY //
    //------//
    public WithBody body(String body) {
        return new WithBody(body);
    }
    public class WithBody {
        private final String body;
        private WithBody(String body) {
            this.body = body;
        }

        public HttpResponse<String> POST() {
            return new Request(Method.POST, body).fetch();
        }
        public HttpResponse<String> PUT() {
            return new Request(Method.PUT, body).fetch();
        }
        public CompletableFuture<HttpResponse<String>> asyncPOST() {
            return new Request(Method.POST, body).fetchAsync();
        }
        public CompletableFuture<HttpResponse<String>> asyncPUT() {
            return new Request(Method.PUT, body).fetchAsync();
        }
    }
    public HttpResponse<String> GET() {
        return new Request(Method.GET).fetch();
    }
    public HttpResponse<String> DELETE() {
        return new Request(Method.DELETE).fetch();
    }
    public CompletableFuture<HttpResponse<String>> asyncGET() {
        return new Request(Method.GET).fetchAsync();
    }
    public CompletableFuture<HttpResponse<String>> asyncDELETE() {
        return new Request(Method.DELETE).fetchAsync();
    }

    //----------------------//
    // REQUEST AND RESPONSE //
    //----------------------//
    class Request {
        private final HttpRequest request;
        Request(Method method, String nullableBody) {
            request = builder.method(method.name(), nullableBody == null
                    ? HttpRequest.BodyPublishers.noBody()
                    : HttpRequest.BodyPublishers.ofString(nullableBody))
                    .build();
        }
        Request(Method method) {
            this(method, null);
        }

        synchronized HttpResponse<String> fetch() {
            try {
                return client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
        CompletableFuture<HttpResponse<String>> fetchAsync() {
            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        }
    }
}
enum Method {
    GET,
    POST,
    PUT,
    DELETE
}
