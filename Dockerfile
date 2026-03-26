# ======================================================================================
# STAGE 1: Build Stage
# ======================================================================================
FROM gradle:8.5.0-jdk21-jammy AS build

WORKDIR /app

# 依存関係定義を先にコピーしてキャッシュを有効化
COPY build.gradle settings.gradle /app/
COPY gradle/ /app/gradle/

# 依存関係をダウンロード
RUN gradle dependencies --no-daemon || true

# ソースコードをコピー（ここにローカルでビルドした output.css も含まれる）
COPY . /app/

# アプリケーションをビルド（実行可能な単一のJARファイルを生成）
RUN gradle bootJar -x test --no-daemon


# ======================================================================================
# STAGE 2: Runtime Stage
# ======================================================================================
FROM eclipse-temurin:21-jre-jammy

LABEL maintainer="SEA ISLAND Aquarium sringEvent Lottery System"

# Renderの動的ポート割り当てに対応（デフォルトは8080）
ENV PORT 8080
EXPOSE ${PORT}

WORKDIR /app

# ビルドステージから実行可能なJARファイルのみをコピー
COPY --from=build /app/build/libs/*-0.0.1-SNAPSHOT.jar app.jar

# Spring Bootアプリケーションを実行
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
