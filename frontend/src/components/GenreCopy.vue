<script setup lang="ts">
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import { zodResolver } from '@primevue/forms/resolvers/zod';
import { z } from 'zod';
import Message from 'primevue/message';
import { Form } from '@primevue/forms';
import ConfirmPopup from 'primevue/confirmpopup';
import Toast from 'primevue/toast';
import genreService from '../service/genreService';
import type { Genre } from '../service/genreService';

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  id: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  genreName: { value: null, matchMode: FilterMatchMode.STARTS_WITH }
});
const genres = ref<Genre[]>([]);
const loading = ref(true);
const confirm = useConfirm();
const toast = useToast();
const initialValues = ref({
  genreName: '',
});
const editingRows = ref([]);

const resolver = ref(zodResolver(
    z.object({
      genreName: z.string().min(1, { message: 'genre is required.' }),
    })
));

async function loadGenres() {
  try {
    loading.value = true;
    genres.value = await genreService.getAllGenres();
  } catch (error) {
    toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to load genres', life: 3000 });
    console.error('Error loading genres:', error);
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  loadGenres();
});

const onFormSubmit = async (e) => {
  if (e.valid) {
    try {
      loading.value = true;
      await genreService.createGenre(e.values.genreName);
      await loadGenres();
      toast.add({ severity: 'success', summary: 'Genre is created', life: 3000 });
      initialValues.value.genreName = '';
    } catch (error) {
      toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to create genre', life: 3000 });
      console.error('Error creating genre:', error);
    } finally {
      loading.value = false;
    }
  }
};

const deleteGenre = (event, data: Genre) => {
  confirm.require({
    target: event.currentTarget,
    message: 'Do you want to delete this Genre?',
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
        await genreService.deleteGenre(data.id);
        await loadGenres();
        toast.add({ severity: 'success', summary: 'Confirmed', detail: 'Genre deleted', life: 3000 });
      } catch (error) {
        console.log("error", error)
        toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to delete genre', life: 3000 });
      } finally {
        loading.value = false;
      }
    },
    reject: () => {
      toast.add({ severity: 'error', summary: 'Rejected', detail: 'You have rejected', life: 3000 });
    }
  });
};

const onRowEditSave = async (event) => {
  let { newData, index } = event;

  try {
    loading.value = true;
    await genreService.updateGenre(newData);
    await loadGenres();
    toast.add({ severity: 'success', summary: 'Genre is updated', life: 3000 });
    initialValues.value.genreName = '';
  } catch (error) {
    toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to update genre', life: 3000 });
    console.error('Error updating genre:', error);
  } finally {
    loading.value = false;
  }

};

</script>

<template>
  <h1>Genre</h1>
  <Toast />
  <div class="card">
    <DataTable
        :value="genres"
        removableSort
        v-model:filters="filters"
        filterDisplay="row"
        :loading="loading"
        :globalFilterFields="['id', 'genreName']"
        tableStyle="min-width: 24rem"
        v-model:editingRows="editingRows"
        editMode="row"
        @row-edit-save="onRowEditSave"
    >

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
        <template #editor="{ data, field }">
          <InputText v-model="data[field]" fluid />
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <InputText v-model="filterModel.value" type="text" @input="filterCallback()" placeholder="Search by genre" />
        </template>
      </Column>
      <Column :rowEditor="true" style="width: 10%; min-width: 8rem" bodyStyle="text-align:center"></Column>
      <Column class="w-24 !text-end">
        <template #body="{ data }">
          <ConfirmPopup></ConfirmPopup>
          <Toast />
          <Button @click="deleteGenre($event, data)" label="Delete" severity="danger" outlined></Button>
          <!--          <Button icon="pi pi-trash" @click="selectRow(data)" severity="danger" rounded></Button>-->
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