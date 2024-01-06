<template>
    <header>
        <!-- <div class="wrapper">
            <HelloWorld/>
        </div> -->
    </header>
    <div ref="mapContainer" class="map-container" />
    <HelloWorld
      @submitForm="handleSubmitForm"
    />
</template>

<div id="map"></div>
<script setup>
import axios from 'axios';
import { ref, onMounted } from 'vue';
import maplibregl from 'maplibre-gl';
import 'maplibre-gl/dist/maplibre-gl.css';
import HelloWorld from './HelloWorld.vue'

const map = ref(null);
const mapContainer = ref(null);
const center = [ 4.83242748730953,45.76024989812256]; // Coordonnées de Lyon, par exemple

const handleSubmitForm = (formData) => {
  console.log('formData', formData);
    // Use Axios or another HTTP library to send a GET request
    axios.get('http://localhost:8080/LigneTransaction/generatePdfByLocationParam', {
      headers: {
        'Content-Type': 'application/json'
      },
      params: {
        "longitude": formData.longitude,
        "latitude": formData.latitude,
        "rayon": formData.radius
      }
    })
    .then(response => {
        console.log(response.data);
    })
    .catch(error => {
        console.error(error);
    });
};

onMounted(() => {
  map.value = new maplibregl.Map({
    container: mapContainer.value,
    style: 'https://api.maptiler.com/maps/streets/style.json?key=get_your_own_OpIi9ZULNHzrESv6T2vL',
    center: center,
    zoom: 13
  })
});


// const setCircle = (radius, longitude, latitude) => {
//     const circleGeoJSON = {
//         type: 'Feature',
//         geometry: {
//             type: 'Point',
//             coordinates: [4.83242748730953, 45.76024989812256]
//         },
//         properties: {
//             radius: 4400000, // Rayon du cercle en mètres
//             color: 'blue'  // Couleur du cercle
//         }
//     };
//     console.log(circleGeoJSON);
//     map.value.addSource('circle-source', {
//       'type': 'geojson',
//       'data': circleGeoJSON
//     });

//     map.value.addLayer({
//       'id': 'circle-layer',
//       'type': 'fill',
//       'source': 'circle-source',
//       'layout': {},
//       'paint': {
//         'fill-color': '#007cbf',
//         'fill-opacity': 0.5
//       }
//     });
// };


// const handleSubmitForm = (formValue) => {
//     console.log('TEST');
//     // radius.value = parseInt(formValue.radius) * 0.001;
//     setCircle(parseInt(formValue.radius) * 0.001, formValue.longitude, formValue.latitude);
//     console.log('TEST');
//     console.log('formValue aeazeazea', formValue);
// };

</script>



<style scoped>
.map-container {
  height: 80vh;
  width: 80vw;
}
</style>
