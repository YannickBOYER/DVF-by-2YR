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
    // On utilise Axios pour envoyer une requête GET à l'API
    axios.get('http://localhost:8080/LigneTransaction/generatePdfByLocation', {
      headers: {
        'Content-Type': 'application/json'
      },
      params: {
        "longitude": formData.longitude,
        "latitude": formData.latitude,
        "rayon": formData.radius
      },
      responseType: 'arraybuffer' // Définit le type de réponse attendu comme un tableau d'octets
    })
    .then(response => {
        const pdfData = new Blob([response.data], { type: 'application/pdf' });
        const pdfUrl = URL.createObjectURL(pdfData);
        window.open(pdfUrl);
        const downloadLink = document.createElement('a');
        downloadLink.href = pdfUrl;
        downloadLink.download = `document_${formData.latitude}_${formData.longitude}_${formData.radius}.pdf`;
        downloadLink.click();
    })
    .catch(error => {
        console.error(error);
        if (error.response != null) {
          const textContent = new TextDecoder('utf-8').decode(error.response.data);
          alert(`${textContent}`);
        }
        else {
          alert("L'API n'est pas disponible");
        }
    });
};

</script>


<style scoped>

</style>
