package eu.cafestube.cloudnet.service;

import org.jetbrains.annotations.NotNull;

public record HostAndPort(
        @NotNull String host,
        int port
) {
}
