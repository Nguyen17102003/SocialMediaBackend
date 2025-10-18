# Sử dụng image có sẵn JDK 21 và Maven cài sẵn
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Thiết lập thư mục làm việc
WORKDIR /app

# Copy toàn bộ project
COPY . .

# Build project bằng Maven
RUN mvn clean package -DskipTests

# Giai đoạn chạy (chỉ copy file jar để giảm dung lượng)
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy file jar từ giai đoạn build
COPY --from=build /app/target/*.jar app.jar

# Mở cổng ứng dụng
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]