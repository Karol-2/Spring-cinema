# Spring cinema app
It's a Spring app that mocks the cinema ticket-buying system. Frontend is created with Thymeleaf. It works with SQL Postgres Database. There are separate pages for not logged-in users, logged users and administrators. Users can browse screening dates, check details, reserve and buy tickets for movies. An administrator has access to statistics from selected date ranges, and the management of movies and screenings.

## Postgres Docker container:
``
docker run --name my_postgres -e POSTGRES_USER=myuser -e POSTGRES_PASSWORD=mypassword -e POSTGRES_DB=mydatabase -p 5432:5432 -d postgres
``
## Swagger docs

Swagger docs: http://localhost:8080/swagger-ui/index.html#/
