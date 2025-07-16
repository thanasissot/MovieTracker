<script setup lang="ts">


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
