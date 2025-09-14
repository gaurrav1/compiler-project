# ---- Stage 1: Build the application ----
FROM maven:3.9.9-amazoncorretto-23-debian AS builder

# Set the working directory in container
WORKDIR /app

# Copy Maven POM and download dependencies early to leverage Docker cache
COPY build.gradle settings.gradle ./
COPY gradlew ./
COPY gradle ./gradle

RUN ./gradlew dependencies --no-daemon

# Copy the actual source code
COPY src ./src

# Package the application
RUN ./gradlew clean build -x test --no-daemon

# ---- Stage 2: Create a lean runtime image ----
FROM maven:3.9.9-amazoncorretto-23-debian

# Set environment variables
ENV JAVA_OPTS="-Xms256m -Xmx512m"
ENV BACKEND_PORT=8080
ENV APP_HOME=/app

# Set working directory
WORKDIR $APP_HOME

# Copy the jar from the builder stage
COPY --from=builder /app/build/libs/compiler-1.0.0.jar compiler-service.jar

# Expose the application port
EXPOSE $BACKEND_PORT

# Run the jar file
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar compiler-service.jar"]
