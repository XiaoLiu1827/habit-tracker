# Java 17 + Maven 環境をベースに使用
FROM maven:3.9.6-eclipse-temurin-17

# 作業ディレクトリを /app に設定
WORKDIR /app

# プロジェクト全体をイメージにコピー
COPY . .

# JARビルド（ローカルキャッシュがあれば活用）
RUN ./mvnw clean package -DskipTests

# アプリを実行（JARファイル名に注意）
CMD ["sh", "-c", "java -jar target/*.jar"]
