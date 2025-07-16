<script setup lang="ts">
import { onMounted } from 'vue';
import { useGenreStore } from './stores/genreStore';

const genreStore = useGenreStore();

onMounted(async () => {
  await genreStore.loadGenres();
});

</script>

<template>
  <h1>Hello App!</h1>
  <p><strong>Current route path:</strong> {{ $route.fullPath }}</p>
  <nav>
    <RouterLink to="/">Go to Home</RouterLink>
    <RouterLink to="/genres">Go to Genre</RouterLink>
    <RouterLink to="/actors">Go to Actors</RouterLink>
    <RouterLink to="/movies">Go to Movies</RouterLink>

  </nav>
  <main>
    <RouterView />
  </main>
</template>
