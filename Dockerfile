# build with: docker build -t todo-app . - run this command inside the folder where the Dockerfile can be found
# run with: docker run -dp 8080:8080 todo-app - the port should equal to the port Spring is using

# https://hub.docker.com/_/openjdk?tab=description&page=1&name=11
FROM openjdk:11

# copy JAR into image
COPY ./target/todo-app-0.0.1-SNAPSHOT.jar /todo-app/todo-app-0.0.1-SNAPSHOT.jar

WORKDIR /todo-app

# run application with this command line
# with ENTRYPOINT we are making sure that the command will be always executed: https://goinbigdata.com/docker-run-vs-cmd-vs-entrypoint/
ENTRYPOINT ["java", "-jar", "/todo-app/todo-app-0.0.1-SNAPSHOT.jar"]