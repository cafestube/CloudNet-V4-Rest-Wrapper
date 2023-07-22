package eu.cafestube.cloudnet;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.Authenticator;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class CloudNetApiClient {



    private final CloudNetServiceManager serviceManager = new CloudNetServiceManager(this);
    private final CloudNetTaskManager taskManager = new CloudNetTaskManager(this);

    private final String username;
    private final String password;

    private String host;
    private String cloudNetToken;

    private final HttpClient cloudNetClient;

    public CloudNetApiClient(String host, String username, String password) {
        this.username = username;
        this.password = password;
        this.host = host;

        this.cloudNetClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();

        this.cloudNetToken = login();
    }

    private String login() {
        try {
            HttpResponse<JsonObject> response = cloudNetClient.send(HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .uri(URI.create(this.host + "/api/v2/auth"))
                    .setHeader("Authorization", "Basic " + java.util.Base64.getEncoder()
                            .encodeToString((this.username + ":" + this.password).getBytes(StandardCharsets.UTF_8)))
                    .build(), jsonBodyHandler());

            if(response.statusCode() != 200) {
                throw new RuntimeException("Failed to login");
            }

            return response.body().get("token").getAsString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> HttpResponse<T> newCloudNetCall(String path, Consumer<HttpRequest.Builder> consumer, HttpResponse.BodyHandler<T> bodyHandler) {
        return newCloudNetCall(path, consumer, bodyHandler, false);
    }

    public <T> HttpResponse<T> newCloudNetCall(String path, HttpResponse.BodyHandler<T> bodyHandler) {
        return newCloudNetCall(path, builder -> {}, bodyHandler);
    }

    private  <T> HttpResponse<T> newCloudNetCall(String path, Consumer<HttpRequest.Builder> consumer, HttpResponse.BodyHandler<T> bodyHandler, boolean isRetry) {
        try {
            HttpRequest.Builder request = HttpRequest.newBuilder()
                    .uri(URI.create(this.host + "/api/v2/" + path))
                    .setHeader("Authorization", "Bearer " + this.cloudNetToken);
            consumer.accept(request);
            HttpResponse<T> response = cloudNetClient.send(request.build(), bodyHandler);

            if(response.statusCode() == 401 && !isRetry) {
                this.cloudNetToken = login();
                return newCloudNetCall(path, consumer, bodyHandler, true);
            } else if(response.statusCode() == 401) {
                throw new RuntimeException("Failed to authenticate");
            }

            return response;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public CloudNetServiceManager getServiceManager() {
        return serviceManager;
    }

    public CloudNetTaskManager getTaskManager() {
        return taskManager;
    }

    public static HttpResponse.BodyHandler<JsonObject> jsonBodyHandler() {
        return responseInfo -> HttpResponse.BodySubscribers.mapping(
                HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                s -> JsonParser.parseString(s).getAsJsonObject()
        );
    }

    public static HttpResponse.BodyHandler<Optional<JsonObject>> optionalJsonBodyHandler() {
        return responseInfo -> HttpResponse.BodySubscribers.mapping(
                HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                s -> {
                    if(s.isEmpty())
                        return Optional.empty();
                    return Optional.of(JsonParser.parseString(s).getAsJsonObject());
                }
        );
    }

    public static HttpRequest.BodyPublisher jsonBody(JsonObject object) {
        return HttpRequest.BodyPublishers.ofString(object.toString(), StandardCharsets.UTF_8);
    }
}
