# IBM MQ Connector SBC Example

### Definition  

Simple Spring Boot Application to
show how we consume the IBM MQ Connector module. 

Add the dependency to the module and then create a bean **MQClientConnector** in a @Configuration class.  

Once done, you only have to subscribe to new Messages and Exceptions to manage
the MQ life events.  

The sample service provided in this SBC module also show how to monitor the MQ connection.
