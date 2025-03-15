FROM gradle:8.12-jdk21-alpine AS build
WORKDIR /app
COPY . .
RUN ./gradlew build -x test --no-daemon

FROM tomcat:10.1.24
COPY --from=build /app/build/libs/weather-app.war $CATALINA_HOME/webapps/weather-app.war
CMD ["catalina.sh", "run"]


#to use it, need to uncomment /build in .dockerignore
#FROM tomcat:10.1.24-jdk21
#WORKDIR /app
#COPY build/libs/weather-app.war $CATALINA_HOME/webapps/weather-app.war
#CMD ["catalina.sh", "run"]