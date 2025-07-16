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
import ConfirmPopup from 'primevue/confirmpopup';
import { useConfirm } from "primevue/useconfirm";
import Toast from 'primevue/toast';
import actorService from '../service/actorService';
import type { Actor } from '../service/actorService';

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  id: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  firstname: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  lastname: { value: null, matchMode: FilterMatchMode.STARTS_WITH }
});

const actors = ref<Actor[]>([]);
const loading = ref(true);
const confirm = useConfirm();
const toast = useToast();
const initialValues = ref({
  firstname: '',
  lastname: '',
});
const editingRows = ref([]);

const resolver = ref(zodResolver(
    z.object({
      firstname: z.string().min(1, { message: 'firstname is required.' }),
      lastname: z.string().min(1, { message: 'lastname is required.' }),
    })
));


async function loadActors() {
  try {
    loading.value = true;
    actors.value = await actorService.getAllActors();
  } catch (error) {
    toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to load actors', life: 3000 });
    console.error('Error loading actors:', error);
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  loadActors();
});

const onFormSubmit = async (e) => {
  if (e.valid) {
    try {
      loading.value = true;
      await actorService.createActor(e.values.firstname, e.values.lastname);
      await loadActors();
      toast.add({ severity: 'success', summary: 'Actor created', life: 3000 });
      initialValues.value.firstname = '';
      initialValues.value.lastname = '';
    } catch (error) {
      toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to create actor', life: 3000 });
      console.error('Error creating actor:', error);
    } finally {
      loading.value = false;
    }
  }
};

const deleteActor = (event, data: Actor) => {
  confirm.require({
    target: event.currentTarget,
    message: 'Do you want to delete this Actor?',
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
        await actorService.deleteActor(data.id);
        await loadActors();
        toast.add({ severity: 'success', summary: 'Confirmed', detail: 'Actor deleted', life: 3000 });
      } catch (error) {
        console.log("error", error);
        toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to delete actor', life: 3000 });
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
    await actorService.updateActor(newData);
    await loadActors();
    toast.add({ severity: 'success', summary: 'Actor updated', life: 3000 });
  } catch (error) {
    toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to update actor', life: 3000 });
    console.error('Error updating actor:', error);
  } finally {
    loading.value = false;
  }
};

</script>

<template>
  <h1>Actors</h1>
  <Toast />
  <div class="card">
    <DataTable
        :value="actors"
        removableSort
        v-model:filters="filters"
        filterDisplay="row"
        :loading="loading"
        :globalFilterFields="['id', 'firstname', 'lastname']"
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
      <Column field="firstname" header="Firstname" sortable bodyStyle="text-align:center">
        <template #editor="{ data, field }">
          <InputText v-model="data[field]" fluid />
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <InputText v-model="filterModel.value" type="text" @input="filterCallback()" placeholder="Search by firstname" />
        </template>
      </Column>
      <Column field="lastname" header="Lastname" sortable bodyStyle="text-align:center">
        <template #editor="{ data, field }">
          <InputText v-model="data[field]" fluid />
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <InputText v-model="filterModel.value" type="text" @input="filterCallback()" placeholder="Search by lastname" />
        </template>
      </Column>
      <Column :rowEditor="true" style="width: 10%; min-width: 8rem" bodyStyle="text-align:center"></Column>
      <Column class="w-24 !text-end">
        <template #body="{ data }">
          <ConfirmPopup></ConfirmPopup>
          <Toast />
          <Button @click="deleteActor($event, data)" label="Delete" severity="danger" outlined></Button>
          <!--          <Button icon="pi pi-trash" @click="selectRow(data)" severity="danger" rounded></Button>-->
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