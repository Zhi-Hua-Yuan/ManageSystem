<script lang="ts" setup>
import {reactive} from 'vue';
import {useRouter} from 'vue-router'
import {employeeLogin} from "../api/employee.ts";

interface FormState {
  employeeId: number | null;
  employeeAccount: string;
  employeePassword: string;
}

const formState = reactive<FormState>({
  employeeId: null,
  employeeAccount: '',
  employeePassword: '',
});

const router = useRouter();

const handleSubmit = async (values: any) => {
  const res = await employeeLogin(values);
  console.log(res);
  // 登录成功的状态码为200
  if (res.data.code === 200 && res.data){
    // 登录成功后，跳转到首页
    await router.replace('/employee/home');
    console.log('登陆成功!');
  }
  else{
    console.log('登陆失败!');
  }
};

</script>

<template>
  <a-form
      :model="formState"
      name="basic"
      :label-col="{ span: 10 }"
      :wrapper-col="{ span: 6 }"
      autocomplete="off"
      @finish="handleSubmit"
      class="login-form"
  >

    <a-form-item
        label="员工工号"
        name="employeeId"
        :rules="[{ required: true, message: '请输入你的员工工号!' }]"
    >
      <a-input v-model:value="formState.employeeId"/>
    </a-form-item>

    <a-form-item
        label="账号名"
        name="employeeAccount"
        :rules="[{ required: true, message: '请输入你的账号!' }]"
    >
      <a-input v-model:value="formState.employeeAccount"/>
    </a-form-item>

    <a-form-item
        label="密码"
        name="employeePassword"
        :rules="[{ required: true, message: '请输入你的密码!' }]"
    >
      <a-input-password v-model:value="formState.employeePassword"/>
    </a-form-item>

    <a-form-item :wrapper-col="{ offset: 12, span: 16 }">
      <a-button type="primary" html-type="submit">登录</a-button>
    </a-form-item>
  </a-form>
</template>

<style scoped>

</style>