package eu.cafestube.cloudnet.service;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.regex.Pattern;

public record ServiceDeployment(
        @NotNull ServiceTemplate template,
        @NotNull Collection<String> excludes,
        @NotNull Collection<String> includes
) {
}
