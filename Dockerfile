# 빌드 과정
FROM openjdk:17-jdk-slim AS builder

WORKDIR /app

# xargs가 필요하므로 필수 유틸 설치
RUN apt-get update && apt-get install -y findutils

COPY . .

RUN ./gradlew build -x test --no-daemon

# 이미지 생성 과정
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=dev"]