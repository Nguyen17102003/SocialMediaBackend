# Sử dụng Java 21
FROM openjdk:21-jdk-slim

# Đặt JAVA_HOME
ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
ENV PATH="$JAVA_HOME/bin:$PATH"

WORKDIR /app
COPY . .

# Build ứng dụng
RUN ./mvnw clean package -DskipTests

# Chạy app
CMD ["java", "-jar", "target/*.jar"]