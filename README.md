## Micronaut 3.0.3 Documentation

- [User Guide](https://docs.micronaut.io/3.0.3/guide/index.html)
- [API Reference](https://docs.micronaut.io/3.0.3/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/3.0.3/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)

---

## Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)

## Run the application

    mvn package


## HTTP Request to test

### Register a callback

    POST http://localhost:8080/subscription/register
    Content-Type: application/json
    
    {
        "uri": "http://localhost:8081/api/test",
        "frequency": 2
    }

### Unregister a callback

    POST http://localhost:8080/subscription/unregister
    Content-Type: application/json
    
    {
        "uri": "http://localhost:8081/api/test"
    }

### Update an existing callback

    PUT http://localhost:8080/subscription/update
    Content-Type: application/json
    
    {
        "url": "http://localhost:8081/api/recipes/test",
        "frequency": 7
    }