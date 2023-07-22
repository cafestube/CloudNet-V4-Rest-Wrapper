package eu.cafestube.cloudnet.service;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import eu.cafestube.cloudnet.task.ServiceTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public record ServiceConfiguration(
        ServiceId serviceId,
        ProcessConfiguration processConfig,
        JsonObject retryConfiguration,
        int port,
        String runtime,
        String hostAddress,
        String javaCommand,
        boolean autoDeleteOnStop,
        boolean staticService,
        Set<String> groups,
        Set<String> deletedFilesAfterStop,
        Set<ServiceTemplate> templates,
        Set<ServiceDeployment> deployments,
        Set<ServiceRemoteInclusion> includes,
        JsonObject properties
) {


    public static @NotNull Builder builder(@NotNull ServiceTask task) {
        return builder()
                .taskName(task.name())

                .runtime(task.runtime())
                .hostAddress(task.hostAddress())
                .javaCommand(task.javaCommand())
                .nameSplitter(task.nameSplitter())

                .autoDeleteOnStop(task.autoDeleteOnStop())
                .staticService(task.staticServices())

                .allowedNodes(task.associatedNodes())
                .groups(task.groups())
                .deletedFilesAfterStop(task.deletedFilesAfterStop())

                .templates(task.templates())
                .deployments(task.deployments())
                .inclusions(task.includes())

                .jvmOptions(task.processConfiguration().jvmOptions())
                .processParameters(task.processConfiguration().processParameters())
                .environmentVariables(task.processConfiguration().environmentVariables())

                .environment(task.processConfiguration().environment())
                .maxHeapMemory(task.processConfiguration().maxHeapMemorySize())
                .startPort(task.startPort());
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ServiceId.Builder serviceId = ServiceId.builder();
        private ProcessConfiguration.Builder processConfig = ProcessConfiguration.builder();
        private JsonObject retryConfiguration;
        private int port;
        private String runtime;
        private String hostAddress;
        private String javaCommand;
        private boolean autoDeleteOnStop;
        private boolean staticService;
        private Set<String> groups = new HashSet<>();
        private Set<String> deletedFilesAfterStop = new HashSet<>();
        private Set<ServiceTemplate> templates = new HashSet<>();
        private Set<ServiceDeployment> deployments = new HashSet<>();
        private Set<ServiceRemoteInclusion> includes = new HashSet<>();
        private JsonObject properties = new JsonObject();

        public Builder() {
            dontRetry();
        }

        public Builder taskName(@NotNull String taskName) {
            this.serviceId.taskName(taskName);
            return this;
        }

        public Builder environment(@NotNull DefaultServiceEnvironment environment) {
            this.serviceId.environment(environment);
            this.processConfig.environment(environment);
            return this;
        }


        public Builder environment(@NotNull CustomServiceEnvironmentType environment) {
            this.serviceId.environment(environment);
            this.processConfig.environment(environment);
            return this;
        }

        public Builder maxHeapMemory(int maxHeapMemory) {
            this.processConfig.maxHeapMemorySize(maxHeapMemory);
            return this;
        }


        public Builder environment(@NotNull String environment) {
            this.serviceId.environment(environment);
            this.processConfig.environment(environment);
            return this;
        }

        public Builder nameSplitter(@NotNull String nameSplitter) {
            this.serviceId.nameSplitter(nameSplitter);
            return this;
        }

        public Builder allowedNodes(@NotNull Collection<String> allowedNodes) {
            this.serviceId.allowedNodes(allowedNodes);
            return this;
        }

        public Builder serviceId(@NotNull ServiceId.Builder serviceId) {
            this.serviceId = serviceId;
            return this;
        }

        public Builder processConfig(@NotNull ProcessConfiguration.Builder processConfig) {
            this.processConfig = processConfig;
            return this;
        }

        public Builder retryConfiguration(@NotNull JsonObject retryConfiguration) {
            this.retryConfiguration = retryConfiguration;
            return this;
        }

        public Builder dontRetry() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("maxRetries", 0);
            jsonObject.add("backoffStrategy", new JsonArray());
            jsonObject.add("eventReceivers", new JsonObject());
            this.retryConfiguration = jsonObject;
            return this;
        }

        public Builder startPort(int port) {
            this.port = port;
            return this;
        }

        public Builder jvmOptions(@NotNull Collection<String> jvmOptions) {
            this.processConfig.jvmOptions(jvmOptions);
            return this;
        }

        public Builder modifyJvmOptions(@NotNull Consumer<Collection<String>> jvmOptions) {
            this.processConfig.modifyJvmOptions(jvmOptions);
            return this;
        }


        public Builder processParameters(@NotNull Collection<String> processParameters) {
            this.processConfig.processParameters(processParameters);
            return this;
        }

        public Builder modifyProcessParameters(@NotNull Consumer<Collection<String>> modifier) {
            this.processConfig.modifyProcessParameters(modifier);
            return this;
        }


        public Builder environmentVariables(@NotNull Map<String, String> environmentVariables) {
            this.processConfig.environmentVariables(environmentVariables);
            return this;
        }

        public Builder modifyEnvironmentVariables(@NotNull Consumer<Map<String, String>> modifier) {
            this.processConfig.modifyEnvironmentVariables(modifier);
            return this;
        }


        public Builder runtime(@NotNull String runtime) {
            this.runtime = runtime;
            return this;
        }

        public Builder hostAddress(@Nullable String hostAddress) {
            this.hostAddress = hostAddress;
            return this;
        }

        public Builder javaCommand(@Nullable String javaCommand) {
            this.javaCommand = javaCommand;
            return this;
        }

        public Builder autoDeleteOnStop(boolean autoDeleteOnStop) {
            this.autoDeleteOnStop = autoDeleteOnStop;
            return this;
        }

        public Builder staticService(boolean staticService) {
            this.staticService = staticService;
            return this;
        }

        public Builder groups(Collection<String> collection) {
            this.groups = new HashSet<>(collection);
            return this;
        }

        public Builder modifyGroups(Consumer<Set<String>> modifier) {
            modifier.accept(groups);
            return this;
        }

        public Builder deletedFilesAfterStop(Collection<String> deletedFilesAfterStop) {
            this.deletedFilesAfterStop = new HashSet<>(deletedFilesAfterStop);
            return this;
        }

        public Builder modifyDeletedFilesAfterStop(Consumer<Set<String>> modifier) {
            modifier.accept(deletedFilesAfterStop);
            return this;
        }

        public Builder templates(Collection<ServiceTemplate> templates) {
            this.templates = new HashSet<>(templates);
            return this;
        }

        public Builder modifyTemplates(Consumer<Set<ServiceTemplate>> modifier) {
            modifier.accept(templates);
            return this;
        }

        public Builder deployments(Collection<ServiceDeployment> deployments) {
            this.deployments = new HashSet<>(deployments);
            return this;
        }

        public Builder modifyDeployments(Consumer<Set<ServiceDeployment>> modifier) {
            modifier.accept(deployments);
            return this;
        }

        public Builder inclusions(Collection<ServiceRemoteInclusion> inclusions) {
            this.includes = new HashSet<>(inclusions);
            return this;
        }

        public Builder modifyIncludes(Consumer<Set<ServiceRemoteInclusion>> modifier) {
            modifier.accept(includes);
            return this;
        }

        public Builder properties(JsonObject properties) {
            this.properties = properties;
            return this;
        }

        public ServiceConfiguration build() {
            return new ServiceConfiguration(
                    serviceId.build(),
                    processConfig.build(),
                    retryConfiguration,
                    port,
                    runtime,
                    hostAddress,
                    javaCommand,
                    autoDeleteOnStop,
                    staticService,
                    ImmutableSet.copyOf(groups),
                    ImmutableSet.copyOf(deletedFilesAfterStop),
                    ImmutableSet.copyOf(templates),
                    ImmutableSet.copyOf(deployments),
                    ImmutableSet.copyOf(includes),
                    properties
            );
        }
    }
    
}
