# Continuous Security Testing in Microservices and Cloud Native Applications
This project was demostrates integration of security testing in cloud native CI/CD pipelines using `Test Driven Security` techniques (https://www.usenix.org/conference/enigma2017/conference-program/presentation/vehent). This approach is a key feature in SecDevOps, and aims at moving security tests leftwards in development pipelines.
One major motivation for this project is the `discovery and registry design pattern` (https://microservices.io/patterns/service-registry.html) of cloud applications design.
Security testing can be flexibly modified to meet with `time-to-market` requirements e.g. policies can be configured for more specific time-based tests, more detailed tests can be conducted as required. Main take-aways:

   - Security testing of microservices in CI/CD .
   - Advanced security testing in the staging area of the microservices in parallel with deployed microservices to enable more inense and comprehensive testing.

# Deployment instructions
1. Clone repository
2. Replace your Discovery and Registry Service (`Eureka Server`) with the this project.
3. Run as a Spring application, normally will run on port 8761.
4. Deploy an OWASP ZAP server
   -  Install based on your OS from https://github.com/zaproxy/zaproxy/wiki/Downloads
   -  Navigate to the root of the instaaltion directory and execute the command `\.zap -addoninstall openapi`
   -  The command starts OWASP ZAP, and installs the `openapi addon` used for parsing openapi documents
   -  OWASP ZAP wilkl listen to connections on port `8080`, otherwise supply your port in the `security.properties` file of the project.
5. Security gateway will test all microservice prior to deployment. 
6. Note that microservices are to be OpenAPI compliant i.e. should have an OpenAPI specified documents, this documents are necessary for the security tests.
 
For more details about this work, take a look at our paper : `Integrating Continuous Security Assessments in Microservices and Cloud Native Applications` (https://www.researchgate.net/publication/320596749_Integrating_Continuous_Security_Assessments_in_Microservices_and_Cloud_Native_Applications).

Please, cite if useful for your research:

`@inproceedings{torkura2017integrating,`

  `title={Integrating Continuous Security Assessments in Microservices and Cloud Native Applications},`
  
  `author={Torkura, Kennedy A and Sukmana, Muhammad IH and Meinel, Christoph},`
  
  `booktitle={Proceedings of the10th International Conference on Utility and Cloud Computing},`
  
  `pages={171--180},`
  
  `year={2017},`
  
  `organization={ACM}`
`}`
