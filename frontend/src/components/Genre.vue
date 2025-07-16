<!-- frontend/src/components/Genre.vue -->
<template>
  <DataTableComponent
      title="Genres"
      :service="genreServiceAdapter"
      entityName="Genre"
      :filterFields="['id', 'genreName']"
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
      <Column field="genreName" header="Genre" sortable bodyStyle="text-align:center">
        <template #editor="{ data, field }">
          <InputText v-model="data[field]" fluid />
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <InputText v-model="filterModel.value" type="text" @input="filterCallback()" placeholder="Search by genre" />
        </template>
      </Column>
    </template>

    <template #form>
      <div class="card flex justify-center">
        <Form v-slot="$form" :resolver="resolver" :initialValues="initialValues" @submit="onFormSubmit" class="flex flex-col gap-4 w-full sm:w-56">
          <div class="flex flex-col gap-1">
            <InputText name="genreName" type="text" placeholder="Genre" fluid />
            <Message v-if="$form.genreName?.invalid" severity="error" size="small" variant="simple">{{ $form.genreName.error?.message }}</Message>
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
import {Form, type FormSubmitEvent} from '@primevue/forms';
import Column from 'primevue/column';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import Message from 'primevue/message';
import DataTableComponent from '../generic-datatable/DataTableComponent.vue';
import genreService from '../service/genreService';
import { createServiceAdapter } from '../generic-datatable/serviceAdapter';

const initialValues = ref({ genreName: '' });
const dataTable = ref<{ handleSubmit: (values: any, resetForm: () => void) => void } | null>(null);

// Adapt the genre service to the DataService interface
const genreServiceAdapter = createServiceAdapter({
  getAll: () => genreService.getAllGenres(),
  create: (data) => genreService.createGenre(data.genreName),
  update: (data) => genreService.updateGenre(data),
  delete: (id) => genreService.deleteGenre(id)
});

const resolver = zodResolver(
    z.object({
      genreName: z.string().min(1, { message: 'genre is required.' }),
    })
);

const onFormSubmit = (e: FormSubmitEvent) => {
  if (e.valid && dataTable.value) {
    dataTable.value.handleSubmit(e.values, () => {
      initialValues.value.genreName = '';
      e.reset();
    });
  }
};
</script>