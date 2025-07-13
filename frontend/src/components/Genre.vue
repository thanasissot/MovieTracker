<template>
  <h1>Genre</h1>

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
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { FilterMatchMode } from '@primevue/core/api';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import InputText from 'primevue/inputtext';


const API_URL = `http://localhost:8080/genres/all`;

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  id: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  genreName: { value: null, matchMode: FilterMatchMode.STARTS_WITH }
});

async function getData() {
  const response = await fetch(API_URL);
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

</script>
