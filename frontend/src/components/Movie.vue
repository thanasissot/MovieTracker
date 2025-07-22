<template>
  <h1>Movies</h1>
  <Toast />
  <ConfirmPopup />

  <div class="grid grid-cols-3 grid-rows-3 gap-4">
    <template v-for="movie in movies" :key="movie.id">
      <MovieCard
          :movie="movie"
          :genres="getMovieGenres(movie.genreIds)"
      />
    </template>
  </div>

<!--  <div class="card">-->
<!--    <DataTable-->
<!--        :value="movies"-->
<!--        removableSort-->
<!--        v-model:filters="filters"-->
<!--        filterDisplay="row"-->
<!--        :loading="loading"-->
<!--        :globalFilterFields="['id', 'title', 'year']"-->
<!--        tableStyle="min-width: 24rem"-->
<!--        v-model:editingRows="editingRows"-->
<!--        editMode="row"-->
<!--        @row-edit-save="onRowEditSave"-->
<!--        v-model:expandedRows="expandedRows"-->
<!--        @rowExpand="onRowExpand"-->
<!--        @rowCollapse="onRowCollapse"-->
<!--    >-->
<!--      <template #header>-->
<!--        <div class="flex justify-end">-->
<!--          <IconField>-->
<!--            <InputIcon>-->
<!--              <i class="pi pi-search" />-->
<!--            </InputIcon>-->
<!--            <InputText v-model="filters['global'].value" placeholder="Keyword Search" />-->
<!--          </IconField>-->
<!--        </div>-->
<!--      </template>-->
<!--      <template #empty> No data found. </template>-->
<!--      <template #loading> Loading data. Please wait. </template>-->

<!--      <Column expander style="width: 5rem" header="View Genres"/>-->
<!--      <Column field="id" header="ID" sortable bodyStyle="text-align:center">-->
<!--        <template #body="{ data }">-->
<!--          {{ data.id }}-->
<!--        </template>-->
<!--        <template #filter="{ filterModel, filterCallback }">-->
<!--          <InputText v-model="filterModel.value" type="text" @input="filterCallback()" placeholder="Search by id" />-->
<!--        </template>-->
<!--      </Column>-->
<!--      <Column field="title" header="Title" sortable bodyStyle="text-align:center">-->
<!--        <template #editor="{ data, field }">-->
<!--          <InputText v-model="data[field]" fluid />-->
<!--        </template>-->
<!--        <template #filter="{ filterModel, filterCallback }">-->
<!--          <InputText v-model="filterModel.value" type="text" @input="filterCallback()" placeholder="Search by title" />-->
<!--        </template>-->
<!--      </Column>-->
<!--      <Column field="year" header="Year" sortable bodyStyle="text-align:center">-->
<!--        <template #editor="{ data, field }">-->
<!--          <InputText v-model="data[field]" type="number" fluid />-->
<!--        </template>-->
<!--        <template #filter="{ filterModel, filterCallback }">-->
<!--          <InputText v-model="filterModel.value" type="text" @input="filterCallback()" placeholder="Search by year" />-->
<!--        </template>-->
<!--      </Column>-->
<!--      <Column :rowEditor="true" style="width: 10%; min-width: 8rem" bodyStyle="text-align:center"></Column>-->
<!--      <Column class="w-24 !text-end">-->
<!--        <template #body="slotProps">-->
<!--          <Button @click="confirmDelete($event, slotProps.data)" label="Delete" severity="danger" outlined></Button>-->
<!--        </template>-->
<!--      </Column>-->
<!--      <template #expansion="slotProps">-->
<!--        <div class="p-4">-->
<!--          <DataTable :value="getMovieGenres(slotProps.data.genreIds)">-->
<!--            <Column field="id" header="Id"></Column>-->
<!--            <Column field="genreName" header="Genre"></Column>-->
<!--          </DataTable>-->
<!--        </div>-->
<!--      </template>-->
<!--    </DataTable>-->
<!--  </div>-->

<!--  &lt;!&ndash; Form for adding new movies &ndash;&gt;-->
<!--  <div class="card flex justify-center mt-4">-->
<!--    <Form v-slot="$form" :resolver="resolver" :initialValues="initialValues" @submit="onFormSubmit" class="flex flex-col gap-4 w-full sm:w-56">-->
<!--      <div class="flex flex-col gap-1">-->
<!--        <InputText name="title" type="text" placeholder="Title" fluid />-->
<!--        <Message v-if="$form.title?.invalid" severity="error" size="small" variant="simple">{{ $form.title.error?.message }}</Message>-->
<!--      </div>-->
<!--      <div class="flex flex-col gap-1">-->
<!--        <InputText name="year" type="number" placeholder="Year" fluid />-->
<!--        <Message v-if="$form.year?.invalid" severity="error" size="small" variant="simple">{{ $form.year.error?.message }}</Message>-->
<!--      </div>-->
<!--      <Button type="submit" severity="secondary" label="Submit" />-->
<!--    </Form>-->
<!--  </div>-->
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { zodResolver } from '@primevue/forms/resolvers/zod';
import { z } from 'zod';
import { FilterMatchMode } from '@primevue/core/api';
import { Form, type FormSubmitEvent } from '@primevue/forms';
import { useToast } from 'primevue/usetoast';
import { useConfirm } from 'primevue/useconfirm';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import Message from 'primevue/message';
import ConfirmPopup from 'primevue/confirmpopup';
import Toast from 'primevue/toast';
import movieService, { type Movie } from '../service/movieService.ts';
import { useGenreStore } from '../stores/genreStore';
import MovieCard from "./MovieCard.vue";
import type { Genre } from '../service/genreService';

// State
const movies = ref<Movie[]>([]);
const loading = ref(true);
const editingRows = ref([]);
const expandedRows = ref([]);
const initialValues = ref({ title: '', year: null });
const genreStore = useGenreStore();
const toast = useToast();
const confirm = useConfirm();

// Filters
const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  id: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  title: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  year: { value: null, matchMode: FilterMatchMode.STARTS_WITH }
});

// Load data
const loadMovies = async () => {
  try {
    loading.value = true;
    movies.value = await movieService.getAllMovies();
  } catch (error) {
    toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to load movies', life: 3000 });
    console.error('Error loading movies:', error);
  } finally {
    loading.value = false;
  }
};

// Initialize data
onMounted(async () => {
  await genreStore.loadGenres();
  await loadMovies();
});

// Helper function to get genre objects for a movie
const getMovieGenres = (genreIds?: number[]) => {
  if (!genreIds || genreIds.length === 0) return [];
  let genres = genreIds.map(id => genreStore.getGenreById(id)) as Genre[];
  console.log(genres)
  return genres;
};

// Validation
const currentYear = new Date().getFullYear();
const resolver = zodResolver(
    z.object({
      title: z.string().min(1, { message: 'Title is required.' }),
      year: z.coerce.number()
          .min(1900, { message: 'Year must be 1900 or later.' })
          .max(currentYear + 5, { message: `Year must be ${currentYear + 5} or earlier.` })
    })
);

// Form submission
const onFormSubmit = async (e: FormSubmitEvent) => {
  if (e.valid) {
    try {
      loading.value = true;
      await movieService.createMovie(e.values.title, Number(e.values.year));
      await loadMovies();
      toast.add({ severity: 'success', summary: 'Success', detail: 'Movie created', life: 3000 });
      initialValues.value = { title: '', year: null };
      e.reset();
    } catch (error) {
      toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to create movie', life: 3000 });
      console.error('Error creating movie:', error);
    } finally {
      loading.value = false;
    }
  }
};

// Row edit save
const onRowEditSave = async (event: { newData: Movie }) => {
  try {
    loading.value = true;
    await movieService.updateMovie(event.newData);
    await loadMovies();
    toast.add({ severity: 'success', summary: 'Success', detail: 'Movie updated', life: 3000 });
  } catch (error) {
    toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to update movie', life: 3000 });
    console.error('Error updating movie:', error);
  } finally {
    loading.value = false;
  }
};

// Delete movie
const confirmDelete = (event: Event, movie: Movie) => {
  confirm.require({
    target: event.currentTarget as HTMLElement,
    message: 'Do you want to delete this movie?',
    icon: 'pi pi-info-circle',
    rejectProps: {
      label: 'Cancel',
      severity: 'secondary',
      outlined: true
    },
    acceptProps: {
      label: 'Delete',
      severity: 'danger'
    },
    accept: async () => {
      try {
        loading.value = true;
        await movieService.deleteMovie(movie.id);
        await loadMovies();
        toast.add({ severity: 'success', summary: 'Success', detail: 'Movie deleted', life: 3000 });
      } catch (error) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to delete movie', life: 3000 });
        console.error('Error deleting movie:', error);
      } finally {
        loading.value = false;
      }
    },
    reject: () => {
      toast.add({ severity: 'info', summary: 'Rejected', detail: 'Operation cancelled', life: 3000 });
    }
  });
};

// @ts-ignore
const onRowExpand = (event) => {
  toast.add({ severity: 'info', summary: 'Product Expanded', detail: event.data.name, life: 3000 });
};
// @ts-ignore
const onRowCollapse = (event) => {
  toast.add({ severity: 'success', summary: 'Product Collapsed', detail: event.data.name, life: 3000 });
};


</script>