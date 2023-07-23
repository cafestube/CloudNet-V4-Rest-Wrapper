package eu.cafestube.cloudnet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import eu.cafestube.cloudnet.exception.TaskNotFoundException;
import eu.cafestube.cloudnet.service.LifecycleUpdate;
import eu.cafestube.cloudnet.service.ServiceConfiguration;
import eu.cafestube.cloudnet.service.result.ServiceCreateResult;
import eu.cafestube.cloudnet.task.ServiceTask;
import org.jetbrains.annotations.Nullable;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class CloudNetServiceManager {

    private final Gson gson = new Gson();
    private final CloudNetApiClient client;

    public CloudNetServiceManager(CloudNetApiClient client) {
        this.client = client;
    }


    public ServiceCreateResult createService(String taskName, boolean start) throws TaskNotFoundException {
        HttpResponse<JsonObject> response = client.newCloudNetCall("service/create", builder -> {
            JsonObject data = new JsonObject();
            data.addProperty("serviceTaskName", taskName);
            data.addProperty("start", start);
            builder.POST(CloudNetApiClient.jsonBody(data));
        }, CloudNetApiClient.jsonBodyHandler());


        if(response.statusCode() == 400) {
            throw new TaskNotFoundException(taskName);
        }

        if(response.statusCode() != 200)
            throw new RuntimeException("Failed to create service for task " + taskName );


        JsonObject data = response.body();

        return gson.fromJson(data.get("result"), ServiceCreateResult.class);
    }

    public ServiceCreateResult createService(ServiceConfiguration configuration, boolean start) {
        HttpResponse<Optional<JsonObject>> response = client.newCloudNetCall("service/create", builder -> {
            JsonObject data = new JsonObject();
            data.add("serviceConfiguration", gson.toJsonTree(configuration));
            data.addProperty("start", start);
            builder.POST(CloudNetApiClient.jsonBody(data));
        }, CloudNetApiClient.optionalJsonBodyHandler());


        if(response.statusCode() == 400) {
            throw new IllegalArgumentException("Invalid service configuration");
        }

        if(response.statusCode() != 200)
            throw new RuntimeException("Failed to create service");

        JsonObject data = response.body().orElse(null);

        if (data == null) {
            throw new RuntimeException("Failed to create service");
        }

        return gson.fromJson(data.get("result"), ServiceCreateResult.class);
    }

    public void updateServiceState(LifecycleUpdate state, UUID serviceId) {
        client.newCloudNetCall("service/" + serviceId + "/lifecycle?target=" + state.name().toLowerCase(Locale.ROOT), builder -> {
            builder.method("PATCH", HttpRequest.BodyPublishers.noBody());
        }, HttpResponse.BodyHandlers.discarding());
    }

}
