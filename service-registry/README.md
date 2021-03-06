# gcloud docs
https://cloud.google.com/sdk/gcloud/reference/app/versions/describe

# If the project is new, first run the below command to create a "master" app
gcloud app create

# View current active project
gcloud config list

# Set project
gcloud config set project stelinno-dev
gcloud config set project stelinno-prod

# To do a test run
mvn spring-boot:run

# To deploy to GCP
mvn appengine:deploy

# To start the Datastore emulator
gcloud beta emulators datastore start

# To use the Datastore emulator
gcloud beta emulators datastore env-init

# Using Catatumbo
http://jmethods.com/catatumbo/quick-start.html

# Call upsert remotely
curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"name":"Sports Results", "domain":"Sports", "subDomain":"Statistics", "endpoint":"http://sports-service.azure.com"}' https://service-registry-dot-stelinno-dev.appspot.com/upsert

# GQL Syntax
https://cloud.google.com/datastore/docs/reference/gql_reference

# Indexing for use with Search API
https://cloud.google.com/appengine/docs/standard/java/search/?hl=en_US&_ga=1.236706563.345585972.1497603790



# ======== OBSOLETE ============ #

# Spring Boot based Hello World app

This sample shows how to run a [Spring Boot][spring-boot] application on [Google
Cloud Platform][cloud-java]. It uses the [Google App Engine flexible
environment][App Engine-flexible].

[App Engine-flexible]: https://cloud.google.com/appengine/docs/flexible/
[cloud-java]: https://cloud.google.com/java/
[spring-boot]: http://projects.spring.io/spring-boot/


## Before you begin

This sample assumes you have [Java 8][java8] installed.

[java8]: http://www.oracle.com/technetwork/java/javase/downloads/

### Download Maven

These samples use the [Apache Maven][maven] build system. Before getting
started, be sure to [download][maven-download] and [install][maven-install] it.
When you use Maven as described here, it will automatically download the needed
client libraries.

[maven]: https://maven.apache.org
[maven-download]: https://maven.apache.org/download.cgi
[maven-install]: https://maven.apache.org/install.html

### Create a Project in the Google Cloud Platform Console

If you haven't already created a project, create one now. Projects enable you to
manage all Google Cloud Platform resources for your app, including deployment,
access control, billing, and services.

1. Open the [Cloud Platform Console][cloud-console].
1. In the drop-down menu at the top, select **Create a project**.
1. Give your project a name.
1. Make a note of the project ID, which might be different from the project
   name. The project ID is used in commands and in configurations.

[cloud-console]: https://console.cloud.google.com/

### Enable billing for your project.

If you haven't already enabled billing for your project, [enable
billing][enable-billing] now.  Enabling billing allows the application to
consume billable resources such as running instances and storing data.

[enable-billing]: https://console.cloud.google.com/project/_/settings

### Install the Google Cloud SDK.

If you haven't already installed the Google Cloud SDK, [install and initialize
the Google Cloud SDK][cloud-sdk] now. The SDK contains tools and libraries that
enable you to create and manage resources on Google Cloud Platform.

[cloud-sdk]: https://cloud.google.com/sdk/

### Install the Google App Engine SDK for Java


```
gcloud components update app-engine-java
gcloud components update
```

### Configure the `app.yaml` descriptor

The [`app.yaml`][app-yaml] descriptor is used to describe URL
dispatch and resource requirements.  This example sets
[`manual_scaling`][manual-scaling] to 1 to minimize possible costs.
These settings should be revisited for production use.

[app-yaml]: https://cloud.google.com/appengine/docs/flexible/java/configuring-your-app-with-app-yaml
[manual-scaling]: https://cloud.google.com/appengine/docs/flexible/java/configuring-your-app-with-app-yaml#manual-scaling

## Run the application locally

1. Set the correct Cloud SDK project via `gcloud config set project
   YOUR_PROJECT` to the ID of your application.
1. Run `mvn spring-boot:run`
1. Visit http://localhost:8080


## Deploy to App Engine flexible environment

1. `mvn appengine:deploy`
1. Visit `http://YOUR_PROJECT.appspot.com`.

Note that deployment to the App Engine flexible environment requires the new
[`com.google.cloud.tools:appengine-maven-plugin` plugin][new-maven-plugin].

[new-maven-plugin]: https://cloud.google.com/appengine/docs/flexible/java/using-maven

Java is a registered trademark of Oracle Corporation and/or its affiliates.

