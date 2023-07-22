package eu.cafestube.cloudnet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import eu.cafestube.cloudnet.task.ServiceTask;
import org.jetbrains.annotations.Nullable;

import java.net.http.HttpResponse;

public class CloudNetTaskManager {

    private final Gson gson = new Gson();
    private final CloudNetApiClient client;

    public CloudNetTaskManager(CloudNetApiClient client) {
        this.client = client;
    }

    public @Nullable ServiceTask getServiceTask(String name) {
        HttpResponse<JsonObject> response = client.newCloudNetCall("task/" + name, CloudNetApiClient.jsonBodyHandler());

        if(response.statusCode() != 200)
            throw new RuntimeException("Failed to get service task " + name + " from cloudnet");

        JsonObject data = response.body();

        if(!data.get("success").getAsBoolean())
            return null;

        return gson.fromJson(data.get("task"), ServiceTask.class);
    }


}
