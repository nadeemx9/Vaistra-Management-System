#FROM openjdk:17
#EXPOSE 8080
#ADD target/cscv-github-action.jar cscv-github-action.jar
#ENTRYPOINT ["java","-jar","/cscv-github-action.jar"]


FROM openjdk:17
EXPOSE 8080
COPY target/*.jar Vaistra-Management-System.jar

ADD target/cscv-github-action.jar cscv-github-action.jar
ENTRYPOINT ["java","-jar","/cscv-github-action.jar"]