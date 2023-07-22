package eu.cafestube.cloudnet.service;

import com.google.gson.JsonObject;

public record ServiceRemoteInclusion(
        String url, String destination, JsonObject properties
) {
}
