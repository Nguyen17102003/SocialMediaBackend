
FROM openjdk:17-jdk-slim

ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk
ENV PATH="$JAVA_HOME/bin:$PATH"

WORKDIR /app
COPY . .

RUN ./mvnw clean package -DskipTests

CMD ["java", "-jar", "target/*.jar"]