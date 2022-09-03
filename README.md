# exporter-sample
A simple app is used to test Azure Monitor OpenTelemetry Exporter

### Build the agent jar locally:

`mvn compile`

`mvn package`

### Running the app:

```
export CONNECTION_STRING={your_connection_string}
```

`mvn exec:java -Dexec.mainClass="com.communication.quickstart.App" -Dexec.cleanupDaemonThreads=false`

and then use [Fiddler](https://www.telerik.com/fiddler) to examine payload.

### [Proxy setup](https://docs.microsoft.com/en-us/azure/developer/java/sdk/proxying)

### Install a local jar to maven local

```
mvn install:install-file -Dfile={package_name_placeholder}.jar -DgroupId={group_id} -DartifactId={artifact_id} -Dversion={version} -Dpackaging=jar
```

[Learn more](https://docs.microsoft.com/en-us/azure/communication-services/quickstarts/telemetry-application-insights?pivots=programming-language-java)
