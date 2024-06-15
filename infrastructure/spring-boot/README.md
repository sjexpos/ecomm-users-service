## Run test from IDE

All integration test need Postgres, Opensearch, Redis and [Localstack](https://github.com/localstack/localstack) running locally. All servers are started by maven before that those tests are run.
When a test has to be run into an IDE the servers are not running. 
But it can be fixed if the following command is run in this folder:

```bash
> mvn pre-integration-test
```

This command will run all plugin which were defined on this maven phase. 
In this case, Postgres is started up, all Flyway scripts are run against that postgres and Opensearch is started up too.

When the servers must be shutdowned the command `mvn post-integration-test` won't work. So, it is need to use docker stop.

```bash
> docker stop -t 1 <postgres container id> <opensearch container id>
```

The container ids can be gotten using:

```bash
> docker ps
```