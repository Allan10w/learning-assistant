# Jenkins 配置指南

为了确保 `Jenkinsfile` 能顺利运行，特别是自动构建和部署功能，请按照以下步骤配置您的 Jenkins 服务器环境。

## 0. 服务器环境准备 (关键步骤)

在运行 Jenkins 的服务器上，必须安装并配置好以下环境。

### 1. 安装 Docker 和 Docker Compose

#### macOS 用户
推荐使用 **Docker Desktop for Mac**。
1.  **下载安装**: 访问 Docker 官网下载安装，或者使用 Homebrew:
    ```bash
    brew install --cask docker
    ```
2.  **启动**: 安装完成后，请务必启动 Docker Desktop 应用，并等待左下角状态变为绿色的 "Running"。

#### Linux (Ubuntu/Debian) 用户
```bash
sudo apt-get update
sudo apt-get install docker.io docker-compose -y
```

### 2. 配置 Jenkins 权限与环境

#### macOS 用户
如果您是通过 Homebrew (`brew install jenkins-lts`) 安装的 Jenkins：
1.  **以及当前用户运行**: Homebrew 默认会让 Jenkins 以当前登录用户身份运行，这就意味着它通常可以直接访问 Docker Desktop，**无需**像 Linux 那样配置用户组。
2.  **关键：环境变量问题**: Jenkins 在 macOS 上经常遇到找不到 `docker` 命令的问题。
    *   请前往 Jenkins 面板 -> **Manage Jenkins** -> **System** -> **Global properties**。
    *   勾选 **Environment variables**。
    *   添加变量 `PATH`，值为 `/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:$PATH` (或者你在终端输入 `echo $PATH` 得到的完整路径)。这能确保 Jenkins 找到 `docker` 和 `docker-compose` 命令。

#### Linux 用户
Jenkins 默认运行在 `jenkins` 用户下，必须配置权限：
```bash
# 1. 将 jenkins 用户加入 docker 组
sudo usermod -aG docker jenkins
# 2. 重启 Jenkins 服务
sudo systemctl restart jenkins
```

### 3. 确认端口与数据库
*   **端口未占用**: 确保本地 **80** 和 **8080** 端口空闲。
*   **数据库**: Docker Desktop for Mac 支持使用 `host.docker.internal` 访问宿主机上的 MySQL 服务。请确保你 Mac 本地的 MySQL 是启动状态。

---


## 1. 安装必要插件

请前往 **Manage Jenkins** -> **Plugins** -> **Available plugins**，搜索并安装以下插件：

-   **Pipeline**: 核心流水线插件（通常默认已安装）。
-   **Maven Integration**: 用于构建 Maven 项目。
-   **NodeJS**: 用于构建前端项目。
-   **Allure Jenkins Plugin**: 用于生成漂亮的测试报告。
-   **Email Extension Plugin**: 用于发送自定义邮件通知。
-   **Git Plugin**: 用于拉取代码和打标签。

## 2. 全局工具配置 (Global Tool Configuration)

请前往 **Manage Jenkins** -> **Tools** 进行配置：

### Maven
-   **Name**: `maven-3.9` (必须与 Jenkinsfile 中的名称一致)
-   **Install automatically**: 勾选，选择版本 3.9.x。

### NodeJS
-   **Name**: `node-20` (必须与 Jenkinsfile 中的名称一致)
-   **Install automatically**: 勾选，选择版本 20.x。

### Allure Commandline
-   **Name**: `allure` (或者保持默认，但在 Jenkinsfile 中可能需要调整)
-   **Install automatically**: 勾选，选择最新版本。

## 3. 凭证配置 (Credentials)

请前往 **Manage Jenkins** -> **Credentials** -> **System** -> **Global credentials**：

### Git 凭证
-   **Kind**: Username with password
-   **Scope**: Global
-   **ID**: `git-credentials-id` (必须与 Jenkinsfile 中的 `GIT_CREDENTIAL_ID` 一致)
-   **Username**: 您的 Git 用户名
-   **Password**: 您的 Git 密码或 Access Token

## 4. 邮件服务器配置

请前往 **Manage Jenkins** -> **System**，找到 **Extended E-mail Notification** 部分：

-   **SMTP Server**: 配置您的邮件服务器地址 (如 `smtp.exmail.qq.com`)
-   **Default User E-mail Suffix**: 默认邮箱后缀
-   **Advanced** -> **Use SMTP Authentication**: 勾选并配置发送方邮箱账号密码。

## 5. 创建流水线任务

1.  点击 **New Item**。
2.  输入任务名称，选择 **Pipeline**，点击 **OK**。
3.  在 **Pipeline** 部分：
    -   **Definition**: 选择 `Pipeline script from SCM`。
    -   **SCM**: 选择 `Git`。
    -   **Repository URL**: 输入您的项目 Git 地址。
    -   **Credentials**: 选择刚才创建的 `git-credentials-id`。
    -   **Script Path**: 保持默认 `Jenkinsfile`。
4.  点击 **Save**。

## 6. 运行流水线

点击 **Build Now** 开始构建。构建完成后：
-   查看 **Console Output** 确认构建过程。
-   点击 **Allure Report** 查看测试报告。
-   检查您的邮箱是否收到通知。

## 7. 验证部署结果

构建成功后，您的应用已经自动部署在当前服务器（本机）上。

### 1. 访问应用
*   **前端页面**: 打开浏览器访问 [http://localhost](http://localhost)
*   **后端接口**: [http://localhost:8080](http://localhost:8080)

### 2. 查看运行状态
在终端运行以下命令查看容器状态：
```bash
docker ps
```
您应该能看到名为 `tlias-frontend` and `tlias-backend` 的两个容器正在运行。

### 3. 查看构建产物 (物理文件)
如果您需要查看编译生成的 JAR 包或静态文件，它们位于 Jenkins 的工作目录中：
*   **前端构建产物**: `~/.jenkins/workspace/<任务名称>/04开发/frontend/dist/`
*   **后端 JAR 包**: `~/.jenkins/workspace/<任务名称>/04开发/backend/target/`

## 8. 日常开发与发布流程

至此，您的 CI/CD 流水线已经完全跑通。团队的日常开发流程如下：

1.  **开发代码**: 团队成员在本地开发功能，并确保本地运行无误。
2.  **提交代码**: 将代码提交并推送到 GitHub 远程仓库 (`main` 分支)。
    ```bash
    git push origin main
    ```
3.  **触发构建**:
    *   **手动 (当前)**: 登录 Jenkins，点击任务的 **Build Now** 按钮。
    *   **自动 (进阶)**: 如果配置了 GitHub Webhook，推送代码后 Jenkins 会自动开始构建。
4.  **自动部署**: Jenkins 会自动完成拉取、编译、测试、打包、构建镜像、重启服务全过程。
5.  **验证**: 几分钟后，刷新浏览器即可看到最新功能。


