<template>
  <h1>Genre</h1>
  <Toast />
  <div class="card">
    <DataTable :value="genres" removableSort
               v-model:filters="filters"
               filterDisplay="row" :loading="loading"
               :globalFilterFields="['id', 'genreName']"
               tableStyle="min-width: 24rem">

      <template #header>
        <div class="flex justify-end">
          <IconField>
            <InputIcon>
              <i class="pi pi-search" />
            </InputIcon>
            <InputText v-model="filters['global'].value" placeholder="Keyword Search" />
          </IconField>
        </div>
      </template>
      <template #empty> No data found. </template>
      <template #loading> Loading data. Please wait. </template>

      <Column field="id" header="ID" sortable bodyStyle="text-align:center">
        <template #body="{ data }">
          {{ data.id }}
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <InputText v-model="filterModel.value" type="text" @input="filterCallback()" placeholder="Search by id" />
        </template>
      </Column>
      <Column field="genreName" header="Genre" sortable bodyStyle="text-align:center">
        <template #body="{ data }">
          {{ data.genreName }}
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <InputText v-model="filterModel.value" type="text" @input="filterCallback()" placeholder="Search by genre" />
        </template>
      </Column>
    </DataTable>
  </div>

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

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { FilterMatchMode } from '@primevue/core/api';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import { zodResolver } from '@primevue/forms/resolvers/zod';
import { useToast } from "primevue/usetoast";
import { z } from 'zod';
import Message from 'primevue/message';
import { Form } from '@primevue/forms';


const API_URL = `http://localhost:8080/genres`;

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  id: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  genreName: { value: null, matchMode: FilterMatchMode.STARTS_WITH }
});

async function getData() {
  const response = await fetch(API_URL+'/all');
  if (!response.ok) {
    throw new Error('Network response was not ok');
  }
  return await response.json();
}

async function createGenre(genreName: string) {
  const response = await fetch(API_URL+'/create', {
      method: 'POST',
      headers: {'Content-Type':'application/json'},//very important for uri
      body: JSON.stringify({ genreName: genreName })
    });

  if (!response.ok) {
    throw new Error('Network response was not ok');
  }
  return await response.json();
}

onMounted(() => {
  getData().then((data) => {
    genres.value = data
    loading.value = false
  });
});

const genres = ref();
const loading = ref(true);

const toast = useToast();
const initialValues = ref({
  genreName: '',
});

const resolver = ref(zodResolver(
  z.object({
    genreName: z.string().min(1, { message: 'genre is required.' }),
  })
));

const onFormSubmit = (e) => {
  if (e.valid) {
    loading.value=true;
    createGenre(e.values.genreName).then(
      () => {
        getData().then((data) => {
          genres.value = data
          loading.value = false
          toast.add({ severity: 'success', summary: 'Genre is created', life: 3000 });
        });
      }
    )
  }
};

</script>
