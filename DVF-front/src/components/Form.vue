<template>
  
  <form @submit.prevent="handleSubmit" class="mx-auto p-6 bg-white rounded-lg shadow-md max-w-lg flex gap-2 flex-col flexbox">
    <label for="longitude">Longitude : </label>
    <input type="number" v-model="longitude" placeholder="longitude" step="any">
    <label for="latitude">Latitude : </label>
    <input type="number" v-model="latitude" placeholder="latitude" step="any">
    <label for="radius">Radius (meters) : </label>
    <input type="number" v-model="radius" placeholder="radius">
    <button type="submit" class="btn-submit">Validate</button>
  </form>
</template>

<script setup lang="ts">

import { ref, defineEmits, watch, defineProps } from 'vue';

const longitude = ref('');
const latitude = ref('');
const radius = ref('');

const emit = defineEmits<{
  (event: 'submitForm' , {} ): void
}>()

const handleSubmit = () => {
  console.log(longitude.value);
  console.log(latitude.value);
  console.log(radius.value);
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

.btn-submit {
  background-color: #4CAF50; /* Green */
  border: none;
  color: white;
  padding: 10px 20px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin: 15px;
}

</style>
