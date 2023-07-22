package eu.cafestube.cloudnet.task;

import com.google.gson.JsonObject;
import eu.cafestube.cloudnet.service.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public record ServiceTask(
        @NotNull String name,
        @NotNull String runtime,
        @Nullable String javaCommand,
        @Nullable String hostAddress,
        @NotNull String nameSplitter,
        boolean disableIpRewrite,
        boolean maintenance,
        boolean autoDeleteOnStop,
        boolean staticServices,
        @NotNull Set<String> groups,
        @NotNull Set<String> associatedNodes,
        @NotNull Set<String> deletedFilesAfterStop,
        @NotNull ProcessConfiguration processConfiguration,
        int startPort,
        int minServiceCount,
        @NotNull Set<ServiceTemplate> templates,
        @NotNull Set<ServiceDeployment> deployments,
        @NotNull Set<ServiceRemoteInclusion> includes,
        JsonObject properties
) {

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        protected Set<String> jvmOptions = new LinkedHashSet<>();
        protected Set<String> processParameters = new LinkedHashSet<>();

        protected Set<ServiceTemplate> templates = new HashSet<>();
        protected Set<ServiceDeployment> deployments = new HashSet<>();
        protected Set<ServiceRemoteInclusion> includes = new HashSet<>();

        protected Map<String, String> environmentVariables = new HashMap<>();

        protected JsonObject properties = new JsonObject();

        private String name;
        private String hostAddress;
        private String javaCommand;
        private String runtime = "jvm";
        private String nameSplitter = "-";

        private boolean maintenance;
        private boolean staticServices;
        private boolean disableIpRewrite;
        private boolean autoDeleteOnStop = true;

        private Set<String> groups = new HashSet<>();
        private Set<String> associatedNodes = new HashSet<>();
        private Set<String> deletedFilesAfterStop = new HashSet<>();

        private ProcessConfiguration.Builder processConfiguration = ProcessConfiguration.builder();

        private int startPort = -1;
        private int minServiceCount = 0;


        public @NotNull Builder name(@NotNull String name) {
            this.name = name;
            return this;
        }

        public @NotNull Builder runtime(@NotNull String runtime) {
            this.runtime = runtime;
            return this;
        }

        public @NotNull Builder hostAddress(@Nullable String hostAddress) {
            this.hostAddress = hostAddress;
            return this;
        }

        public @NotNull Builder javaCommand(@Nullable String command) {
            this.javaCommand = command;
            return this;
        }

        public @NotNull Builder nameSplitter(@NotNull String nameSplitter) {
            this.nameSplitter = nameSplitter;
            return this;
        }

        public @NotNull Builder disableIpRewrite(boolean disableIpRewrite) {
            this.disableIpRewrite = disableIpRewrite;
            return this;
        }

        public @NotNull Builder maintenance(boolean maintenance) {
            this.maintenance = maintenance;
            return this;
        }

        public @NotNull Builder autoDeleteOnStop(boolean autoDeleteOnStop) {
            this.autoDeleteOnStop = autoDeleteOnStop;
            return this;
        }

        public @NotNull Builder staticServices(boolean staticServices) {
            this.staticServices = staticServices;
            return this;
        }

        public @NotNull Builder associatedNodes(@NotNull Collection<String> associatedNodes) {
            this.associatedNodes = new HashSet<>(associatedNodes);
            return this;
        }

        public @NotNull Builder modifyAssociatedNodes(@NotNull Consumer<Collection<String>> modifier) {
            modifier.accept(this.associatedNodes);
            return this;
        }


        public @NotNull Builder groups(@NotNull Collection<String> groups) {
            this.groups = new HashSet<>(groups);
            return this;
        }

        public @NotNull Builder modifyGroups(@NotNull Consumer<Collection<String>> modifier) {
            modifier.accept(this.groups);
            return this;
        }

        public @NotNull Builder deletedFilesAfterStop(@NotNull Collection<String> deletedFilesAfterStop) {
            this.deletedFilesAfterStop = new HashSet<>(deletedFilesAfterStop);
            return this;
        }

        public @NotNull Builder modifyDeletedFileAfterStop(@NotNull Consumer<Collection<String>> modifier) {
            modifier.accept(this.deletedFilesAfterStop);
            return this;
        }

        public @NotNull Builder processConfiguration(@NotNull ProcessConfiguration.Builder processConfiguration) {
            this.processConfiguration = processConfiguration;
            return this;
        }

        public @NotNull Builder minServiceCount(int minServiceCount) {
            this.minServiceCount = minServiceCount;
            return this;
        }

        public @NotNull Builder maxHeapMemory(int maxHeapMemory) {
            this.processConfiguration.maxHeapMemorySize(maxHeapMemory);
            return this;
        }

        public @NotNull Builder serviceEnvironmentType(@NotNull CustomServiceEnvironmentType serviceEnvironmentType) {
            this.processConfiguration.environment(serviceEnvironmentType);
            this.startPort = this.startPort == -1 ? serviceEnvironmentType.defaultServiceStartPort() : this.startPort;

            return this;
        }

        public @NotNull Builder serviceEnvironmentType(@NotNull DefaultServiceEnvironment serviceEnvironmentType) {
            this.processConfiguration.environment(serviceEnvironmentType);
            this.startPort = this.startPort == -1 ? 44955 : this.startPort;

            return this;
        }

        public @NotNull Builder jvmOptions(@NotNull Collection<String> jvmOptions) {
            this.processConfiguration.jvmOptions(jvmOptions);
            return this;
        }

        public @NotNull Builder modifyJvmOptions(@NotNull Consumer<Collection<String>> modifier) {
            this.processConfiguration.modifyJvmOptions(modifier);
            return this;
        }

        public @NotNull Builder processParameters(@NotNull Collection<String> processParameters) {
            this.processConfiguration.processParameters(processParameters);
            return this;
        }

        public @NotNull Builder modifyProcessParameters(@NotNull Consumer<Collection<String>> modifier) {
            this.processConfiguration.modifyProcessParameters(modifier);
            return this;
        }

        public @NotNull Builder environmentVariables(@NotNull Map<String, String> environmentVariables) {
            this.processConfiguration.environmentVariables(environmentVariables);
            return this;
        }

        public @NotNull Builder modifyEnvironmentVariables(@NotNull Consumer<Map<String, String>> modifier) {
            this.processConfiguration.modifyEnvironmentVariables(modifier);
            return this;
        }

        public ServiceTask build() {
            return new ServiceTask(
                    this.name,
                    this.runtime,
                    this.hostAddress,
                    this.javaCommand,
                    this.nameSplitter,
                    this.disableIpRewrite,
                    this.maintenance,
                    this.autoDeleteOnStop,
                    this.staticServices,
                    Set.copyOf(this.groups),
                    Set.copyOf(this.associatedNodes),
                    Set.copyOf(this.deletedFilesAfterStop),
                    this.processConfiguration.build(),
                    this.startPort,
                    this.minServiceCount,
                    Set.copyOf(this.templates),
                    Set.copyOf(this.deployments),
                    Set.copyOf(this.includes),
                    this.properties.deepCopy()
            );
        }
    }

}
