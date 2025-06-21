<script setup lang="ts">
import {ref} from 'vue';
import {Dayjs} from 'dayjs';

const value = ref<Dayjs>();
const onPanelChange = (value: Dayjs, mode: string) => {
  console.log(value, mode);
};
</script>

<template>
  <a-calendar v-model:value="value" @panelChange="onPanelChange"/>
</template>

<style scoped>

</style>