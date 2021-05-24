# Utils Example

STILL NEED TO CLEAN DEPENDENCIES

#### Definition  

Simple Spring Boot Application to demonstrate what services the utils of this module provide. 


#### Diffable  

A tool to compare differences from 2 objects.  
A generic version is available, but users can implement their own way to compare two types of objects (See Step class)

Run & See swagger ui -> http://localhost:10005/swagger-ui/index.html  

#### Serializer  

A tool to serialize any type of Objects. Used in most other utils. 
ObjectList and SerializableList should be usable in POJO, but does not work in Convert field.  


#### Audit  

A tool to store every modifications done on an Object.
May be not the best practice... 

#### Mockito  

Simple example of Mock usage in test folder.


#### Audit  

Cipher method to encrypt values Sensitive but required to be translated-back to be used.  
(Example: User's secret word to send from us to another app for a process).  
Encrypt / Decrypt endpoints provided.  




