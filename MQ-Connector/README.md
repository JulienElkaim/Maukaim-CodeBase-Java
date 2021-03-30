# IBM MQ Connector Module

### Definition

This module can connect to an IBM MQ, parse the messages using adapters, and dispatch the result
to anyone listening for it.

- Connection thread safe. 
- Dispatch to listeners not threaded. 
- No XML support for Swift Adapter (Nov. 2022).  

### Future enhancements  

- [X] Add Swift adapter.
- [ ] Add unit tests.
- [ ] Make the dispatching to listeners threaded.
- [ ] Add adapters for other very common uses of MQs.


### Dependencies  

This module is using **ibm.mq** in version 2.0.0, which is an implementation of JMS doc.  
The Swift adapter is using **prowidesoftware** in version SRU2019-8.0.2, not supporting Swift XML (Nov. 2022).  


[Depencies described in POM file.](pom.xml)

### How to use  

To use it in a Spring Boot application, add the module to the dependencies, and then create a MQClientConnector bean
thanks to the MQClientConnector provided. 

If your need is simple (Receive -> translate -> dispatch to listeners) you only have to provide it with your implementation of
GenericMessageFactory.  

If it requires more complex logic in the background, you can provide it with a custom implementation 
of **MQListenersManager**(i.e swift messages need to be processed as : Receive -> translate -> Dispatch if final or cache until the final message is received)
 
