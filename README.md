# news-feed

Клиент-серверное приложение на SpringBoot с асинхронным взаимодействием через RabbitMQ (вместо REST) по типу микросервисов.

## Запуск
Запустить rabbitMQ в докер контейнере: 
`# docker compose up`

Собрать каждое приложение:
`mvn clean package`

Запустить сервер:
`java -jar target/news-feed-0.0.1-SNAPSHOT.jar `

Запустить клиент:
`java -jar target/news-client-0.0.1-SNAPSHOT.jar `

## Настройка БД 
Создание БД PostgreSQL локально вне контейнра:
1) sudo -u postgres psql
2) CREATE DATABASE test_db;
3) CREATE USER newsadmin WITH PASSWORD 'newspwd';
4) \c test_db
5) GRANT ALL PRIVILEGES ON DATABASE test_db TO newsadmin;