package eu.cafestube.cloudnet.service.status;

import com.google.gson.JsonObject;
import eu.cafestube.cloudnet.service.HostAndPort;
import eu.cafestube.cloudnet.service.ProcessConfiguration;
import eu.cafestube.cloudnet.service.ServiceConfiguration;

public record ServiceInfoSnapshot(
    long creationTime,
    HostAndPort address,
    ServiceConfiguration configuration,
    long connectedTime,
    ProcessSnapshot processSnapshot,
    ServiceLifeCycle lifeCycle,
    JsonObject properties
) {
}
