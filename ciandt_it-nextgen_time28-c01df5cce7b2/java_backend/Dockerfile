# Usar uma imagem base do Maven para construir os microserviços
FROM maven:3-openjdk-17-slim AS build

# Definir o diretório de trabalho
WORKDIR /app

# Copiar o arquivo pom.xml do monorepo e o diretório de código-fonte do microserviço escolhido
COPY . .

# Definir o argumento de construção para escolher qual microserviço compilar
ARG SERVICE_NAME

# Construir o microserviço escolhido
RUN mvn clean install -pl microservices/${SERVICE_NAME} -am -DskipTests

# Usar uma imagem base do OpenJDK para rodar o microserviço
FROM openjdk:17-jdk

# Definir o argumento de construção para escolher qual microserviço compilar
ARG SERVICE_NAME
ENV SERVICE_NAME=${SERVICE_NAME}

# Definir o diretório de trabalho
WORKDIR /app

# Copiar o JAR construído para o diretório de trabalho
COPY --from=build /app/microservices/${SERVICE_NAME}/target/${SERVICE_NAME}-*.jar ${SERVICE_NAME}.jar

# Comando para rodar o microserviço escolhido
CMD ["sh", "-c", "java -jar ${SERVICE_NAME}.jar"]