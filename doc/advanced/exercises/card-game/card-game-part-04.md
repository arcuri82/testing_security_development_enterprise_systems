
In the REST API of `user-collections`, update each endpoint
to use wrapped responses, and customized error handling (as seen in class).

To use wrapped responses and custom error handling based on them, 
you need to rely on the classes from the modules:
* `org.tsdes.advanced.rest:rest-dto`
* `org.tsdes.advanced.rest:rest-exception`

However, do **NOT** import those libraries (as they are SNAPSHOTs
that are not published on Maven Central, and so the build
will fail on a different machine), but rather
just copy&paste their code in your project, in your package
hierarchy.  
Note: in the solutions we import those libraries, just for simplicity.
Furthermore, we use:
`@SpringBootApplication(scanBasePackages = ["org.tsdes.advanced"])`
to tell Spring to scan for those beans.
This is unnecessary if they are under your package hierarchy.

Make also sure to have the following in your `application.yml`:
```
spring:
  mvc:
    throwExceptionIfNoHandlerFound: true
  resources:
    add-mappings: false
```