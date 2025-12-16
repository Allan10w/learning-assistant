-- ================================================
-- 01_create_database.sql
-- 创建数据库 tlias 及可选的演示账号
-- ================================================

CREATE DATABASE IF NOT EXISTS `tlias`
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

-- （可选）创建一个普通用户用于演示
CREATE USER IF NOT EXISTS 'tlias_user'@'%' IDENTIFIED BY 'Tlias@123';
GRANT ALL PRIVILEGES ON `tlias`.* TO 'tlias_user'@'%';
FLUSH PRIVILEGES;