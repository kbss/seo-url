### Requirements

* java 11

## How to:

### Run application

#### Prerequisites:

Make sure that gradlew executable:

#### Command for Mac/nix:

``chmod +x gradlew``

### 1. Run Application without build

Command:
```shell
gradlew bootRun
```

### 2. Run application in docker container

#### Prerequisites:

Make sure that docker installed

#### Build application container image:

```
docker build -t seo-url-app .
```

#### Start application container:

```
docker run -dp 8080:8080 seo-url-app .

```