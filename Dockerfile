FROM hseeberger/scala-sbt:11.0.14.1-oraclelinux8_1.6.2_2.13.8 AS builder
WORKDIR /app
COPY ./ ./
RUN sbt --batch -Dsbt.server.forcestart=true stage

FROM openjdk:11.0.14.1-jre-buster
WORKDIR /opt/btcbillionaire
COPY --from=builder /app/target/universal/stage ./
CMD ["./bin/btcbillionaire"]