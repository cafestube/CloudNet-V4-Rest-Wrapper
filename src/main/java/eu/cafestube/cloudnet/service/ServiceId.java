package eu.cafestube.cloudnet.service;

import com.google.common.base.Preconditions;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public record ServiceId(
        @NotNull String taskName,
        @NotNull String nameSplitter,
        @NotNull Set<String> allowedNodes,
        @NotNull UUID uniqueId,
        int taskServiceId,
        @Nullable String nodeUniqueId,
        @NotNull String environmentName,
        @Nullable CustomServiceEnvironmentType environment
) {


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private UUID uniqueId = UUID.randomUUID();
        private String taskName;
        private int taskServiceId = -1;
        private String nodeUniqueId;
        private String environmentName;
        private String nameSplitter;
        private CustomServiceEnvironmentType environment;
        private Set<String> allowedNodes;

        public @NotNull Builder uniqueId(@NotNull UUID uniqueId) {
            this.uniqueId = uniqueId;
            return this;
        }

        public @NotNull Builder taskName(@NotNull String taskName) {
            this.taskName = taskName;
            return this;
        }

        public @NonNull Builder taskServiceId(int taskServiceId) {
            this.taskServiceId = taskServiceId;
            return this;
        }

        public @NonNull Builder nodeUniqueId(@Nullable String nodeUniqueId) {
            this.nodeUniqueId = nodeUniqueId;
            return this;
        }

        public @NonNull Builder nameSplitter(@NonNull String nameSplitter) {
            this.nameSplitter = nameSplitter;
            return this;
        }

        public @NonNull Builder environment(@NonNull String environmentName) {
            this.environmentName = environmentName;
            return this;
        }

        public @NonNull Builder environment(@NonNull DefaultServiceEnvironment environment) {
            this.environmentName = environment.name();
            return this;
        }

        public @NonNull Builder environment(@Nullable CustomServiceEnvironmentType environment) {
            if (environment != null) {
                this.environment = environment;
                this.environmentName = environment.name();
            }
            return this;
        }

        public @NonNull Builder allowedNodes(@NonNull Collection<String> allowedNodes) {
            this.allowedNodes = new HashSet<>(allowedNodes);
            return this;
        }

        public @NonNull Builder modifyAllowedNodes(@NonNull Consumer<Collection<String>> modifier) {
            modifier.accept(this.allowedNodes);
            return this;
        }

        public @NonNull ServiceId build() {
            Preconditions.checkNotNull(this.taskName, "no task name given");
            Preconditions.checkNotNull(this.environmentName, "no environment given");
            Preconditions.checkArgument(this.taskServiceId == -1 || this.taskServiceId > 0, "taskServiceId <= 0");

            return new ServiceId(
                    this.taskName,
                    this.nameSplitter,
                    Set.copyOf(this.allowedNodes),
                    this.uniqueId,
                    this.taskServiceId,
                    this.nodeUniqueId,
                    this.environmentName,
                    this.environment
            );
        }


    }

}
