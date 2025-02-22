# OpenJDK 17을 기반으로 사용
FROM openjdk:17-jdk

# Gradle 빌드 후 생성된 JAR 파일을 컨테이너 내부로 복사
COPY build/libs/*SNAPSHOT.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
