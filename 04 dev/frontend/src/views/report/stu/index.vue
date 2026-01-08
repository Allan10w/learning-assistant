<script setup>
import { onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { queryStudentCountDataApi, queryStudentDegreeDataApi } from '@/api/report'

// 颜色常量 (Apple 调色盘)
const COLORS = ['#0071E3', '#34C759', '#FF9F0A', '#FF3B30', '#AF52DE', '#5856D6', '#00C7BE', '#FFCC00']

//钩子函数 - 加载报表
onMounted(async () => {
  await nextTick()
  loadStudentCountChart() //加载班级人数报表
  loadDegreeChart() //加载学历统计报表
})

//获取班级人数统计报表
const loadStudentCountChart = async () => {
  let result = await queryStudentCountDataApi();
  let clazzList = result.data.clazzList;
  let dataList = result.data.dataList;

  initStudentCountChart(clazzList, dataList)
}

//获取学历统计报表
const loadDegreeChart = async () => {
  let result = await queryStudentDegreeDataApi();
  initDegreeChart(result.data)
}

//班级人数统计报表
function initStudentCountChart(clazzList, dataList) {
  const myChart = echarts.init(document.getElementById('container1'));
  myChart.setOption({
    title: {
      text: '班级人数统计',
      left: 'center',
      top: 10,
      textStyle: {
        fontSize: 16,
        fontWeight: '600',
        color: '#1D1D1F'
      }
    },
    grid:{
      left: '8%',
      right: '8%',
      bottom: '10%',
      top: '20%',
      containLabel: true
    },
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255, 255, 255, 0.9)',
      borderWidth: 0,
      shadowBlur: 10,
      shadowColor: 'rgba(0,0,0,0.1)'
    },
    xAxis: {
      type: 'category',
      data: clazzList,
      axisLine: { show: false },
      axisTick: { show: false },
      axisLabel: {
        interval: 0,
        rotate: 30
      }
    },
    yAxis: {
      type: 'value',
      splitLine: { lineStyle: { type: 'dashed', color: '#E5E5E5' } }
    },
    series: [
      {
        name: '人数',
        type: 'bar',
        barWidth: '40%',
        data: dataList,
        itemStyle:{
          borderRadius: [6, 6, 0, 0],
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#34C759' },
            { offset: 1, color: '#30B44B' }
          ])
        },
        emphasis: {
          itemStyle: {
            color: '#28A745'
          }
        }
      }
    ]
  });
}

function initDegreeChart(degreeDataList) {
  const myChart = echarts.init(document.getElementById('container2'));
  let option = {
    title: {
      text: '学员学历统计',
      left: 'center',
      top: 10,
      textStyle: {
        fontSize: 16,
        fontWeight: '600',
        color: '#1D1D1F'
      }
    },
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(255, 255, 255, 0.9)',
      borderWidth: 0
    },
    legend: {
      bottom: '5%',
      left: 'center',
      itemWidth: 10,
      itemHeight: 10,
      icon: 'circle'
    },
    series: [
      {
        name: '学历',
        type: 'pie',
        radius: ['45%', '70%'],
        center: ['50%', '50%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 8,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: { show: false },
        data: degreeDataList.map((item, index) => ({
           ...item,
           itemStyle: { color: COLORS[index % COLORS.length] }
        }))
      }
    ]
  };
  myChart.setOption(option);
}

</script>

<template>
  <div class="page-container">
    <div class="page-header">
       <h2 class="page-title">学员信息统计</h2>
       <span class="page-subtitle">实时监控班级容量与学历构成</span>
    </div>

    <div class="charts-grid">
       <el-card class="chart-card">
          <div class="chart-container" id="container1"></div>
       </el-card>

       <el-card class="chart-card">
          <div class="chart-container" id="container2"></div>
       </el-card>
    </div>
  </div>
</template>

<style scoped>
.page-container {
  display: flex;
  flex-direction: column;
}

.page-header {
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

.charts-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
}

.chart-card {
  border: none;
  min-height: 450px;
}

.chart-container {
  width: 100%;
  height: 400px;
}

@media (max-width: 1200px) {
  .charts-grid {
    grid-template-columns: 1fr;
  }
}
</style>
