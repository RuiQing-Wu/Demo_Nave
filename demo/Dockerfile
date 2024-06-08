# FROM maven:3.8.2-jdk-8 # for Java 8
FROM jelastic/maven:3.9.5-openjdk-21

# Set the working directory
WORKDIR /app

# Copy the pom.xml file to the working directory
COPY pom.xml .

# Copy the source code to the working directory
COPY src src

# Resolve the dependencies in the pom.xml file
RUN mvn dependency:resolve

# Build the project
RUN mvn clean package -DskipTests

# Run the application
#ENTRYPOINT ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
ENTRYPOINT mvn test && java -jar target/demo-0.0.1-SNAPSHOT.jar