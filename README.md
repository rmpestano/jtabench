# Small JTA benchmark suite


## Goals

This small webapp is intended to give you a rough feeling about JTA vs resource-local JPA transactions 


## Running

Just deploy the WAR and browse the page. It contains a download link to an Apache JMeter (http://jmeter.apache.org) benchmark script.

### Running locally

$> mvn clean install tomee:run

Browse http://localhost:8080/jtabench/index.html

### Benchmarking

Open the jtabench.jmx file in Apache JMeter and activate one of the two sites.
Between the rounds invoke
http://localhost:8080/jtabench/customer/reset
to clean all customers from the DB again
