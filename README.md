# Small JTA benchmark suite


## Goals

This small webapp is intended to give you a rough feeling about JTA vs resource-local JPA transactions 


## Running

Just deploy the WAR and browse the page. It contains a download link to an Apache JMeter (http://jmeter.apache.org) benchmark script.

### Running locally

$> mvn clean install tomee:run

Browse http://localhost:8080/jtabench/index.html

### Benchmarking

run command:
```
mvn gatling:execute -P perf
```

For the torture mode use:
```
 mvn gatling:execute -Dtorture -P perf
```

it will run the simulation for a longer period (~7min instead of 50 sec) with more users (300 instead of 100)

Note that each user will perform 100 database operations on each request