# Процедура запуска автотестов
1. Открыть Docker Desktop. В нем должны быть контейнеры, созданные из образов "node", "mySQL", "PostgreSQL" соответственно.
2. Запустить IDE с проектом.
3. В терминале IDE выполнить команду "docker-compose up". Дождаться загрузки контейнеров.
4. В новом окне терминала выполнить команду "java -jar artifacts/aqa-shop.jar "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app". Дождаться загрузки SUT.
5. В тертьем окне терминала выполнить команду "./gradlew test".
   
[Перечень сценариев](https://github.com/vrnkv/DiplomaProject/blob/main/docs/plan.md)

[Отчет о проведенном тестировании](https://github.com/vrnkv/DiplomaProject/blob/main/docs/Report.md#отчет-о-проведенном-тестировании)

[Отчёт о проведённой автоматизации](https://github.com/vrnkv/DiplomaProject/blob/main/docs/Summary.md)
