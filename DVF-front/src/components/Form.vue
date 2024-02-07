<template>
  <div ref="mapContainer" style="width: 100%; height: 600px"></div>
  <form @submit.prevent="handleSubmit" @change="handleFormChange" class="mx-auto p-6 bg-white rounded-lg shadow-md max-w-lg flex gap-2 flex-col flexbox">
    <label for="latitude">Latitude : </label>
    <input type="number" v-model="latitude" placeholder="latitude" step="any" id="latitude">
    <label for="longitude">Longitude : </label>
    <input type="number" v-model="longitude" placeholder="longitude" step="any" id="longitude">
    <label for="radius">Radius (meters) : </label>
    <input type="number" v-model="radius" placeholder="radius" id="radius">
    <button type="submit" class="btn-submit">Validate</button>
  </form>
</template>

<div id="map"></div>
<script setup lang="ts">

import { ref, defineEmits, watch, defineProps, onMounted} from 'vue';
import L, { marker } from 'leaflet';

const map = ref()
const mapContainer = ref()
const markerPos = ref()
const circle = ref()
const defaultLatitude = 45.764043;
const defaultLongitude = 4.835659;
const defaultRadius = 500;

onMounted(() => {
  map.value = L.map(mapContainer.value).setView([defaultLatitude, defaultLongitude], 13);
  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
  }).addTo(map.value);
  markerPos.value = L.marker([defaultLatitude, defaultLongitude],{draggable: true});
  markerPos.value.addTo(map.value).on("drag", (event)=> {
    circle.value.setLatLng([markerPos.value._latlng.lat, markerPos.value._latlng.lng]);
    latitude.value = markerPos.value._latlng.lat;
    longitude.value = markerPos.value._latlng.lng;
    radius.value = circle.value.getRadius();
  });
  
  map.value.on("click", (event)=> {
    latitude.value = event.latlng.lat;
    longitude.value = event.latlng.lng;
    radius.value = circle.value.getRadius();

    map.value.setView([latitude.value, longitude.value], map.value.getZoom());
   
    markerPos.value.setLatLng([latitude.value, longitude.value]);
    circle.value.setLatLng([latitude.value, longitude.value]);
    circle.value.setRadius(radius.value);
  })
  circle.value = L.circle([defaultLatitude, defaultLongitude], {
    color: 'red',
    fillColor: '#f03',
    fillOpacity: 0.5,
    radius: defaultRadius
  });
  circle.value.addTo(map.value);

  // Default form values
  latitude.value = markerPos.value._latlng.lat;
  longitude.value = markerPos.value._latlng.lng;
  radius.value = circle.value.getRadius();
})


const longitude = ref('');
const latitude = ref('');
const radius = ref('');

const emit = defineEmits<{
  (event: 'submitForm' , {} ): void
}>()

const handleFormChange = () => {
  map.value.setView([latitude.value, longitude.value], map.value.getZoom());
  markerPos.value.setLatLng([latitude.value, longitude.value]);
  circle.value.setLatLng([latitude.value, longitude.value]);
  circle.value.setRadius(radius.value);
};

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
  margin-top: 10px;
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
