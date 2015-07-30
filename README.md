# mesos-kibana

[Kibana](https://www.elastic.co/products/kibana) framework for Mesos.

The kibana framework is developed with a simple approach: For each instance of elasticsearch know by the framework, one or more instances of kibana can be deployed to serve the users.

Each instance of kibana run as a docker image in the mesos cluster, thus docker containers must be supported by the mesos-slaves.

## Bits and pieces

FIXME

* demo repositor and jenkins job
* gradle and tasks
* The _simple JSON service_
* ...

## Roadmap

### Features
- [ ] **Slave attribute handling**. _Improve attribute handling to support for example locations, so the kibana instance can run on the same network or physical location as the elasticsearch server_
- [ ] **UI status interface**. _A framework webpage with status over running kibana instances and known elasticsearch servers_.
- [ ] **JSON management service interface**. _The simple JSON service to use to interact with the kibana framework could support some kind of more user friendly interface_.

### Testing
- [ ] **Extended unit testing**. _Primarily using the Mesos test framework and best-practices for testing frameworks. Currently tests are very simple_.
- [ ] **Extended functional testing.** _More use-case oriented testing, for example verifying framework behaves as expected on requests to the JSON service_.
- [ ] **System testing, ELK on Mesos.** _An overall demo and test of ELK on Mesos, using the two other frameworks for [LogStash](https://github.com/mesos/logstash) and [Elasticsearch](https://github.com/mesos/elasticsearch) to provide a complet ELK stack running on Mesos_.

### Miscellaneous
- [ ] DCOS CLI support.

### CoDe

_Different continuous delivery improvements, mostly related to gradle tasks_.

- [ ] Deploy over ssh.
- [ ] Publish artifacts.
    - [ ] gradle publish to artifact repository (mesos official?)
- [ ] Extend deploy to use published artifacts.
- [ ] Support deploy on Marathon frameworks.


## Pre-releases

During development the latest pre-release can be downloaded from our [mesos-kibana_release](http://code.praqma.net/ci/view/All/job/mesos-kibana_release) Jenkins  job artifact (or this [direct link](http://code.praqma.net/ci/view/All/job/mesos-kibana_release/lastSuccessfulBuild/artifact/build/libs/).

## Deployment from 0.1.0

### Deploy using gradle
Currently deploying is supported for docker based mesos clusters. The task takes it's settings from the `deploy.properties` file, the default values matches a docker cluster created using `docker-compose` in the [kibana-mesos-demo](https://github.com/Praqma/mesos-kibana-demo) repository.

By default the latest release is only stored on our CI server on the [mesos-kibana_release](http://code.praqma.net/ci/view/All/job/mesos-kibana_release) job selected (until a artifact management and repository system is available).

If the _deploy_ task is executed as
```
gradle deploy -PverifyBuild
```
the latest build from the verification job will be used.

### Deploy manually
Copy the kibana jar file onto your Mesos master and run it.
```
java -jar /path-to/kibana.jar
```

### Launch options
```
-m      -master          Mesos Master URI.
-zk     -zookeeper       Mesos Zookeeper URL.
-p      -port            The TCP port for the webservice. Defaults to 9001.
-es     -elasticsearch   URLs of ElasticSearch to start a Kibana for at startup.
```
Note: Either `-m` or `-zk` must be provided at startup.

FIXME - example on launch and deploy manually FIXME (based on our demo)


## Management
Tasks can be managed through the _simple JSON service_ we have created.

### Starting and killing tasks
To spin up new, or kill off excess, Kibana instances you can `POST` a `TaskRequest` JSON object to the webservice at `task/request`.
A `TaskRequest` has an `elasticSearchUrl`, which points to the ES you want to spin up Kibana instances for, and a `delta`, which is the amount of Kibana tasks you want to start.

`POST` the following to `task/request` to spin up one new instance of Kibana, pointing to the given ES:
```json
{
    "elasticSearchUrl": "http://db.fancytech.com:9200",
    "delta": 1
}
```

The procedure is the same for killing off excess tasks, except a negative delta should be given. Instances are killed off in LIFO fashion.

FIXME - examples (based on our demo - should we extend the dcemo job with it?)


## Changelog


### 0.1.0

The second release of the Kibana framework:

* With gradle as build tool and gradle deploy tasks
* The _simple JSON service_ added to interact with the framework
* FIXME



### 0.0.1

The first version of the Kibana Framework for Mesos with limited functionality.


#### Deployment
Copy the kibana-0.0.1.jar file onto your Mesos master and execute:
```
java -jar /path-to/kibana-0.0.1.jar <mesos-master-uri>|<zookeeper-url/mesos> <elasticsearch-url>...
```
# Sponsors
This project is sponsored by Cisco Cloud Services

# License
Apache License 2.0
