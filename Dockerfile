FROM openjdk:17
EXPOSE 8080
ADD target/cscv-github-action.jar cscv-github-action.jar
ENTRYPOINT ["java","-jar","/cscv-github-action.jar"]