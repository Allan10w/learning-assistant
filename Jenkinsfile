//Jenkinsfile是Jenkins流水线的配置文件，它定义了流水线的执行流程
pipeline {
    agent any

    tools {
        // 请确保在 Jenkins "Global Tool Configuration" 中配置了名为 "maven-3.9" 和 "node-20" 的工具
        maven 'maven-3.9'
        nodejs 'node-20'
    }

    environment {
        // 环境变量配置
        // 请在 Jenkins 凭证管理中配置 git-credentials-id
        GIT_CREDENTIAL_ID = 'gitee-credentials-id'
        GITHUB_CREDENTIAL_ID = 'git-credentials-id' // 请在 Jenkins 中配置 GitHub 凭据
        // 邮件接收人列表，多个用逗号分隔
        EMAIL_RECIPIENTS = '1484096635@qq.com,2823546988@qq.com'
    }

    //声明式流水线的核心，定义了流水线的执行流程
    stages {
        stage('Checkout') {
            steps {
                script {
                    echo '正在拉取代码...'
                    checkout scm
                    // 拉取当前流水线代码来源
                    // 如果你在jenkins任务里配的是GitHub仓库，他就自动拉取那个仓库的最新代码
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('04 dev/frontend') { // 切换工作目录到前端源码文件夹
                    script {
                        echo '正在构建前端...'
                        sh 'npm install'  //安装依赖
                        sh 'npm run build' //构建
                    }
                }
            }
        }

        stage('Build Backend') {
            steps {
                dir('04 dev/backend') {
                    script {
                        echo '正在构建后端...'
                        // -DskipTests=false 确保运行测试以生成报告
                        // 执行Maven打包命令
                        sh 'mvn clean package -DskipTests=false'

                    }
                }
            }
        }

        stage('Test & Report') {
            steps {
                script {
                    echo '正在生成测试报告...'
                    // 使用Allure插件收集测试结果
                    // 在'target/surefire-reports' 目录下找 Maven 运行测试生成的 xml 文件
                    allure includeProperties: false, jdk: '', results: [[path: '04 dev/backend/target/surefire-reports']]
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                dir('04 dev/backend') {
                    script {
                        echo '正在进行代码质量与安全扫描...'
                        // 需要在Jenkins中配置SonarQube服务器 (Manage Jenkins -> System -> SonarQube servers)
                        // 这里的 'SonarQube' 是你在Jenkins设置里给服务器起的名字
                        withSonarQubeEnv('SonarQube') {
                            // 执行Maven Sonar插件，jacoco报告会自动被识别
                            sh 'mvn sonar:sonar'
                        }
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                dir('04 dev/deploy') {
                    script {
                        echo '正在部署...'
                        // 尝试停止旧容器（如果有）并启动新容器
                        // 注意：需要确保 Jenkins 节点安装了 docker-compose 并有权限执行
                        try {
                            //这一步调用了宿主机的 Docker 命令。Docker 会读取刚才前后端构建生成的产物
                            // 1.停止并移除旧容器
                            sh 'docker-compose down || true'
                            // 2.构建新镜像并后台启动
                            sh 'docker-compose up -d --build'
                            echo '部署完成！应用正在后台运行。'
                            //（Frontend 的 dist 和 Backend 的 jar），把它们分别打包进新的镜像里，然后启动。
                        } catch (Exception e) {
                            echo "部署失败: ${e.getMessage()}"
                            throw e
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            script {
                echo '构建成功，正在打标签...'
                // 需要配置 git user email 和 name 才能打 tag，或者在 Jenkins 全局配置中设置
                sh "git config --global user.email 'jenkins@example.com'"
                sh "git config --global user.name 'Jenkins CI'"
                
                def tagName = "release-${BUILD_NUMBER}-${new Date().format('yyyyMMddHHmmss')}"
                sh "git tag -a ${tagName} -m 'Jenkins build ${BUILD_NUMBER}'"
                
                // 1. 推送代码和标签到 Gitee
                echo '正在推送代码和标签到 Gitee...'
                withCredentials([usernamePassword(credentialsId: "${GIT_CREDENTIAL_ID}", passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                    // 推送当前分支
                    sh "git push https://${GIT_USERNAME}:${GIT_PASSWORD}@gitee.com/allanchanice/learning-assistant.git HEAD:master"
                    // 推送标签
                    sh "git push https://${GIT_USERNAME}:${GIT_PASSWORD}@gitee.com/allanchanice/learning-assistant.git ${tagName}"
                }

                // 2. 推送代码和标签到 GitHub (实现双平台同步)
                echo '正在推送代码和标签到 GitHub...'
                withCredentials([usernamePassword(credentialsId: "${GITHUB_CREDENTIAL_ID}", passwordVariable: 'GH_PASSWORD', usernameVariable: 'GH_USERNAME')]) {
                    // 推送当前分支
                    sh "git push https://${GH_USERNAME}:${GH_PASSWORD}@github.com/Allan10w/learning-assistant.git HEAD:master"
                    // 推送标签
                    sh "git push https://${GH_USERNAME}:${GH_PASSWORD}@github.com/Allan10w/learning-assistant.git ${tagName}"
                }
                
                emailext (
                    subject: "构建成功: ${env.JOB_NAME} - #${env.BUILD_NUMBER}",
                    body: """<p>构建成功！</p>
                             <p>项目: ${env.JOB_NAME}</p>
                             <p>构建编号: #${env.BUILD_NUMBER}</p>
                             <p>查看详情: <a href='${env.BUILD_URL}'>${env.BUILD_URL}</a></p>
                             <p>Allure 报告: <a href='${env.BUILD_URL}allure'>${env.BUILD_URL}allure</a></p>""",
                    to: "${EMAIL_RECIPIENTS}"
                )
            }
        }
        failure {
            script {
                echo '构建失败，发送通知...'
                emailext (
                    subject: "构建失败: ${env.JOB_NAME} - #${env.BUILD_NUMBER}",
                    body: """<p style='color:red;'>构建失败！</p>
                             <p>项目: ${env.JOB_NAME}</p>
                             <p>构建编号: #${env.BUILD_NUMBER}</p>
                             <p>查看日志: <a href='${env.BUILD_URL}console'>${env.BUILD_URL}console</a></p>""",
                    to: "${EMAIL_RECIPIENTS}"
                )
            }
        }
    }
}
