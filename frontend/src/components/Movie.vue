<!-- frontend/src/components/Movie.vue -->
<template>
  <DataTableComponent
      title="Movies"
      :service="movieServiceAdapter"
      entityName="Movie"
      :filterFields="['id', 'title', 'year']"
      ref="dataTable"
  >
    <template #columns>
      <Column field="id" header="ID" sortable bodyStyle="text-align:center">
        <template #body="{ data }">
          {{ data.id }}
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <InputText v-model="filterModel.value" type="text" @input="filterCallback()" placeholder="Search by id" />
        </template>
      </Column>
      <Column field="title" header="Title" sortable bodyStyle="text-align:center">
        <template #editor="{ data, field }">
          <InputText v-model="data[field]" fluid />
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <InputText v-model="filterModel.value" type="text" @input="filterCallback()" placeholder="Search by title" />
        </template>
      </Column>
      <Column field="year" header="Year" sortable bodyStyle="text-align:center">
        <template #editor="{ data, field }">
          <InputText v-model="data[field]" type="number" fluid />
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <InputText v-model="filterModel.value" type="text" @input="filterCallback()" placeholder="Search by year" />
        </template>
      </Column>
    </template>

    <template #form>
      <div class="card flex justify-center">
        <Form v-slot="$form" :resolver="resolver" :initialValues="initialValues" @submit="onFormSubmit" class="flex flex-col gap-4 w-full sm:w-56">
          <div class="flex flex-col gap-1">
            <InputText name="title" type="text" placeholder="Title" fluid />
            <Message v-if="$form.title?.invalid" severity="error" size="small" variant="simple">{{ $form.title.error?.message }}</Message>
          </div>
          <div class="flex flex-col gap-1">
            <InputText name="year" type="number" placeholder="Year" fluid />
            <Message v-if="$form.year?.invalid" severity="error" size="small" variant="simple">{{ $form.year.error?.message }}</Message>
          </div>
          <Button type="submit" severity="secondary" label="Submit" />
        </Form>
      </div>
    </template>
  </DataTableComponent>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { zodResolver } from '@primevue/forms/resolvers/zod';
import { z } from 'zod';
import { Form } from '@primevue/forms';
import Column from 'primevue/column';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import Message from 'primevue/message';
import DataTableComponent from '../generic-datatable/DataTableComponent.vue';
import movieService from '../service/movieService';
import { createServiceAdapter } from '../generic-datatable/serviceAdapter';
import type { Movie } from '../service/movieService.ts';

const initialValues = ref({ title: '', year: new Date().getFullYear() });
const dataTable = ref<{ handleSubmit: (values: any, resetForm: () => void) => void } | null>(null);

// Adapt the movie service to the DataService interface
const movieServiceAdapter = createServiceAdapter<Movie>({
  getAll: () => movieService.getAllMovies(),
  create: (data: { title: string; year: number }) => movieService.createMovie(data.title, Number(data.year)),
  update: (data: Movie) => movieService.updateMovie(data),
  delete: (id: number) => movieService.deleteMovie(id)
});

const currentYear = new Date().getFullYear();
const resolver = zodResolver(
    z.object({
      title: z.string().min(1, { message: 'Title is required.' }),
      year: z.coerce.number()
          .min(1900, { message: 'Year must be 1900 or later.' })
          .max(currentYear + 5, { message: `Year must be ${currentYear + 5} or earlier.` })
    })
);

const onFormSubmit = (e: { valid: boolean; values: any }) => {
  if (e.valid && dataTable.value) {
    dataTable.value.handleSubmit(e.values, () => {
      initialValues.value.title = '';
      initialValues.value.year = new Date().getFullYear();
    });
  }
};
</script>