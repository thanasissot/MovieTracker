<script setup lang="ts">
  import type { Genre } from '../service/genreService'
  import { useGenreStore } from '@/stores/genreStore.ts'

  const genreStore = useGenreStore()
  const loading = ref(true)
  const search = ref('')
  const headers = [
    { text: 'ID', value: 'id', sortable: true },
    { text: 'Genre Name', value: 'genreName', sortable: true },
  ]
  const sortBy = ref([{ key: 'id', order: 'asc' }])
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
      v-model:sort-by="sortBy"
      fixed-header
      :headers="headers"
      height="500"
      item-value="name"
      :items="genreStore.genres"
      :loading="loading"
      :search="search"
    />
    <pre>{{ sortBy }}</pre>
  </v-card>
</template>
