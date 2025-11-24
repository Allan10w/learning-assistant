# Jenkins 配置指南

为了确保 `Jenkinsfile` 能顺利运行，请按照以下步骤配置您的 Jenkins 环境。

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
