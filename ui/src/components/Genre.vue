<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
  import type { Genre } from '../service/genreService'
  import { useGenreStore } from '@/stores/genreStore.ts'

  const genreStore = useGenreStore()
  const loading = ref(true)
  const search = ref('')
  const headers = [
    { title: 'ID', text: 'ID', value: 'id', sortable: true },
    { title: 'Genre', text: 'Genre Name', value: 'genreName', sortable: true },
  ]
  const itemsPerPage = ref(5)

  // Load genres on component mount
  onMounted(async () => {
    await genreStore.loadGenres().then(() => {
      loading.value = false
    })
  })
</script>

<template>
  <v-card
    flat
    title="Genre"
  >
    <template #text>
      <v-text-field
        v-model="search"
        hide-details
        label="Search"
        prepend-inner-icon="mdi-magnify"
        single-line
        variant="outlined"
      />
    </template>
    <v-data-table
      :sort-by="[{ key: 'id', order: 'asc' }, { key: 'genreName', order: 'asc' }]"
      multi-sort
      fixed-header
      :headers="headers"
      height="500"
      :items="genreStore.genres"
      :loading="loading"
      :search="search"
    />
  </v-card>
</template>

<style>
.v-data-table-header__sort-badge {
  display: none !important;
}
</style>
