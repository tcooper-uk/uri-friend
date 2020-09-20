FROM openjdk:11

# setting our working dir
WORKDIR /usr/src/app

# move the contents
COPY target/libs/ libs/
COPY target/uri-friend-web-1.0-SNAPSHOT.jar .

# make sure we expose the web app port
EXPOSE 80

# run jar for prod env 
ENTRYPOINT ["java", "-jar", "uri-friend-web-1.0-SNAPSHOT.jar", "prod"]