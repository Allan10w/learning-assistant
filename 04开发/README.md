# 🧠 Learning Assistant System （学习辅助系统）

一个基于 **Spring Boot + Vite + MySQL** 的学习辅助平台，支持课程管理、学生管理、测验发布、学习进度追踪等核心功能。  
本项目包含前后端分离的完整实现及部署示例。

---

## 📂 项目结构

```
learning-assistant/
│
├── frontend/           # 前端源码（Vite + Vue/React）
│   ├── src/            # 页面与组件
│   ├── public/
│   ├── vite.config.js
│   ├── package.json
│
├── backend/            # 后端源码（Spring Boot）
│   ├── src/
│   ├── pom.xml
│   └── application.yml
│
├── deploy/             # 部署与配置（Nginx、Caddy、脚本）
│   └── nginx.conf
│
├── release/            # ✅ 打包产物（前端 dist + 后端 jar）
│   ├── dist/
│   └── tlias-web-management-0.0.1-SNAPSHOT.jar
│
├── .gitignore
└── README.md
```

---

## ⚙️ 技术栈

| 模块 | 技术 |
|------|------|
| 前端 | Vite · Vue 3 / React · Axios · ECharts |
| 后端 | Spring Boot 3 · MyBatis · Lombok |
| 数据库 | MySQL 8 |
| 构建与打包 | Maven · npm · vite build |
| 部署 | Nginx / Caddy（前端） + Java Jar（后端） |

---

## 🚀 本地运行

### 🖥 后端启动
1. 进入后端目录  
   ```bash
   cd backend
   ```
2. 启动 MySQL 并导入数据库表（文件：`/backend/sql/init.sql`）
3. 修改 `application.yml` 数据库连接信息
4. 启动服务：
   ```bash
   mvn spring-boot:run
   # 或运行打包后的 jar
   java -jar ../release/tlias-web-management-0.0.1-SNAPSHOT.jar
   ```
5. 后端接口默认运行在  
   👉 http://localhost:8080

---

### 💻 前端启动
1. 进入前端目录
   ```bash
   cd frontend
   npm install
   npm run dev
   ```
2. 访问开发服务器  
   👉 http://localhost:5173

3. （生产环境）打包发布：
   ```bash
   npm run build
   ```
   构建产物会输出到 `/release/dist`

---

## 🌐 使用 Nginx 部署前端
```nginx
server {
    listen 8088;
    server_name localhost;

    root /Users/mac/Documents/learning-assistant/release/dist;
    index index.html;

    # 解决前端刷新 404 问题
    location / {
        try_files $uri /index.html;
    }

    # 反向代理后端 API
    location /api/ {
        proxy_pass http://localhost:8080/;
    }
}
```

---

## 🧰 打包与部署步骤总结

1️⃣ **前端打包**  
```bash
cd frontend
npm run build
mv dist ../release/
```

2️⃣ **后端打包**
```bash
cd backend
mvn clean package -DskipTests
mv target/*.jar ../release/
```

3️⃣ **启动后端**
```bash
java -jar ../release/tlias-web-management-0.0.1-SNAPSHOT.jar
```

4️⃣ **部署前端（Nginx 或 Caddy）**  
```bash
nginx -s reload
# 或
caddy file-server --root ./release/dist --listen :8088
```

---

## 🪄 未来改进计划
- [ ] 用户权限与角色管理  
- [ ] 在线测验模块（教师端创建 + 学生端答题）  
- [ ] 移动端界面适配  
- [ ] 使用 Docker 实现一键部署  

---

## 🧑‍💻 作者信息

**陈熙龙**  
📍 广州南方学院
📧 1484096635@qq.com  
🌐 [GitHub Profile](https://github.com/Allan10w)

---

## 📜 License

This project is licensed under the MIT License.  
Copyright (c) 2025 AllanChan
