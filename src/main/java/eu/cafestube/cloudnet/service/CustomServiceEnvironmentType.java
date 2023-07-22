package eu.cafestube.cloudnet.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;


public record CustomServiceEnvironmentType(
        @NotNull String name,
        int defaultServiceStartPort,
        @NotNull Set<String> defaultProcessArguments,
        @NotNull JsonObject properties
) {

    public static @NonNull Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private int defaultServiceStartPort = 44955;
        private Set<String> defaultProcessArguments;
        private JsonObject properties;



        public @NotNull Builder name(@NotNull String name) {
            this.name = name;
            return this;
        }

        public @NotNull Builder defaultServiceStartPort(int defaultServiceStartPort) {
            this.defaultServiceStartPort = defaultServiceStartPort;
            return this;
        }

        public @NotNull Builder withProperty(@NotNull String key, @NotNull String value) {
            this.properties.addProperty(key, value);
            return this;
        }

        public @NotNull Builder withProperty(@NotNull String key, @NotNull Number value) {
            this.properties.addProperty(key, value);
            return this;
        }

        public @NotNull Builder withProperty(@NotNull String key, @NotNull Boolean value) {
            this.properties.addProperty(key, value);
            return this;
        }

        public @NotNull Builder withProperty(@NotNull String key, @NotNull JsonElement value) {
            this.properties.add(key, value);
            return this;
        }

        public @NotNull Builder defaultProcessArguments(@NotNull Collection<String> defaultProcessArguments) {
            this.defaultProcessArguments = new HashSet<>(defaultProcessArguments);
            return this;
        }

        public @NotNull Builder modifyDefaultProcessArguments(@NotNull Consumer<Collection<String>> modifier) {
            modifier.accept(this.defaultProcessArguments);
            return this;
        }

        public @NotNull CustomServiceEnvironmentType build() {
            Preconditions.checkNotNull(this.name, "no name given");
            Preconditions.checkArgument(this.defaultServiceStartPort >= 0 && this.defaultServiceStartPort <= 0xFFFF,
                    "invalid default port");

            return new CustomServiceEnvironmentType(
                this.name,
                this.defaultServiceStartPort,
                ImmutableSet.copyOf(this.defaultProcessArguments),
                this.properties
            );
        }

    }


}
