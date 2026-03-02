# --- Giai đoạn 1: Dùng Maven để Build code (Nặng) ---
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy file cấu hình và tải thư viện trước (giúp cache nhanh hơn cho lần sau)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy toàn bộ code vào và đóng gói thành file .jar
COPY src ./src
RUN mvn clean package -DskipTests

# --- Giai đoạn 2: Chỉ lấy JRE để chạy App (Nhẹ) ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy file .jar đã build từ giai đoạn 1 sang
COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]