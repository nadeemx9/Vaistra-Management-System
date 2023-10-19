FROM openjdk:17
EXPOSE 8080
ADD target/Vaistra-Management-System.jar Vaistra-Management-System.jar
ENTRYPOINT ["java","-jar","/Vaistra-Management-System.jar"]