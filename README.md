# exporter-sample
A simple app is used to test Azure Monitor OpenTelemetry Exporter

### Build the agent jar locally:

`mvn compile`

`mvn package`

### Running the app:

`mvn exec:java -Dexec.mainClass="com.communication.quickstart.App" -Dexec.cleanupDaemonThreads=false`

### Install a local jar to maven local

```
mvn install:install-file -Dfile={package_name_placeholder}.jar -DgroupId={group_id} -DartifactId={artifact_id} -Dversion={version} -Dpackaging=jar
```
