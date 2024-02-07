<script setup>
import Map from './components/Map.vue'
</script>

<template>
  <main>
    <Map/>
  </main>
</template>

<style scoped>
header {
  line-height: 1.5;
}

main {
  position: absolute;
  top:0%;
  left:0%;

  height: 100%;
  width: 100%;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
}
</style>
