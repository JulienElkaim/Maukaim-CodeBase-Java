# Swagger Module

### Definition

This module creates a Swagger (Specifications 2.0) to any Spring Boot application importing it.

### Future enhancements  

- [X] Auto create swagger once module added to main project.
- [X] Auto create a UI once module added to main project. (default)
- [ ] Customize UI for nice looking. (default is ugly, **see apibank** )
- [ ] Add customizing capability via application.yml files. (**see apibank**)
- [ ] Add Oauth2 capability. (**see apibank**)
- [ ] Add authentication capability. (**see springfox**)
- [ ] Provide a way to override UI template. (Templating? Config for default changes as logo? **see apibank, springfox**)

### Dependencies  

This module is using **springfox** in version 3.0.0. 
It requires the importing module to provide Spring Boot Web and Jupiter, no version specified.

[Depencies described in POM file.](pom.xml)

### How to use  

In version 0.0.1-SNAPSHOT, you only need to add this module to the dependencies, and the Swagger will be created. 
This module is applying configurations to your Spring Boot application, 
using a [spring.factories](src/main/resources/META-INF/spring.factories) file to declare its
configuration classes to your application before the application start.  


See more details on [Spring.io's page relative to auto configuration](https://docs.spring.io/spring-boot/docs/2.0.0.M3/reference/html/boot-features-developing-auto-configuration.html).  

In future version, you will have to specify some configs in application.yml of your application for the swagger to activate.  

