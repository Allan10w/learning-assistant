<script setup>
import { onMounted, ref } from 'vue'
import { queryPageApi , addApi, queryInfoApi, updateApi, deleteApi, handleViolationApi} from '@/api/stu'
import { queryAllApi as queryAllClazzApi } from '@/api/clazz'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete, Search } from '@element-plus/icons-vue' // Import icons explicitly if needed, though they are likely globally registered

//学历列表数据
const degrees = ref([{ name: '初中', value: 1 },{ name: '高中', value: 2 },{ name: '大专', value: 3 },{ name: '本科', value: 4 },{ name: '硕士', value: 5 },{ name: '博士', value: 6 }])
//性别列表数据
const genders = ref([{ name: '男', value: 1 }, { name: '女', value: 2 }])
//是否是院校学员列表数据
const whethers = ref([{ name: '是', value: 1 }, { name: '否', value: 0 }])
//搜索表单对象
let searchStu = ref({clazzId: '',degree: '',name: ''}) 
//列表展示数据
let tableData = ref([])

//钩子函数 - 页面加载时触发
onMounted(() => {
  queryPage()
  queryAllClazz()
})


//所有的班级数据
let clazzs = ref([])
//加载所有的班级数据
const queryAllClazz = async () => {
  let result = await queryAllClazzApi()
  if(result.code) {
    clazzs.value = result.data
  }
}


//复选框
let selectIds = ref([])
const handleSelectionChange = (selection) => {
  selectIds.value = selection.map(item => item.id)
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
      searchStu.value.clazzId,
      searchStu.value.degree,
      searchStu.value.name,
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
  searchStu.value = {clazzId:'', degree:'', name:'',}
  queryPage()
}


//----------- 新增 / 修改 ---------------------------
let dialogFormVisible = ref(false) //控制新增/修改的对话框的显示与隐藏
let labelWidth = ref(100) //form表单label的宽度
let formTitle = ref('') //表单的标题
let stu = ref({ //学生对象-表单数据绑定
  name: '',
  no: '',
  gender: '',
  phone: '',
  idCard: '',
  isCollege: '',
  address: '',
  degree: '',
  graduationDate: '',
  clazzId: ''
})

//清空表单
const clearStu = () => {
  stu.value = {
    name: '',
    no: '',
    gender: '',
    phone: '',
    idCard: '',
    isCollege: '',
    address: '',
    degree: '',
    graduationDate: '',
    clazzId: ''
  }
}

//新增学员
const addStu = () => {
  dialogFormVisible.value = true
  formTitle.value = '新增学员'
  clearStu()
}

//修改学生
const updateStu = async (id) => {
  clearStu()
  dialogFormVisible.value = true
  formTitle.value = '修改学员'
  let result = await queryInfoApi(id)
  if(result.code){
    stu.value = result.data
  }
}

//表单校验规则
const stuFormRef = ref()
const rules = ref({
  name: [
    { required: true, message: '姓名为必填项', trigger: 'blur' },
    { min: 2, max: 10, message: '姓名长度为2-20个字', trigger: 'blur' }
  ],
  no: [
    { required: true, message: '学号为必填项', trigger: 'blur' },
    { min: 10, max: 10, message: '学号长度为10个字', trigger: 'blur' }
  ],
  gender: [{ required: true, message: '性别为必填项', trigger: 'change' }],
  phone: [
    { required: true, message: '手机号为必填项', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/g, message: '请输入合法的手机号', trigger: 'blur' }
  ],
  idCard: [
    { required: true, message: '身份证号为必填项', trigger: 'blur' },
    { pattern: /^\d{18}$/g, message: '请输入合法的身份证号', trigger: 'blur' }
  ],
  isCollege: [
    { required: true, message: '是否院校学员为必填项', trigger: 'change' }
  ]
})

//重置表单
const resetForm = (stuForm) => {
  if (!stuForm) return
  stuForm.resetFields()
}

//-------------保存学生信息 
const save = (stuForm) => {
  //表单校验
  if(!stuForm) return
  stuForm.validate(async (valid) => {
    if(valid) {
      let api 
      if(stu.value.id) {
        api = updateApi(stu.value)
      }else {
        api = addApi(stu.value)
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


//------- 删除学生
//根据ID删除单个学生
const delById = async (id) => {
  ElMessageBox.confirm('您确认删除此数据吗?' , '删除学生', {confirmButtonText:'确认', cancelButtonText:'取消', type:'warning'})
    .then(async () => {
      let result =  await deleteApi(`${id}`)
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

//批量删除员工
const delByIds = async () => {
  ElMessageBox.confirm('您确认删除此数据吗?' , '删除学生', {confirmButtonText:'确认', cancelButtonText:'取消',type:'warning'})
    .then(async () => {
      let result =  await deleteApi(selectIds.value.join(','))
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


const openViolation = (id) => {
  ElMessageBox.prompt('请输入违纪扣分', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    inputPattern: /\d?/,
    inputErrorMessage: '数据无效',
  }).then(async (val) => {    
    let result = await handleViolationApi(id, val.value);
    if(result.code) {
      ElMessage.success('处理成功')
      queryPage()
    }else {
      ElMessage.error(result.msg)
    }
  })
}
</script>

<template>
  <div class="page-container">
    <!-- 顶部标题与操作栏 -->
    <div class="page-header">
      <div class="title-area">
        <h2 class="page-title">学员管理</h2>
        <span class="page-subtitle">管理全校学生档案与信息</span>
      </div>
      <div class="action-area">
        <el-button type="primary" @click="addStu();resetForm(stuFormRef)">
          <el-icon class="el-icon--left"><Plus /></el-icon>新增学员
        </el-button>
        <el-button type="danger" plain @click="delByIds()" :disabled="selectIds.length === 0">
          <el-icon class="el-icon--left"><Delete /></el-icon>批量删除
        </el-button>
      </div>
    </div>

    <!-- 筛选卡片 -->
    <el-card class="filter-card" shadow="hover">
      <el-form :inline="true" :model="searchStu" class="filter-form">
        <el-form-item label="姓名">
          <el-input v-model="searchStu.name" placeholder="搜索姓名" clearable />
        </el-form-item>

        <el-form-item label="最高学历">
          <el-select v-model="searchStu.degree" placeholder="全部学历" clearable style="width: 140px">
             <el-option v-for="degree in degrees" :key="degree.value" :label="degree.name" :value="degree.value" />
          </el-select>
        </el-form-item>

        <el-form-item label="所属班级">
          <el-select v-model="searchStu.clazzId" placeholder="全部班级" clearable filterable style="width: 180px">
             <el-option v-for="clazz in clazzs" :key="clazz.id" :label="clazz.name" :value="clazz.id" />
          </el-select>
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
      <el-table :data="tableData" style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" align="center" width="40" />
        
        <el-table-column prop="name" label="姓名" min-width="100">
          <template #default="scope">
            <span class="text-strong">{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="no" label="学号" min-width="120" class-name="mono-font" />
        <el-table-column prop="clazzName" label="班级" min-width="140" />
        
        <el-table-column prop="gender" label="性别" width="70">
          <template #default="scope">
            {{ scope.row.gender == 1 ? '男': '女' }}
          </template>
        </el-table-column>
        
        <el-table-column prop="phone" label="手机号" width="120" />
        
        <el-table-column prop="degree" label="最高学历" width="90">
          <template #default="scope">
            <el-tag size="small" type="info" effect="plain">{{ degrees.find(d => d.value == scope.row.degree)?.name || '其他' }}</el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="violationCount" label="违纪次数" align="right" width="100">
           <template #default="scope">
             <span :class="{'text-danger': scope.row.violationCount > 0}">{{ scope.row.violationCount }}</span>
           </template>
        </el-table-column>
        
        <el-table-column prop="violationScore" label="扣分" align="right" width="80">
            <template #default="scope">
             <span :class="{'text-danger': scope.row.violationScore > 0}">{{ scope.row.violationScore }}</span>
           </template>
        </el-table-column>
        
        <el-table-column prop="updateTime" label="更新时间" width="160" align="right" class-name="text-secondary" />
        
        <el-table-column label="操作" width="180" fixed="right" align="right">
          <template #default="scope">
            <el-button type="primary" link @click="updateStu(scope.row.id);resetForm(stuFormRef)">编辑</el-button>
            <el-button type="warning" link @click="openViolation(scope.row.id)">违纪</el-button>
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

    <!-- 新增/修改学员对话框 -->
    <el-dialog v-model="dialogFormVisible" :title="formTitle" width="600px" destroy-on-close class="app-dialog">
      <el-form :model="stu" ref="stuFormRef" :rules="rules" label-position="top">
        <div class="form-grid">
           <!-- Row 1 -->
           <el-form-item label="姓名" prop="name">
              <el-input v-model="stu.name" placeholder="请输入姓名"/>
           </el-form-item>
           <el-form-item label="学号" prop="no">
              <el-input v-model="stu.no" placeholder="请输入学号"/>
           </el-form-item>
           
           <!-- Row 2 -->
           <el-form-item label="性别" prop="gender">
              <el-select v-model="stu.gender" placeholder="选择性别" style="width: 100%;">
                <el-option v-for="gender in genders" :key="gender.value" :label="gender.name" :value="gender.value" />
              </el-select>
           </el-form-item>
           <el-form-item label="手机号" prop="phone">
              <el-input v-model="stu.phone" placeholder="请输入手机号"/>
           </el-form-item>
           
           <!-- Row 3 -->
           <el-form-item label="身份证号" prop="idCard" class="full-width">
              <el-input v-model="stu.idCard" placeholder="请输入身份证号"/>
           </el-form-item>
           
           <!-- Row 4 -->
           <el-form-item label="所属班级" prop="clazzId">
              <el-select v-model="stu.clazzId" placeholder="选择班级" style="width: 100%;">
                <el-option v-for="clazz in clazzs" :key="clazz.id" :label="clazz.name" :value="clazz.id" />
              </el-select>
           </el-form-item>
           <el-form-item label="最高学历">
              <el-select v-model="stu.degree" placeholder="选择学历" style="width: 100%;">
                <el-option v-for="degree in degrees" :key="degree.value" :label="degree.name" :value="degree.value" />
              </el-select>
           </el-form-item>
           
           <!-- Row 5 -->
           <el-form-item label="是否院校" prop="isCollege">
             <el-radio-group v-model="stu.isCollege">
                <el-radio v-for="w in whethers" :key="w.value" :label="w.value">{{ w.name }}</el-radio>
             </el-radio-group>
           </el-form-item>
           <el-form-item label="毕业时间">
              <el-date-picker v-model="stu.graduationDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%;"/>
           </el-form-item>
           
           <!-- Row 6 -->
           <el-form-item label="联系地址" class="full-width">
              <el-input v-model="stu.address" placeholder="请输入详细地址" type="textarea" :rows="2"/>
           </el-form-item>
        </div>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisible = false; resetForm(stuFormRef)">取消</el-button>
          <el-button type="primary" @click="save(stuFormRef)">保存</el-button>
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

.text-danger {
  color: var(--el-color-danger);
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
