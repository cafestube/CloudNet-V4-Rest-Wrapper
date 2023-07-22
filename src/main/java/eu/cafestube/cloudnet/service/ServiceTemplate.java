package eu.cafestube.cloudnet.service;

import org.jetbrains.annotations.NotNull;

public record ServiceTemplate(
        @NotNull String prefix,
        @NotNull String name,
        @NotNull String storage,
        int priority,
        boolean alwaysCopyToStaticServices
) {
}
