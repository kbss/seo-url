## Run application

#### Prerequisites

Make sure that gradlew executable:

#### Mac:

``chmod +x gradlew``

### 1. Run Application without build

```shell
gradlew bootRun
```

## Docker image

#### Prerequisites

Make sure that docker installed

* Build application container image:

```
docker build -t seo-url-app .
```

* Start application container:

```
docker run -dp 8080:8080 seo-url-app .

```