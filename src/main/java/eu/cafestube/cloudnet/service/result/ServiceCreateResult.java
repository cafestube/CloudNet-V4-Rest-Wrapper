package eu.cafestube.cloudnet.service.result;

import eu.cafestube.cloudnet.service.status.ServiceInfoSnapshot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record ServiceCreateResult(
        @NotNull State state,
        @Nullable UUID creationId,
        ServiceInfoSnapshot serviceInfo
) {


    public enum State {

        CREATED,
        DEFERRED,
        FAILED
    }

}
