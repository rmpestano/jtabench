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

it will run the simulations for a longer period (~7min instead of 50 sec) with more users simultaneously

**Note** that each user will perform 100 database operations on each request

### Results
Resulting reports can be accessed on links below:

* [JTA simulation report](http://rmpestano.github.io/jtabench/jtasimulation/index.html)
* [NON JTA simulation report](http://rmpestano.github.io/jtabench/nonjtasimulation/index.html)
* [JTA and NON JTA simulation report](http://rmpestano.github.io/jtabench/jtaandnonjtasimulation/index.html)

the above simulations were ran without torture flag on a core I5(3470) CPU and 8GB RAM using JDK 8(u40).

For now the results are the same for both modes, maybe the simulation needs to be reworked or maybe JTA perform same as non JTA. 

Here is a report generated for torture mode:

[JTA and NON JTA **Torture** simulation report](http://rmpestano.github.io/jtabench/jtaandnonjtasimulation_torture/index.html)

Here we can see some difference in response time where non jta has a slight better performance (as expected)