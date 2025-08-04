# news-feed

Создание БД PostgreSQL локально вне контейнра:
1) sudo -u postgres psql
2) CREATE DATABASE test_db;
3) CREATE USER newsadmin WITH PASSWORD 'newspwd';
4) \c test_db
5) GRANT ALL PRIVILEGES ON DATABASE test_db TO newsadmin;

Для проверки отправки сообщений в очередь сервером, можно зайти в очередь:
1) Откройте веб-интерфейс RabbitMQ (обычно http://localhost:15672)
2) Перейдите во вкладку "Queues"
3) Найдите очередь news.request.queue
4) Нажмите "Publish message"
5) В поле "Payload" введите любой correlationId (например, "test123"), именно в кавычках, потому что это json
6) Нажмите "Publish message"

Для вычитки сообщения из очереди:
1) Создайте временную очередь в интерфейсе RabbitMQ:
Имя: например, temp.test.queue
Добавьте binding: exchange=news.exchange, routing_key=news.responce.test123
2) Отправьте запрос как описано выше
3) Проверьте сообщения во временной очереди temp.test.queue