FROM ubuntu:22.04

# 1. Установить зависимости
RUN apt-get update && \
    apt-get install -y wget unzip libaio1 lsb-release criu

# 2. Скачать и распаковать Zulu JDK с поддержкой CRaC
RUN wget https://cdn.azul.com/zulu-cryostat/zulu21.42.21-ca-crac-jdk21.0.7-linux_x64.tar.gz && \
    tar -xzf zulu21.42.21-ca-crac-jdk21.0.7-linux_x64.tar.gz && \
    mv zulu21.42.21-ca-crac-jdk21.0.7-linux_x64 /opt/crac-jdk

ENV JAVA_HOME=/opt/crac-jdk
ENV PATH=$JAVA_HOME/bin:$PATH

# 3. Копировать jar-файл внутрь контейнера
COPY target/spring-petclinic-3.4.0-SNAPSHOT.jar /app/spring-petclinic.jar
WORKDIR /app

CMD ["java", "-XX:CRaCCheckpointTo=cr", "-jar", "spring-petclinic.jar"]
