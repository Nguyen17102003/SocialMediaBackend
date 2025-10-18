# Base image với JDK 21
FROM openjdk:17-jdk-slim

# Cài đặt Maven
RUN apt-get update && apt-get install -y maven

# Xác định JAVA_HOME (đúng với image openjdk:21-jdk-slim)
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH="$JAVA_HOME/bin:$PATH"

WORKDIR /app

# Copy toàn bộ source code
COPY . .

# Đảm bảo mvnw có quyền thực thi
RUN chmod +x mvnw || true

# Dùng Maven có sẵn trong hệ thống để build
RUN mvn clean package -DskipTests

# Mở cổng (tùy app)
EXPOSE 8080

# Chạy ứng dụng
CMD ["java", "-jar", "target/*.jar"]