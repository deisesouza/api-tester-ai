# api-tester-ai

<h4 align="center">
  API de Engenharia de Prompt que utiliza IA Generativa (Azure OpenAI) para validar outros microserviços.
</h4>

## Tecnologias

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [Docker Compose](https://docs.docker.com/compose/)
- [Azure OpenAI](https://azure.microsoft.com/pt-br/products/ai-foundry/agent-service/)

## Como Executar

- Rodar o Docker Compose:
```
$ docker-compose up
```
- Construir o projeto:
```
$ ./gradlew clean build
```
- Executar a aplicação:
```
$ "java", "-jar", "app.jar"

```