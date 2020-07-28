# YDH
ydh是springcloud alibaba的基础核心代码框架,包括统一认证服务、统一权限管理、统一组织管理、单点登录、统一API服务、服务发现及配置中心和后端网关。

### 目录结构
```
ydh 项目名
    api-gateway-server // 后端网关(gateway)
    nacos              // 服务与发现及配置中心
    oauth-server // 统一认证服务
    file-server  // 统一文件服务
    api-server   // 统一API服务
    admin-server // 服务监控
    form-server  // 表单服务
```
### 如何编译
mvn clean package -Dmaven.test.skip=true

### 如何运行
nohup java  -Xmx3g -Xms3g -Xmn1g -Xss256k -XX:ParallelGCThreads=8 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC  -jar ***-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev --spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848 --spring.cloud.nacos.config.server-addr=127.0.0.1:8848 >/dev/null 2>&1 &

### 注意事项
    1.请在.gitignore文件中加入项目忽略提交文件
    2.新建项目模块请勿修改公共pom依赖(SpringCloud Alibaba需要的相关依赖不用加)
    3.自己项目需要的依赖加在自己模块的POM文件中
    4.开发或者通过IDEA运行时,需要先执行 bin/mvn-install-dependency.sh，安装oracle的依赖

### 其他

