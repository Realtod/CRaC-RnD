FROM ubuntu:22.04

RUN apt-get update && \
    apt-get install -y wget unzip libaio1 lsb-release criu postgresql-client && \
    rm -rf /var/lib/apt/lists/*

RUN wget https://cdn.azul.com/zulu/bin/zulu21.42.21-ca-crac-jdk21.0.7-linux_x64.tar.gz && \
    tar -xzf zulu21.42.21-ca-crac-jdk21.0.7-linux_x64.tar.gz && \
    mv zulu21.42.21-ca-crac-jdk21.0.7-linux_x64 /opt/crac-jdk && \
    rm zulu21.42.21-ca-crac-jdk21.0.7-linux_x64.tar.gz

COPY target/spring-petclinic-3.4.0-SNAPSHOT.jar /app/spring-petclinic.jar

WORKDIR /app

ENV JAVA_HOME=/opt/crac-jdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

COPY postgresql-*.jar /app/

CMD ["java", "-XX:CRaCCheckpointTo=cr", "-cp", "spring-petclinic.jar:postgresql-*.jar", "org.springframework.samples.petclinic.PetClinicApplication"]
