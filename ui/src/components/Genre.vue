<script setup lang="ts">
  import { ref, computed, onMounted } from 'vue'
  import type { Genre } from '../service/genreService'
  import { useGenreStore } from '@/stores/genreStore.ts'
  import { useField, useForm } from 'vee-validate'

  const { handleSubmit, handleReset } = useForm({
    validationSchema: {
      name (value: string) {
        if (!!(value) && value.trim().length > 0) return true

        return  'You must enter a Genre first.'
      },
    },
  })

  const genreStore = useGenreStore()
  const loading = ref(false)
  const name = useField('name')

  const headers = [
    { title: 'ID', text: 'ID', value: 'id', sortable: true },
    { title: 'Genre', text: 'Genre Name', value: 'genreName', sortable: true },
  ]

  // Load genres on component mount
  onMounted(async () => {
    await genreStore.loadGenres().then(() => {
      loading.value = false
    })
  })

  const submit = handleSubmit((values, { resetForm }) => {
    alert(JSON.stringify(values, null, 2))
    console.log(values)
    loading.value = true
    genreStore.addGenre(values.name.trim()).then(() => {
      genreStore.loadGenres()
      loading.value = false
      resetForm({
        values: { name: '' }, // Reset the form values
      });
    })
  })

// const add = () => {
//   genreStore.addGenre(search.value.trim()).then(() => {
//     search.value = ''
//     console.log(searchField)
//     genreStore.loadGenres()
//     console.log(searchField)
//   })
// }
// function onSubmit () {
//   if (!form.value) return
//
//   loading.value = true
//   genreStore.addGenre(search.value.trim()).then(() => {
//     search.value = ''
//     genreStore.loadGenres()
//     form.value.reset()
//     form.value.resetValidation()
//     loading.value = false
//   })
// }
// function required (v: string) {
//   return !!v || 'Field is required'
// }

</script>

<template>
  <v-card
    flat
    title="Genre"
  >
    <template v-slot:title>
      <v-form fast-fail width="100%"
          @submit.prevent="submit"
      >
        <v-container>
          <v-row>
            <v-col cols="6" sm="6">
              <v-text-field
                  v-model="name.value.value"
                  hide-details="auto"
                  label="Search or click to add a new genre"
                  prepend-inner-icon="mdi-magnify"
                  single-line
                  variant="outlined"
                  clearable
                  :error-messages="name.errorMessage.value"
              />
            </v-col>
            <v-col cols="6" sm="6" class="d-flex">
              <v-btn
                  class="ma-auto pa-auto fill-height w-75"
                  prepend-icon="mdi-plus"
                  rounded="lg"
                  text="Add a Genre"
                  type="submit"
              ></v-btn>
            </v-col>
          </v-row>
        </v-container>

      </v-form>
    </template>
    <template v-slot:item>
      <v-data-table
          :sort-by="[{ key: 'id', order: 'asc' }, { key: 'genreName', order: 'asc' }]"
          multi-sort
          fixed-header
          :headers="headers"
          height="500"
          :items="genreStore.genres"
          :loading="loading"
          :search="name.value.value"
      >
      </v-data-table>
    </template>
  </v-card>
</template>

<style>
.v-data-table-header__sort-badge {
  display: none !important;
}
</style>
