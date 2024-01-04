<template>
  <!-- <div class="flexbox top-left">
    <form @submit.prevent="handleSubmit" class="mx-auto p-6 bg-white rounded-lg shadow-md max-w-lg flex gap-2 flex-col ">
      <label for="longitude">Longitude : </label>
      <input type="text" v-model="input1" placeholder="longitude">
      <label for="latitude">Latitude : </label>
      <input type="text" v-model="input2" placeholder="latitude">
      <label for="radius">Radius (meters): </label>
      <input type="text" v-model="input3" placeholder="radius">
      <button @click="submitForm">Validate</button>
    </form>
  </div> -->
  
  <form @submit.prevent="handleSubmit" class="mx-auto p-6 bg-white rounded-lg shadow-md max-w-lg flex gap-2 flex-col flexbox">
    <label for="longitude">Longitude : </label>
    <input type="number" v-model="longitude" placeholder="longitude">
    <label for="latitude">Latitude : </label>
    <input type="number" v-model="latitude" placeholder="latitude">
    <label for="radius">Radius (meters) : </label>
    <input type="number" v-model="radius" placeholder="radius">
    <button type="submit">Validate</button>
  </form>
</template>

<script setup lang="ts">

import { ref, defineEmits, watch, defineProps } from 'vue';

const longitude = ref('');
const latitude = ref('');
const radius = ref('');

const emit = defineEmits<{
  (event: 'submitForm', {  } ): void
}>()

const handleSubmit = () => {
  emit('submitForm', {longitude: longitude.value, latitude: latitude.value, radius: radius.value})
};

  
</script>

<style scoped>
.flexbox {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: flex-start;
}

.top-left {
  position: absolute;
  top: 0;
  left: 0;
}
</style>
