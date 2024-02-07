<template>
  <header>
    <h1>Recherche de transactions immobilières</h1>
  </header>
    <div v-if="loading" class="loading-spinner">
      <!-- Affichage de la roue d'attente -->
      <span class="loader"></span>
    </div>

    <Form @submitForm="handleSubmitForm" />
</template>

<script setup>
import axios from 'axios';
import { ref } from 'vue'; // Importation de la fonction ref
import Form from './Form.vue';

const loading = ref(false); // Création d'une référence réactive pour gérer l'état de chargement

const handleSubmitForm = (formData) => {
  loading.value = true; // Affichage de la roue d'attente au début de l'appel API
  axios.get('http://localhost:8080/LigneTransaction/generatePdfByLocation', {
    headers: {
      'Content-Type': 'application/json'
    },
    params: {
      "longitude": formData.longitude,
      "latitude": formData.latitude,
      "rayon": formData.radius
    },
    responseType: 'arraybuffer'
  })
  .then(response => {
    loading.value = false; // Masquage de la roue d'attente après réception de la réponse
    const pdfData = new Blob([response.data], { type: 'application/pdf' });
    const pdfUrl = URL.createObjectURL(pdfData);
    window.open(pdfUrl);
    const downloadLink = document.createElement('a');
    downloadLink.href = pdfUrl;
    downloadLink.download = `document_${formData.latitude}_${formData.longitude}_${formData.radius}.pdf`;
    downloadLink.click();
  })
  .catch(error => {
    loading.value = false; // Masquage de la roue d'attente en cas d'erreur
    console.error(error);
    if (error.response != null) {
      const textContent = new TextDecoder('utf-8').decode(error.response.data);
      alert(`${textContent}`);
    } else {
      alert("L'API n'est pas disponible");
    }
  });
};
</script>

<style scoped>
h1 {
  margin-bottom: 10px;
  text-align: center;
}

.loading-spinner {
  position: absolute;
  top: 0%;
  left: 0%;
  z-index: 9999;
  height: 100%;
  width: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}
.loader {
    width: 48px;
    height: 48px;
    border: 5px solid #FFF;
    border-bottom-color: #FF3D00;
    border-radius: 50%;
    display: inline-block;
    box-sizing: border-box;
    animation: rotation 1s linear infinite;
}

@keyframes rotation {
0% {
    transform: rotate(0deg);
}
100% {
    transform: rotate(360deg);
}
} 
  
</style>
