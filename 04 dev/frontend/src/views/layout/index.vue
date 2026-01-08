<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus';
import { useRouter } from 'vue-router'

let router = useRouter()

const loginName = ref('')
//定义钩子函数, 获取登录用户名
onMounted(() => {
  //获取登录用户名
  let loginUser = JSON.parse(localStorage.getItem('loginUser'))
  if (loginUser) {
    loginName.value = loginUser.name
  }
})

const logout = () => {
  //弹出确认框, 如果确认, 则退出登录, 跳转到登录页面
  ElMessageBox.confirm('确认退出登录吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {//确认, 则清空登录信息
    ElMessage.success('退出登录成功')
    localStorage.removeItem('loginUser')
    router.push('/login')//跳转到登录页面
  })
}
</script>

<template>
  <div class="common-layout">
    <el-container>
      <!-- Header 区域 -->
      <el-header class="header">
        <div class="header-content">
          <span class="title">CampusOS</span>
          <span class="right_tool">
            <el-button link class="header-action">
              <el-icon><EditPen /></el-icon> 修改密码
            </el-button>
            <el-divider direction="vertical" />
            <el-button link class="header-action" @click="logout">
              <el-icon><SwitchButton /></el-icon> 退出登录 【{{ loginName }}】
            </el-button>
          </span>
        </div>
      </el-header>
      
      <el-container>
        <!-- 左侧菜单 -->
        <el-aside width="240px" class="aside">
          <div class="menu-spacer"></div>
          <el-menu router :default-active="$route.path">
            <!-- 首页菜单 -->
            <el-menu-item index="/index">
              <el-icon><Promotion /></el-icon>
              <span>首页</span>
            </el-menu-item>
            
            <!-- 班级管理菜单 -->
            <el-sub-menu index="/manage">
              <template #title>
                <el-icon><Menu /></el-icon>
                <span>班级学员管理</span>
              </template>
              <el-menu-item index="/clazz">
                <el-icon><HomeFilled /></el-icon>班级管理
              </el-menu-item>
              <el-menu-item index="/stu">
                <el-icon><UserFilled /></el-icon>学员管理
              </el-menu-item>
            </el-sub-menu>
            
            <!-- 系统信息管理 -->
            <el-sub-menu index="/system">
              <template #title>
                <el-icon><Tools /></el-icon>
                <span>系统信息管理</span>
              </template>
              <el-menu-item index="/dept">
                <el-icon><HelpFilled /></el-icon>部门管理
              </el-menu-item>
              <el-menu-item index="/emp">
                <el-icon><Avatar /></el-icon>员工管理
              </el-menu-item>
            </el-sub-menu>

            <!-- 数据统计管理 -->
            <el-sub-menu index="/report">
              <template #title>
                <el-icon><Histogram /></el-icon>
                <span>数据统计管理</span>
              </template>
              <el-menu-item index="/report/emp">
                <el-icon><InfoFilled /></el-icon>员工信息统计
              </el-menu-item>
              <el-menu-item index="/report/stu">
                <el-icon><Share /></el-icon>学员信息统计
              </el-menu-item>
              <el-menu-item index="/log">
                <el-icon><Document /></el-icon>日志信息统计
              </el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-aside>
        
        <!-- 主展示区域 -->
        <el-main>
          <div class="main-content-wrapper">
             <router-view></router-view>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.header {
  background-color: rgba(255, 255, 255, 0.85); /* Proper glass effect base */
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  box-shadow: none; /* Remove heavy shadow */
  height: 60px;
  position: sticky;
  top: 0;
  z-index: 100;
  padding: 0 24px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  max-width: 1600px;
  margin: 0 auto;
}

.title {
  color: var(--el-text-color-primary);
  font-size: 18px;
  font-family: -apple-system, BlinkMacSystemFont, "SF Pro Display", sans-serif;
  font-weight: 600;
  letter-spacing: -0.02em;
}

.right_tool {
  display: flex;
  align-items: center;
}

.header-action {
  font-size: 13px;
  color: var(--el-text-color-secondary);
}

.header-action:hover {
  color: var(--el-color-primary);
}

.aside {
  width: 240px;
  border-right: 1px solid rgba(0, 0, 0, 0.05);
  background-color: #F8F8F9; /* Sidebar gray */
  height: calc(100vh - 60px); /* Fill height minus header */
  overflow-y: auto;
}

.menu-spacer {
  height: 24px;
}

:deep(.el-main) {
  background-color: var(--el-bg-color-page);
  padding: 32px; /* Add breathing room */
}

.main-content-wrapper {
  max-width: 1400px; /* Prevent content from stretching too wide on huge screens */
  margin: 0 auto;
}

/* Fix Element Plus menu transition glitch */
.el-menu {
  background-color: transparent;
}
</style>
