# --- Estágio 1: Compilação (Build) ---
# Usamos uma imagem com Maven e Java 21 oficial
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Define a pasta de trabalho dentro do container
WORKDIR /app

# Copia o arquivo de dependências e baixa (cache layer)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia todo o código fonte do projeto para dentro do container
COPY src ./src

# Compila o projeto e gera o .jar (Pula os testes para ser mais rápido agora)
RUN mvn clean package -DskipTests

# --- Estágio 2: Execução (Runtime) ---
# Usamos uma imagem leve apenas com o Java (sem Maven) para rodar
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia apenas o .jar gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta 8080
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]