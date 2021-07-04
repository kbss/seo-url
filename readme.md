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

Make sure that Docker is installed.

#### Build application container image:

```shell
docker build -t seo-url-app .
```

#### Start application container:

### 3. Run tests

```shell
gradlew test 
```

##### Reports:

* Test results:
  ```
  build/reports/tests/test/index.html
  ```

* Test coverage report:
  ```
  build/reports/jacoco/test/html/jacoco-sessions.html
  ```