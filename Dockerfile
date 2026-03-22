# ======================================================================================
# STAGE 1: Build Stage
# Java 21 / Gradle環境でアプリケーションをビルド
# ======================================================================================
# Spring Boot 3.3 は Gradle 8.5+ を推奨
FROM gradle:8.5.0-jdk21-jammy AS build

# コンテナ内の作業ディレクトリを設定
WORKDIR /app

# 依存関係定義ファイルのみを先にコピーし、Dockerのレイヤーキャッシュを最大限に活用
COPY build.gradle settings.gradle /app/
COPY gradle/ /app/gradle/

# 依存関係をダウンロード
# これにより、ソースコードの変更だけでは再ダウンロードが発生せず、ビルドが高速化
RUN gradle dependencies --build-cache || true

# アプリケーションの全ソースコードをコピー
COPY . /app/

# Gradle Wrapperでアプリケーションをビルド
# Spring Bootプラグインの 'bootJar' タスクは実行可能なJARを生成
RUN gradle bootJar -x test


# ======================================================================================
# STAGE 2: Runtime Stage
# Java 21 の軽量なランタイムイメージで実行環境を構築
# ======================================================================================
FROM openjdk:21-slim

# 作者情報（任意）
LABEL maintainer="SEA ISLAND Aquarium sringEvent Lottery System"

# アプリケーションがリッスンするポートを8080として公開
# Renderはこのポートを自動検出し、外部トラフィックをルーティング
EXPOSE 8080

# コンテナ内の作業ディレクトリを設定
WORKDIR /app

# ビルドステージから生成された実行可能なJARファイルのみをコピー
# build.gradleの 'version' に基づくファイル名が生成
COPY --from=build /app/build/libs/*.jar app.jar

# コンテナ起動時にSpring Bootアプリケーションを実行
# JVMのメモリ割り当てを調整し、終了時のシグナルを適切に処理する`exec`形式を推奨
ENTRYPOINT ["java", "-jar", "app.jar"]
