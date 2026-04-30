# Imagen base: Maven + JDK 17
FROM maven:3.8.4-openjdk-17-slim

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

# Instalar librerías necesarias para Swing/AWT
RUN apt-get update && apt-get install -y \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libfreetype6 \
    fontconfig \
    && rm -rf /var/lib/apt/lists/*

RUN mvn clean install -DskipTests

CMD ["mvn", "exec:java"]
