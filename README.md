# transaction-management

## 项目概述
`transaction-management` 是一个用于管理交易记录的项目，它提供了创建、读取、更新和删除交易记录的基本功能。该项目使用 Java 开发，结合 Spring Boot 框架，前端采用 React 进行构建。

## 项目结构
项目主要包含以下几个模块：
- **前端部分**：基于 React 构建的用户界面。
- **后端部分**：使用 Java 开发的服务层和控制器层，负责处理业务逻辑和与前端的交互。
- **测试部分**：包含单元测试和压力测试。

### 前端部分
前端项目位于 `transaction-management-frontend` 目录下，它是一个基于 React 的单页应用程序。

#### 主要脚本命令
- `npm start`：在开发模式下运行应用程序，你可以通过访问 [http://localhost:3000](http://localhost:3000) 在浏览器中查看。当你对代码进行修改时，页面会自动重新加载，同时任何 lint 错误会显示在控制台中。
- `npm test`：启动测试运行器，进入交互式监视模式。更多关于运行测试的信息可以参考 [这里](https://facebook.github.io/create-react-app/docs/running-tests)。
- `npm run build`：将应用程序打包为生产环境版本，输出到 `build` 文件夹。此命令会正确地将 React 打包到生产模式，并对构建进行优化以获得最佳性能。构建后的文件会被压缩，文件名中包含哈希值，此时你的应用程序就可以部署了。更多部署信息请参考 [这里](https://facebook.github.io/create-react-app/docs/deployment)。

### 后端部分
后端项目使用 Spring Boot 框架，主要代码位于 `transaction-management` 目录下。

#### 主要功能
- **创建交易记录**：通过 `TransactionService` 类的 `createTransaction` 方法，为新的交易记录分配唯一的 ID 和时间戳，并将其存储在内存中的 `ConcurrentHashMap` 中。
- **获取交易记录列表**：通过 `TransactionService` 类的 `getAllTransactions` 方法，获取所有交易记录的列表。
- **更新交易记录**：通过 `TransactionService` 类的 `updateTransaction` 方法，根据交易记录的 ID 更新对应的交易记录信息，并更新时间戳。
- **删除交易记录**：通过 `TransactionService` 类的 `deleteTransaction` 方法，根据交易记录的 ID 从内存中删除对应的交易记录。

### 单元测试
项目中包含了多个单元测试类，用于测试服务层和控制器层的各个方法。例如，`TransactionServiceTest` 类包含了 `testCreateTransaction` 和 `testUpdateTransaction` 等测试方法，用于测试交易记录的CURD功能。这些单元测试使用 JUnit 框架编写，确保代码的正确性和可靠性。

### 压测
为了测试项目在高并发情况下的性能，我们还编写了一些压测代码。例如，在 `TransactionControllerStressTest` 类中，我们编写了 `createTransactionStressTest` 方法，使用多线程的方式模拟高并发场景，测试 `createTransaction` 方法的性能。

### 标准 JDK 之外的外部库
- pring-boot-starter-web：Spring Boot 的 Web 启动器，包含了构建 Web 应用所需的核心依赖，如 Tomcat 嵌入式服务器、Spring MVC 等。
- spring-boot-starter-test：Spring Boot 的测试启动器，包含了 JUnit、Mockito 等测试框架的依赖，方便进行单元测试和集成测试。
- mockito-core：Mockito 的核心库，用于创建和管理模拟对象，方便对依赖组件进行隔离测试。