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
        GIT_CREDENTIAL_ID = 'git-credentials-id'
        // 邮件接收人列表，多个用逗号分隔
        EMAIL_RECIPIENTS = '1484096635@qq.com,2823546988@qq.com'
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    echo '正在拉取代码...'
                    checkout scm
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('04开发/frontend') {
                    script {
                        echo '正在构建前端...'
                        sh 'npm install'
                        sh 'npm run build'
                    }
                }
            }
        }

        stage('Build Backend') {
            steps {
                dir('04开发/backend') {
                    script {
                        echo '正在构建后端...'
                        // -DskipTests=false 确保运行测试以生成报告
                        sh 'mvn clean package -DskipTests=false'
                    }
                }
            }
        }

        stage('Test & Report') {
            steps {
                script {
                    echo '正在生成测试报告...'
                    // 假设 Surefire 插件生成的报告在 target/surefire-reports
                    // Allure 插件会读取这些结果
                    allure includeProperties: false, jdk: '', results: [[path: '04开发/backend/target/surefire-reports']]
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    echo '正在部署...'
                    // 这里是模拟部署，实际部署请替换为 scp, docker push, 或 kubectl apply 等命令
                    echo 'Deploying application to server...'
                    // sh './deploy_script.sh'
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
                // 需要有 push 权限的凭证
                withCredentials([usernamePassword(credentialsId: "${GIT_CREDENTIAL_ID}", passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                    sh "git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/Allan10w/learning-assistant.git ${tagName}"
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
