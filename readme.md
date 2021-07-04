Build application container image:

```
docker build -t seo-url-app .
```

Start application container:

```

[comment]: <> (docker build -t seo-url-app .)
docker run -dp 8080:8080 seo-url-app .

```