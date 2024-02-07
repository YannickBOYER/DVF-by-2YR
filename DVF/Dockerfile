FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# Remove Windows line endings
RUN sed -i 's/\r$//' mvnw 
RUN ./mvnw dependency:resolve

COPY src ./src
COPY doc ./doc

CMD ["./mvnw", "spring-boot:run"]