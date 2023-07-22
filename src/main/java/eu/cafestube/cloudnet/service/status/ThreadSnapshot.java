package eu.cafestube.cloudnet.service.status;

import org.jetbrains.annotations.NotNull;

record ThreadSnapshot(
        long id,
        int priority,
        boolean daemon,
        @NotNull String name,
        @NotNull Thread.State threadState
) {

}