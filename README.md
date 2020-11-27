# Weather Umbrella

Demo microservice using kotlin 1.4.x, Gradle and Spring Boot 2.4.0.

Information from code challenge is omitted.

## Get the service up and running
In the root folder execute:
./gradlew bootRun

## How to query the API
To query the api simply send HTTP requests on port 8080 from localhost.
The API is the same as defined on the challenge. Although for a production ready 
its recommended to version the API. Also it's recommended to write a OAS3.0 specification. 
Example request:
GET http://localhost:8080/current?location=Berlin

## Production ready microservice

## How to deploy the application in an AWS

In order to deploy a simple application with low initial costs we can use AWS Lambda.
After the business evolves more sophisticated solutions can be proposed, for example using Kubernetes.

Terraform can be used to write infrastructure as a code on AWS and 
define the resources our application needs.

The following architecture is proposed:
* Upload our deployment package to Amazon S3 (see https://docs.aws.amazon.com/lambda/latest/dg/java-package.html)
* Create an AWS Lambda function using AWS Serverless Java container: https://github.com/awslabs/aws-serverless-java-container
 and our existing Spring Boot Application 
* Create an Api Gateway to receive the requests from the internet and transform into events
that are sent to Lamba. It will work as a simple proxy to our Lambda function

After also is recommended automating your build via a CI system.