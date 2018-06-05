# ise-svr

[TOC]

## 1. 部署

- 本机

```sh
cd ~/workspaces/02/ise/ise-svr
./deploy/deploy-???.sh
```

## 2. 配置中心

- 在git服务器中加入 ise-svr-prod.yml 文件的内容

## 3. 创建并启动容器

启动时注意映射端口号

```sh
docker run -d --net=host --name ise-svr-a -v /usr/local/ise-svr/files:/usr/local/myservice/static/files -v /usr/local/ise-svr/a:/usr/local/myservice --restart=always nnzbz/spring-boot-app
```