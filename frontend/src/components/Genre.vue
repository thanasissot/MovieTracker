<script setup lang="ts">
import DataTable from 'primevue/datatable';
import { ref, onMounted } from 'vue';
import { zodResolver } from '@primevue/forms/resolvers/zod';
import { z } from 'zod';
import {Form, type FormSubmitEvent} from '@primevue/forms';
import Column from 'primevue/column';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import Message from 'primevue/message';
import { useGenreStore } from '../stores/genreStore';
import { FilterMatchMode } from '@primevue/core/api';
import ConfirmPopup from "primevue/confirmpopup";
import {useToast} from "primevue/usetoast";
import {useConfirm} from "primevue/useconfirm";
import InputIcon from 'primevue/inputicon';
import IconField from 'primevue/iconfield';
import type { Genre } from '../service/genreService';
import Toast from "primevue/toast";

const genreStore = useGenreStore();
const editingRows = ref([]);
const initialValues = ref({ genreName: '' });
const loading = ref(true);
const toast = useToast();
const confirm = useConfirm();

// Load genres on component mount
onMounted(async () => {
  await genreStore.loadGenres().then(() => {
    loading.value = false;
  })
});

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  id: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  genreName: { value: null, matchMode: FilterMatchMode.STARTS_WITH }
});

const resolver = zodResolver(
    z.object({
      genreName: z.string().min(1, { message: 'genre is required.' }),
    })
);

const onFormSubmit = async (e: FormSubmitEvent) => {
  if (e.valid) {
    try {
      loading.value = true;
      await genreStore.addGenre(e.values.genreName);
      await genreStore.loadGenres().then(() => {
        loading.value = false;
      })
      toast.add({ severity: 'success', summary: 'Success', detail: 'Genre created', life: 3000 });
      initialValues.value.genreName = '';
      e.reset();
    } catch (error) {
      toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to create Genre', life: 3000 });
      console.error('Error creating Genre:', error);
    } finally {
      loading.value = false;
    }
  }
};

// Delete movie
const confirmDelete = (event: Event, genre: Genre) => {
  confirm.require({
    target: event.currentTarget as HTMLElement,
    message: 'Do you want to delete this genre?',
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
        await genreStore.deleteGenre(genre.id);
        await genreStore.loadGenres().then(() => {
          loading.value = false;
        })
        toast.add({ severity: 'success', summary: 'Success', detail: 'Genre deleted', life: 3000 });
      } catch (error) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to delete Genre', life: 3000 });
        console.error('Error deleting Genre:', error);
      } finally {
        loading.value = false;
      }
    },
    reject: () => {
      toast.add({ severity: 'info', summary: 'Rejected', detail: 'Operation cancelled', life: 3000 });
    }
  });
};

// Row edit save
const onRowEditSave = async (event: { newData: Genre }) => {
  try {
    loading.value = true;
    await genreStore.updateGenre(event.newData.id, event.newData.genreName);
    await genreStore.loadGenres().then(() => {
      loading.value = false;
    })
    toast.add({ severity: 'success', summary: 'Success', detail: 'Genre updated', life: 3000 });
  } catch (error) {
    toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to update Genre', life: 3000 });
    console.error('Error updating Genre:', error);
  } finally {
    loading.value = false;
  }
};

</script>

<template>
  <div class="card">
    <DataTable
        :value="genreStore.genres"
        stripedRows
        removableSort
        showGridlines
        v-model:filters="filters"
        dataKey="id"
        filterDisplay="row"
        :loading="loading"
        :globalFilterFields="['id', 'genreName']"
        v-model:editingRows="editingRows"
        editMode="row"
        @row-edit-save="onRowEditSave"
        tableStyle="min-width: 25rem">
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
      <template #empty> No genres found. </template>
      <template #loading> Loading genres data. Please wait. </template>
      <Column field="id"  sortable  style="min-width: 12rem; text-align: center">
        <template #header>
          <span class="flex-1 text-center">Id</span>
        </template>
        <template #body="{ data }">
          {{ data.id }}
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <InputText v-model="filterModel.value" type="text" @input="filterCallback()" style="text-align: center" placeholder="Search by id" />
        </template>
      </Column>
      <Column field="genreName" sortable style="min-width: 12rem; text-align: center">
        <template #header>
          <span class="flex-1 text-center">Name</span>
        </template>
        <template #editor="{ data, field }">
          <InputText v-model="data[field]" fluid />
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <InputText v-model="filterModel.value" type="text" @input="filterCallback()" style="text-align: center" placeholder="Search by Name" />
        </template>
      </Column>
      <Column :rowEditor="true" style="width: 10%; min-width: 8rem" bodyStyle="text-align:center"></Column>
      <Column class="w-24 !text-end">
        <template #body="slotProps">
          <ConfirmPopup></ConfirmPopup>
          <Button @click="confirmDelete($event, slotProps.data)" label="Delete" severity="danger" outlined></Button>
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
  <Toast />
</template>


<!--&lt;!&ndash; frontend/src/components/Genre.vue &ndash;&gt;-->
<!--<template>-->
<!--  <DataTableComponent-->
<!--      title="Genres"-->
<!--      :service="genreServiceAdapter"-->
<!--      entityName="Genre"-->
<!--      :filterFields="['id', 'genreName']"-->
<!--      ref="dataTable"-->
<!--  >-->
<!--    <template #columns>-->
<!--      <Column field="id" header="ID" sortable bodyStyle="text-align:center">-->
<!--        <template #body="{ data }">-->
<!--          {{ data.id }}-->
<!--        </template>-->
<!--        <template #filter="{ filterModel, filterCallback }">-->
<!--          <InputText v-model="filterModel.value" type="text" @input="filterCallback()" placeholder="Search by id" />-->
<!--        </template>-->
<!--      </Column>-->
<!--      <Column field="genreName" header="Genre" sortable bodyStyle="text-align:center">-->
<!--        <template #editor="{ data, field }">-->
<!--          <InputText v-model="data[field]" fluid />-->
<!--        </template>-->
<!--        <template #filter="{ filterModel, filterCallback }">-->
<!--          <InputText v-model="filterModel.value" type="text" @input="filterCallback()" placeholder="Search by genre" />-->
<!--        </template>-->
<!--      </Column>-->
<!--    </template>-->

<!--    <template #form>-->
<!--      <div class="card flex justify-center">-->
<!--        <Form v-slot="$form" :resolver="resolver" :initialValues="initialValues" @submit="onFormSubmit" class="flex flex-col gap-4 w-full sm:w-56">-->
<!--          <div class="flex flex-col gap-1">-->
<!--            <InputText name="genreName" type="text" placeholder="Genre" fluid />-->
<!--            <Message v-if="$form.genreName?.invalid" severity="error" size="small" variant="simple">{{ $form.genreName.error?.message }}</Message>-->
<!--          </div>-->
<!--          <Button type="submit" severity="secondary" label="Submit" />-->
<!--        </Form>-->
<!--      </div>-->
<!--    </template>-->
<!--  </DataTableComponent>-->
<!--</template>-->

