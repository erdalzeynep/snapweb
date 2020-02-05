FROM java:8
ADD build/libs/snapweb-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
