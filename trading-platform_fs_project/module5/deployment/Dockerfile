# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and the pom.xml file
COPY module1/backend/mvnw .
COPY module1/backend/.mvn .mvn
COPY module1/backend/pom.xml .

# Copy the project source
COPY module1/backend/src ./src

# Package the application
RUN ./mvnw package -DskipTests

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java","-jar","/app/target/trading_platform-0.0.1-SNAPSHOT.jar"]
