# Weather Umbrella

Demo microservice using kotlin 1.4.x, Gradle and Spring Boot 2.4.0.

Information from code challenge specification is omitted.

## Get the service up and running
Requirements:
* Docker Desktop/daemon running on machine (to run integration tests - testcontainers)
* Have a MongoDB instance running

Steps:
* Set spring.data.mongodb.uri property
* In the root folder execute:
./gradlew bootRun

TODO: create Docker compose for the app. Should include app and MongoDB Docker containers

## How to query the API
To query the api simply send HTTP requests on port 8080 from localhost.
The API is the same as defined on the challenge. Although for a production ready 
its recommended to version the API. Also it's recommended to write a OAS3.0 specification. 
Example request:
GET http://localhost:8080/current?location=Berlin

## Production ready microservice
A production ready microservice should include mechanisms to monitor the application, troubleshoot eventual
bugs and be easily maintained with relatively low costs. Also, it should have a flexible design so that 
it can evolve with business needs. 

This microservice should have more integration tests and unit tests. Although current coverage 
is 86% on lines of code, more corner cases should be tested.
Error handling can be improved, and a few TODOs that were left on the code should be done.
It may be necessary further configuration for application logs to integrate with Amazon Cloudwatch.
The application performance should be measured using monitoring solutions and metrics.

## How to deploy the application in an AWS

In order to deploy a simple application with low initial costs we can use AWS Lambda.
After the business evolves more sophisticated solutions can be proposed, for example using Kubernetes
to deploy microservices.

Terraform can be used to write infrastructure as a code on AWS and 
define the resources our application needs.

The following architecture is proposed:
* Create a MongoDB on AWS (needed because state can't be persisted on AWS Lambda)
* Set spring.data.mongodb.uri and Weather Api properties
* For the Weather API, API KEY should be configured
* Build and Upload our deployment package to Amazon S3 (see https://docs.aws.amazon.com/lambda/latest/dg/java-package.html)
* Create an AWS Lambda function using AWS Serverless Java container: https://github.com/awslabs/aws-serverless-java-container
 and our existing Spring Boot Application 
* Create an Api Gateway to receive the requests from the internet and transform into events
that are sent to Lamba. It will work as a simple proxy to our Lambda function

After also is recommended automating your build via a CI system.