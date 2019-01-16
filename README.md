# Continuous Security Testing in Microservices and Cloud Native Applications
This project was demostrates integration of security testing in cloud native CI/CD pipelines using [Test Driven Security techniques] (https://www.usenix.org/conference/enigma2017/conference-program/presentation/vehent). This approach is a key feature in SecDevOps, and aims at moving security tests leftwards in development pipelines.
One major motivation for this project is the [discovery and registry design pattern](https://microservices.io/patterns/service-registry.html) of cloud applications design.
Security testing can be flexibly modified to meet with `time-to-market` requirements e.g. policies can be configured for more specific time-based tests, more detailed tests can be conducted as required. 

# Deployment instructions
1. Clone repository
2. Replace your Discovery and Registry Service (`Eureka Server`) with the this project.
3. Run as a Spring application, normally will run on port 8761.
4. Deploy an OWASP ZAP server e.g. `docker pull owasp/zap2docker-stable` and supply the port in the `security.properties` file.
5. Security gateway will test all microservice prior to deployment. 
6. Note that microservices are to be OpenAPI compliant i.e. should have an OpenAPI speciifed documents, this documents are necessary for the security tests.
 
For more details about this work, take a look at our paper : [Integrating Continuous Security Assessments in Microservices and Cloud Native Applications] (https://www.researchgate.net/publication/320596749_Integrating_Continuous_Security_Assessments_in_Microservices_and_Cloud_Native_Applications).

Please, cite if useful for your research:

`@inproceedings{torkura2017integrating,`

  `title={Integrating Continuous Security Assessments in Microservices and Cloud Native Applications},`
  
  `author={Torkura, Kennedy A and Sukmana, Muhammad IH and Meinel, Christoph},`
  
  `booktitle={Proceedings of the10th International Conference on Utility and Cloud Computing},`
  
  `pages={171--180},`
  
  `year={2017},`
  
  `organization={ACM}`
}