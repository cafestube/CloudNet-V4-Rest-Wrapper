package eu.cafestube.cloudnet.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.Consumer;

public record ProcessConfiguration(
        @NotNull String environment,
        int maxHeapMemorySize,
        @NotNull @Unmodifiable List<String> jvmOptions,
        @NotNull @Unmodifiable List<String> processParameters,
        @NotNull @Unmodifiable Map<String, String> environmentVariables
) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String environment;
        private int maxHeapMemorySize = 512;

        private List<String> jvmOptions = new LinkedList<>();
        private List<String> processParameters = new LinkedList<>();
        private Map<String, String> environmentVariables = new HashMap<>();

        public @NonNull Builder maxHeapMemorySize(@Range(from = 50, to = Integer.MAX_VALUE) int maxHeapMemorySize) {
            Preconditions.checkArgument(maxHeapMemorySize > 50, "Max heap memory must be at least 50 mb");

            this.maxHeapMemorySize = maxHeapMemorySize;
            return this;
        }

        public @NonNull Builder environment(@NonNull String environment) {
            this.environment = environment;
            return this;
        }

        public @NonNull Builder environment(@NonNull CustomServiceEnvironmentType environment) {
            this.environment = environment.name();
            return this;
        }

        public @NonNull Builder environment(@NonNull DefaultServiceEnvironment environment) {
            this.environment = environment.name();
            return this;
        }

        public @NonNull Builder jvmOptions(@NonNull Collection<String> jvmOptions) {
            this.jvmOptions = new LinkedList<>(jvmOptions);
            return this;
        }

        public @NonNull Builder modifyJvmOptions(@NonNull Consumer<Collection<String>> modifier) {
            modifier.accept(this.jvmOptions);
            return this;
        }


        public @NonNull Builder processParameters(@NonNull Collection<String> processParameters) {
            this.processParameters = new LinkedList<>(processParameters);
            return this;
        }

        public @NonNull Builder modifyProcessParameters(@NonNull Consumer<Collection<String>> modifier) {
            modifier.accept(this.processParameters);
            return this;
        }

        public @NonNull Builder environmentVariables(@NonNull Map<String, String> environmentVariables) {
            this.environmentVariables = new HashMap<>(environmentVariables);
            return this;
        }


        public @NonNull Builder modifyEnvironmentVariables(@NonNull Consumer<Map<String, String>> modifier) {
            modifier.accept(this.environmentVariables);
            return this;
        }

        public @NonNull ProcessConfiguration build() {
            Preconditions.checkNotNull(this.environment, "no environment given");

            return new ProcessConfiguration(
                    this.environment,
                    this.maxHeapMemorySize,
                    ImmutableList.copyOf(this.jvmOptions),
                    ImmutableList.copyOf(this.processParameters),
                    Map.copyOf(this.environmentVariables)
            );
        }
    }

}
