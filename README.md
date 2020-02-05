# Snapweb

This is a project that provides RESTful services to capture the screenshots of given website URLs and store them
to download them later at any point in the future.

## Prerequisites

* Java8
* [Docker](https://www.docker.com/)
* [jq](https://stedolan.github.io/jq/)


## Build

```bash
git clone https://github.com/erdalzeynep/snapweb.git
cd snapweb
./gradlew clean build -x test
docker build . -t snapweb 
```

## Run
```bash
docker run -it -p 9443:9443 snapweb:latest
```

## Capture Screenshots

```bash
# ./capture-screenshot.sh '["<website-1>","<website-2>","<website-3>"]'
./capture-screenshot.sh '["http://www.google.com","http://facebook.com","http://twitter.com"]'
```

Running the command above will issue the request and if it all goes well it will return the request-id
as to be used to fetch the detail of the request later and it will also download the screenshots to the
current folder. Here is an example to show how it would look like;

```
Request id is 180
All images have been downloaded in current folder
```

The images that is downloaded will show up with a name like `request_<request-id>_image_<counter>.png`. (`request_180_image_2.png`)

## Fetch Request Detail

```bash
# ./request-detail.sh <request-id>
./request-detail.sh 180
```

## Run Unit Tests

```bash
./gradlew test
```

## How to Scale

Since this is a dockerized service, it can be deployed as many docker containers as it is liked behind a load balancer to
handle more requests.


## Technologies That is Used

* [Spring Boot](https://spring.io/projects/spring-boot)

* [Jersey Web Framework With Spring](https://eclipse-ee4j.github.io/jersey/)

* [Hibernate with Embedded Database H2](https://hibernate.org/orm/documentation/5.4/)

* [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

* [JUnit4 for Unit-tests](https://junit.org/junit4/)

* [Gradle](https://gradle.org/)

* [Docker](https://www.docker.com/)