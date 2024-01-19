<template>
    <header>
        <!-- <div class="wrapper">
            <Form/>
        </div> -->
    </header>
    <Form
      @submitForm="handleSubmitForm"
    />
</template>

<div id="map"></div>
<script setup>
import axios from 'axios';
import { ref, onMounted } from 'vue';
import Form from './Form.vue'

const handleSubmitForm = (formData) => {
  console.log('formData', formData);
    // On utilise Axios pour envoyer une requête GET à l'API
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

</script>


<style scoped>

</style>
