<template>
  <a-layout>
<!--    <a-layout-header class="header">-->
    <a-layout-header class="header" style="background: #000; display: flex; align-items: center; justify-content: space-between;">
      <div class="logo" style="color: green; font-size: 18px; font-weight: bold;">
        员工管理系统
      </div>
      <a-menu
          v-model:selectedKeys="selectedKeys1"
          theme="dark"
          mode="horizontal"
          :style="{ lineHeight: '64px' }"
      >
        <a-menu-item key="1" style="color: white">考勤设置</a-menu-item>
      </a-menu>
    </a-layout-header>
    <a-layout>
      <a-layout-sider width="200" style="background: #fff">
        <a-menu
            v-model:selectedKeys="selectedKeys2"
            v-model:openKeys="openKeys"
            mode="inline"
            :style="{ height: '100%', borderRight: 0 }"
        >
          <a-sub-menu key="sub1">
            <template #title>
              <span>
                <user-outlined/>
                主页
              </span>
            </template>
            <a-menu-item key="1">查看个人信息</a-menu-item>
            <a-menu-item key="2">修改个人信息</a-menu-item>
            <a-menu-item key="3">进行当日考勤</a-menu-item>
            <a-menu-item key="4">查看考勤记录</a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="sub2">
            <template #title>
              <span>
                <laptop-outlined/>
                员工管理
              </span>
            </template>
            <a-menu-item key="5">员工基本信息</a-menu-item>
            <a-menu-item key="6">添加员工信息</a-menu-item>
            <a-menu-item key="7">员工角色管理</a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="sub3">
            <template #title>
              <span>
                <notification-outlined/>
                请假管理
              </span>
            </template>
            <a-menu-item key="8">请假申请</a-menu-item>
            <a-menu-item key="9">请假记录</a-menu-item>
            <a-menu-item key="10">请假审批</a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="sub4">
            <template #title>
              <span>
                <notification-outlined/>
                加班管理
              </span>
            </template>
            <a-menu-item key="11">加班申请</a-menu-item>
            <a-menu-item key="12">加班记录</a-menu-item>
            <a-menu-item key="13">加班审批</a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="sub5">
            <template #title>
              <span>
                <notification-outlined/>
                出差管理
              </span>
            </template>
            <a-menu-item key="14">出差申请</a-menu-item>
            <a-menu-item key="15">出差记录</a-menu-item>
            <a-menu-item key="16">出差审批</a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="sub6">
            <template #title>
              <span>
                <notification-outlined/>
                统计管理
              </span>
            </template>
            <a-menu-item key="14">考勤统计</a-menu-item>
            <a-menu-item key="15">图表统计</a-menu-item>
            <a-menu-item key="16">部门人数统计</a-menu-item>
          </a-sub-menu>
        </a-menu>
      </a-layout-sider>
      <a-layout style="padding: 0 24px 24px">
        <a-breadcrumb style="margin: 16px 0">
          <a-breadcrumb-item>Home</a-breadcrumb-item>
          <a-breadcrumb-item>List</a-breadcrumb-item>
          <a-breadcrumb-item>App</a-breadcrumb-item>
        </a-breadcrumb>
        <a-layout-content
            :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
          <router-view/>
        </a-layout-content>
      </a-layout>
    </a-layout>
  </a-layout>
</template>
<script lang="ts" setup>
import {ref} from 'vue';
import {LaptopOutlined, NotificationOutlined, UserOutlined} from '@ant-design/icons-vue';

const selectedKeys1 = ref<string[]>(['2']);
const selectedKeys2 = ref<string[]>(['1']);
const openKeys = ref<string[]>(['sub1']);
</script>
<style scoped>
#components-layout-demo-top-side-2 .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top-side-2 .logo {
  float: right;
  margin: 16px 0 16px 24px;
}

.site-layout-background {
  background: #fff;
}
</style>
