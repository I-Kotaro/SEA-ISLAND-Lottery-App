# ======================================================================================
# STAGE 1: Frontend Build Stage
# ======================================================================================
FROM node:20-slim AS frontend-build

WORKDIR /app

# npm依存関係をインストール
COPY package*.json ./
RUN npm ci

# ソースコードをコピーして CSS をビルド
COPY . .
RUN npm run build:css

# ======================================================================================
# STAGE 2: Build Stage
# ======================================================================================
FROM gradle:8.5.0-jdk21-jammy AS build

WORKDIR /app

# 依存関係定義を先にコピーしてキャッシュを有効化
COPY build.gradle settings.gradle /app/
COPY gradle/ /app/gradle/

# 依存関係をダウンロード
RUN gradle dependencies --no-daemon || true

# ソースコードをコピー
COPY . /app/

# frontend-build ステージで生成された output.css をコピー
COPY --from=frontend-build /app/src/main/resources/static/css/output.css /app/src/main/resources/static/css/output.css

# アプリケーションをビルド（実行可能な単一のJARファイルを生成）
RUN gradle bootJar -x test --no-daemon


# ======================================================================================
# STAGE 3: Runtime Stage
# ======================================================================================
FROM eclipse-temurin:21-jre-jammy

LABEL maintainer="SEA ISLAND Aquarium sringEvent Lottery System"

# Renderの動的ポート割り当てに対応（デフォルトは8080）
ENV PORT 8080
EXPOSE ${PORT}

WORKDIR /app

# ビルドステージから実行可能なJARファイルのみをコピー
# バージョンに関わらず app.jar としてコピーすることで、起動コマンドを固定
COPY --from=build /app/build/libs/*-0.0.1-SNAPSHOT.jar app.jar

# Spring Bootアプリケーションを実行
# server.port を環境変数 PORT で上書きするように設定
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
