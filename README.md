# Быстрый старт CRaC + Docker + Colima (macOS)

## 1. Установка и запуск Colima

```bash
brew install colima
colima start --cpu 4 --memory 6
```

**Проверка контекста Docker:**
```bash
docker context ls
# colima должен быть со звездочкой *
```

---

## 2. Dockerfile для CRaC + Spring Boot

```Dockerfile
FROM ghcr.io/azul-research/crac-java21:latest

RUN apt-get update && apt-get install -y criu

WORKDIR /app
COPY spring-petclinic.jar .

# ENTRYPOINT пустой — приложение запускается вручную
```

**Собирание образ:**
```bash
docker build -t petclinic-crac .
```

---

## 3. Подготовка папки для чекпоинта

```bash
mkdir cr
chmod 777 cr
```

---

## 4. Запуск приложения для чекпоинта

```bash
docker run --privileged -it --rm --network=host \
  -v $PWD/cr:/app/cr \
  petclinic-crac \
  /opt/crac-jdk/bin/java -XX:CRaCCheckpointTo=/app/cr -jar /app/spring-petclinic.jar
```
- Ждите появления в логах `Started PetClinicApplication...`.
- Не выключайте контейнер!

---

## 5. Создание чекпоинта во втором терминале

**Как узнать ID контейнера:**
```bash
docker ps
# CONTAINER ID для petclinic-crac
```
**Зайти внутрь:**
```bash
docker exec -it <CONTAINER_ID> bash
```
**Найти PID Java:**
```bash
ps aux | grep java
# Например, PID = 42
```
**Создать чекпоинт:**
```bash
jcmd <PID> JDK.checkpoint
# Command executed successfully
```
**Проверит наличие файлов:**
```bash
ls /app/cr
# или локально
ls cr
```

---

## 6. Запуск восстановления из чекпоинта

- Остановить первый контейнер (ctrl+C или docker stop).

```bash
docker run --privileged -it --rm --network=host \
  -v $PWD/cr:/app/cr \
  petclinic-crac \
  /opt/crac-jdk/bin/java -XX:CRaCRestoreFrom=/app/cr
```

---

## 7. Сравнение время запуска

Смотреть строку в логах:
```
Started PetClinicApplication in 0.XXX seconds
```
### Сравнение для spring-petclinic:

```bash
# Старт с чекпоинтом
Spring-managed lifecycle restart completed (restored JVM running for 7625 ms)
```
```bash
# Старт без чекпоинта
Started PetClinicApplication in 49.926 seconds (process running for 54.147)
```

---

## Быстрый чек-лист команд

```bash
brew install colima
colima start --cpu 4 --memory 6
docker context ls
docker build -t petclinic-crac .
mkdir cr && chmod 777 cr

# Старт c чекпоинтом:
docker run --privileged -it --rm --network=host -v $PWD/cr:/app/cr petclinic-crac /opt/crac-jdk/bin/java -XX:CRaCCheckpointTo=/app/cr -jar /app/spring-petclinic.jar

# Второй терминал:
docker ps
docker exec -it <CONTAINER_ID> bash
ps aux | grep java
jcmd <PID> JDK.checkpoint

# Восстановление:
docker run --privileged -it --rm --network=host -v $PWD/cr:/app/cr petclinic-crac /opt/crac-jdk/bin/java -XX:CRaCRestoreFrom=/app/cr
```
