<script setup>
import { onMounted, ref, watch } from 'vue'
import { queryPageApi , addApi, queryInfoApi, updateApi, deleteApi} from '@/api/clazz'
import { queryAllApi as queryAllEmpApi } from '@/api/emp'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'

//学科列表数据
const subjects = ref([{ name: 'Java', value: 1 },{ name: '前端', value: 2 },{ name: '大数据', value: 3 },{ name: 'Python', value: 4 },{ name: 'Go', value: 5 },{ name: '嵌入式', value: 6 }])
//搜索表单对象
let searchClazz = ref({begin:'', end:'', date:[], name:''})
//列表展示数据
let tableData = ref([])

//钩子函数 - 页面加载时触发
onMounted(() => {
  queryPage()
  queryAllEmp()
})

//所有的员工数据
let emps = ref([])
//加载所有的员工数据
const queryAllEmp = async () => {
  let result = await queryAllEmpApi()
  if(result.code) {
    emps.value = result.data
  }
}

//分页组件
const pagination = ref({currentPage: 1, pageSize: 10, total: 0})
//每页展示记录数发生变化时触发
const handleSizeChange = (pageSize) => {
  pagination.value.pageSize = pageSize
  queryPage()
}
//当前页码发生变化时触发
const handleCurrentChange = (page) => {
  pagination.value.currentPage = page
  queryPage()
}

//分页条件查询
const queryPage = async () => {
  const result = await queryPageApi(
      searchClazz.value.begin,
      searchClazz.value.end,
      searchClazz.value.name,
      pagination.value.currentPage,
      pagination.value.pageSize
  );

  if(result.code) {
    tableData.value = result.data.rows
    pagination.value.total = result.data.total
  }
}

//清空搜索栏
const clear = () => {
  searchClazz.value = {begin:'', end:'', date:[], name:''}
  queryPage()
}

//监听searchEmp的date属性
watch(() => searchClazz.value.date, (newVal, oldVal) => {
  if(newVal && newVal.length > 0) {
    searchClazz.value.begin = newVal[0]
    searchClazz.value.end = newVal[1]
  }else {
    searchClazz.value.begin = ''
    searchClazz.value.end = ''
  }
})



//----------- 新增 / 修改 ---------------------------
let dialogFormVisible = ref(false) //控制新增/修改的对话框的显示与隐藏
let labelWidth = ref(80) //form表单label的宽度
let formTitle = ref('') //表单的标题
//员工对象-表单数据绑定
let clazz = ref({
  id: '',
  name: '',
  room: '',
  beginDate: '',
  endDate: '',
  subject: '',
  masterId: ''
})

//清空表单
const clearClazz = () => {
  clazz.value = {
    id: '',
    name: '',
    room: '',
    beginDate: '',
    endDate: '',
    subject: '',
    masterId: ''
  }
}

//新增班级
const addClazz = () => {
  dialogFormVisible.value = true
  formTitle.value = '新增班级'
  clearClazz()
}

//修改班级
const updateClazz = async (id) => {
  clearClazz()
  dialogFormVisible.value = true
  formTitle.value = '修改班级'
  let result = await queryInfoApi(id)
  if(result.code){
    clazz.value = result.data
  }
}

//表单校验规则
const clazzFormRef = ref()
const rules = ref({
  name: [
    { required: true, message: '班级名称为必填项', trigger: 'blur' },
    { min: 4, max: 30, message: '用户名长度为4-30个字', trigger: 'blur' }
  ],
  room: [
    { min: 1, max: 20, message: '班级教室长度为1-20个字', trigger: 'blur' }
  ],
  beginDate: [{ required: true, message: '开课时间为必填项', trigger: 'change' }],
  endDate: [{ required: true, message: '结课时间为必填项', trigger: 'change' }],
  subject: [{ required: true, message: '学科为必填项', trigger: 'change' }]
})

//重置表单
const resetForm = (clazzForm) => {
  if (!clazzForm) return
  clazzForm.resetFields()
}

//-------------保存班级信息 
const save = (clazzForm) => {
  //表单校验
  if(!clazzForm) return
  clazzForm.validate(async (valid) => {
    if(valid) {
      let api 
      if(clazz.value.id) {
        api = updateApi(clazz.value)
      }else {
        api = addApi(clazz.value)
      }

      let result = await api
      if(result.code) {
        ElMessage.success('操作成功')
        dialogFormVisible.value = false
        queryPage()
      }else {
        ElMessage.error(result.msg)
      }
    }else {
      return false
    }
  })
}


//------- 删除班级
//根据ID删除单个班级
const delById = async (id) => {
  ElMessageBox.confirm('您确认删除此数据吗?' , '删除班级', {confirmButtonText:'确认', cancelButtonText:'取消',type:'warning'})
    .then(async () => {
      let result =  await deleteApi(id)
      if(result.code) {
        ElMessage.success('删除成功')
        queryPage()
      }else {
        ElMessage.error(result.msg)
      }
    }).catch(() => {
      ElMessage.info('取消删除')
    })
}
</script>

<template>
  <div class="page-container">
    <!-- 顶部标题与操作栏 -->
    <div class="page-header">
      <div class="title-area">
        <h2 class="page-title">班级管理</h2>
        <span class="page-subtitle">管理全校班级、课程安排及教室分配</span>
      </div>
      <div class="action-area">
        <el-button type="primary" @click="addClazz();resetForm(clazzFormRef)">
          <el-icon class="el-icon--left"><Plus /></el-icon>新增班级
        </el-button>
      </div>
    </div>

    <!-- 筛选卡片 -->
    <el-card class="filter-card" shadow="hover">
      <el-form :inline="true" :model="searchClazz" class="filter-form">
        <el-form-item label="班级名称">
          <el-input v-model="searchClazz.name" placeholder="搜索班级名称" clearable />
        </el-form-item>
        
        <el-form-item label="结课时间">
          <el-date-picker
            v-model="searchClazz.date"
            type="daterange"
            range-separator=" 至 "
            start-placeholder="开始"
            end-placeholder="结束"
            value-format="YYYY-MM-DD"
            style="width: 240px"
          />
        </el-form-item>

        <el-form-item>
          <el-button @click="queryPage()">
            <el-icon><Search /></el-icon>
          </el-button>
          <el-button link @click="clear()">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <div class="spacer"></div>
    
    <!-- 数据列表 -->
    <el-card class="table-card" shadow="never">
      <el-table :data="tableData" style="width: 100%">
        <el-table-column type="index" label="序号" width="60" />
        
        <el-table-column prop="name" label="班级名称" min-width="180">
           <template #default="scope">
             <span class="text-strong">{{ scope.row.name }}</span>
           </template>
        </el-table-column>
        
        <el-table-column prop="room" label="教室" min-width="120" class-name="text-secondary" />
        
        <el-table-column prop="masterName" label="班主任" width="120">
           <template #default="scope">
             <el-tag size="small" effect="plain" type="info">{{ scope.row.masterName || '未分配' }}</el-tag>
           </template>
        </el-table-column>
        
        <el-table-column prop="beginDate" label="开课时间" width="140" class-name="mono-font" />
        <el-table-column prop="endDate" label="结课时间" width="140" class-name="mono-font" />
        
        <el-table-column prop="updateTime" label="更新时间" width="160" align="right" class-name="text-secondary mono-font" />
        
        <el-table-column label="操作" width="160" fixed="right" align="right">
          <template #default="scope">
            <el-button type="primary" link @click="updateClazz(scope.row.id); resetForm(clazzFormRef)">编辑</el-button>
            <el-button type="danger" link @click="delById(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[5, 10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next"
          :total="pagination.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          background
        />
      </div>
    </el-card>

    <!-- 新增/修改对话框 -->
    <el-dialog v-model="dialogFormVisible" :title="formTitle" width="600px" destroy-on-close class="app-dialog">
      <el-form :model="clazz" ref="clazzFormRef" :rules="rules" label-position="top">
        <div class="form-grid">
           <!-- Row 1 -->
           <el-form-item label="班级名称" prop="name" class="full-width">
              <el-input v-model="clazz.name" placeholder="请输入班级名称" />
           </el-form-item>

           <!-- Row 2 -->
           <el-form-item label="班级教室" prop="room">
              <el-input v-model="clazz.room" placeholder="例如：A301"/>
           </el-form-item>
           <el-form-item label="班主任">
              <el-select v-model="clazz.masterId" placeholder="选择班主任" style="width: 100%;" filterable>
                <el-option v-for="emp in emps" :key="emp.id" :label="emp.name" :value="emp.id" />
              </el-select>
           </el-form-item>

           <!-- Row 3 -->
           <el-form-item label="开课时间" prop="beginDate">
              <el-date-picker v-model="clazz.beginDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%;"/>
           </el-form-item>
           <el-form-item label="结课时间" prop="endDate">
              <el-date-picker v-model="clazz.endDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%;"/>
           </el-form-item>
           
           <!-- Row 4 -->
           <el-form-item label="所属学科" prop="subject" class="full-width">
             <el-radio-group v-model="clazz.subject">
                <el-radio v-for="sub in subjects" :key="sub.value" :label="sub.value">{{ sub.name }}</el-radio>
             </el-radio-group>
           </el-form-item>
        </div>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisible = false; resetForm(clazzFormRef)">取消</el-button>
          <el-button type="primary" @click="save(clazzFormRef)">保存</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<style scoped>
.page-container {
  display: flex;
  flex-direction: column;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin-bottom: 4px;
}

.page-subtitle {
  font-size: 13px;
  color: var(--el-text-color-secondary);
}

.filter-card {
  margin-bottom: 16px;
  border: none;
}

.filter-form .el-form-item {
  margin-bottom: 0;
  margin-right: 24px;
}

.table-card {
  border: none;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.text-strong {
  font-weight: 500;
  color: var(--el-text-color-primary);
}

.text-secondary {
  color: var(--el-text-color-secondary);
}

.mono-font {
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
}

/* Dialog Form Grid */
.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.full-width {
  grid-column: span 2;
}
</style>