# FIX Engine Module

### Definition

This module provide an example to connect and discuss with a counterpart in FIX protocol.  
It is designed to restrain itself to 1 active session.  

An example to produce a NewOrderSingle is available.

An example to consume an ExecutionReport is available. 


### Future enhancements  

- [ ] Magic Strings / Int to remove. 

### Dependencies  

This module is using **org.quickfixj** in version 2.0.0, QuickFix is an OpenSource java library for FIX protocol.  

[Depencies described in POM file.](pom.xml)

### How to use  

To use it in a Spring Boot application, add the module to the dependencies, and then create a FixEngineConnector bean. 

In this version, you need to provide all the configurations by a config file, see example on the consumer module.
 
