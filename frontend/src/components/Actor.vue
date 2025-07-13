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

const API_URL = `http://localhost:8080/actors`;

const actors = ref();
const loading = ref(true);
const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  id: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  firstname: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  lastname: { value: null, matchMode: FilterMatchMode.STARTS_WITH }
});

async function getData() {
  const response = await fetch(API_URL+'/all');
  if (!response.ok) {
    throw new Error('Network response was not ok');
  }
  return await response.json();
}

async function createActor(firstname: string, lastname: string) {
  const response = await fetch(API_URL+'/create', {
    method: 'POST',
    headers: {'Content-Type':'application/json'},//very important for uri
    body: JSON.stringify({ firstname: firstname, lastname: lastname })
  });

  if (!response.ok) {
    throw new Error('Network response was not ok');
  }
  return await response.json();
}

onMounted(() => {
  getData().then((data) => {
    actors.value = data
    loading.value = false
  });
});

const toast = useToast();
const initialValues = ref({
  firstname: '',
  lastname: '',
});

const resolver = ref(zodResolver(
    z.object({
      firstname: z.string().min(1, { message: 'firstname is required.' }),
      lastname: z.string().min(1, { message: 'lastname is required.' }),
    })
));

const onFormSubmit = (e) => {
  if (e.valid) {
    loading.value=true;
    createActor(e.values.firstname, e.values.lastname).then(
        () => {
          getData().then((data) => {
            actors.value = data
            loading.value = false
            toast.add({ severity: 'success', summary: 'Genre is created', life: 3000 });
          });
        }
    )
  }
  initialValues.value.firstname = '';
  initialValues.value.lastname = '';
};

</script>

<template>
  <h1>Actors</h1>
  <Toast />
  <div class="card">
    <DataTable :value="actors" removableSort
               v-model:filters="filters"
               filterDisplay="row" :loading="loading"
               :globalFilterFields="['id', 'firstname', 'lastname']"
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
      <Column field="firstname" header="Firstname" sortable bodyStyle="text-align:center">
        <template #body="{ data }">
          {{ data.firstname }}
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <InputText v-model="filterModel.value" type="text" @input="filterCallback()" placeholder="Search by firstname" />
        </template>
      </Column>
      <Column field="lastname" header="Lastname" sortable bodyStyle="text-align:center">
        <template #body="{ data }">
          {{ data.lastname }}
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <InputText v-model="filterModel.value" type="text" @input="filterCallback()" placeholder="Search by lastname" />
        </template>
      </Column>
    </DataTable>
  </div>

  <div class="card flex justify-center">
    <Form v-slot="$form" :resolver="resolver" :initialValues="initialValues" @submit="onFormSubmit" class="flex flex-col gap-4 w-full sm:w-56">
      <div class="flex flex-col gap-1">
        <InputText name="firstname" type="text" placeholder="Firstname" fluid />
        <Message v-if="$form.firstname?.invalid" severity="error" size="small" variant="simple">{{ $form.firstname.error?.message }}</Message>
      </div>
      <div class="flex flex-col gap-1">
        <InputText name="lastname" type="text" placeholder="Lastname" fluid />
        <Message v-if="$form.lastname?.invalid" severity="error" size="small" variant="simple">{{ $form.lastname.error?.message }}</Message>
      </div>
      <Button type="submit" severity="secondary" label="Submit" />
    </Form>
  </div>

</template>

<style scoped>

</style>