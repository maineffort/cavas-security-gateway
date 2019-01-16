# Eureka Server with in-built security testing features
The project was demostrates integration of security testing in cloud native CI/CD pipelines. This is a key feature in SecDevOps which security testing is moved leftwards `test driven security`.
Motivation is drived for the `discovery and registry design pattern`, security testing can be flexibly modified to meet with `time-to-market` requirements.

# Deployment instructions
1. Clone repository
2. Replace your Discovery and Registry Service (`Eureka Server`) with the Security Gateway.
3. Run as a Spring application, normally will run on port 8761.
4. Deploy an OWASP ZAP server e.g. `docker pull owasp/zap2docker-stable` and supply the port in the `security.properties` file.
5. Security gateway will test all microservice prior to deployment. 
6. Note that microservice have to be OpenAPI compliant i.e. should generate an OpenAPI document, which is used for security testing.
 
For more details about this work see the paper : `Integrating Continuous Security Assessments in Microservices and Cloud Native Applications`

Please, cite if useful for your research:

@inproceedings{torkura2017integrating,
  title={Integrating Continuous Security Assessments in Microservices and Cloud Native Applications},
  author={Torkura, Kennedy A and Sukmana, Muhammad IH and Meinel, Christoph},
  booktitle={Proceedings of the10th International Conference on Utility and Cloud Computing},
  pages={171--180},
  year={2017},
  organization={ACM}
}