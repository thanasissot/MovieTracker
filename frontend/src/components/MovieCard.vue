<script setup lang="ts">
import Tag from 'primevue/tag';
import Card from 'primevue/card';
import type { Movie } from '../service/movieService';
import type { Genre } from '../service/genreService';

// Define props
const props = defineProps<{
  movie: Movie;
  genres: Genre[];  // Array of genre objects for this movie
}>();

console.log(props)

</script>

<template>
  <Card style="width: 25rem; overflow: hidden">
    <template #header>
      movie icon
    </template>
    <template #title>{{ movie.title }}</template>
    <template #subtitle>Released: {{ movie.year }}</template>
    <template #content>
      <div class="flex gap-2 flex-wrap mb-3">
        <Tag v-for="genre in genres" :key="genre.id" :value="genre.genreName"></Tag>
      </div>
    </template>
  </Card>
</template>

