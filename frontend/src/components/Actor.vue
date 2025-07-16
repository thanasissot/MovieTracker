<!-- frontend/src/components/Actor.vue -->
<template>
  <DataTableComponent
      title="Actors"
      :service="actorServiceAdapter"
      entityName="Actor"
      :filterFields="['id', 'firstname', 'lastname']"
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
    </template>

    <template #form>
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
import actorService from '../service/actorService';
import { createServiceAdapter } from '../generic-datatable/serviceAdapter';
import type { Actor } from '../service/actorService';

const initialValues = ref({ firstname: '', lastname: '' });
const dataTable = ref<{ handleSubmit: (values: any, resetForm: () => void) => void } | null>(null);

// Adapt the actor service to the DataService interface
const actorServiceAdapter = createServiceAdapter<Actor>({
  getAll: () => actorService.getAllActors(),
  create: (data: { firstname: string; lastname: string }) => actorService.createActor(data.firstname, data.lastname),
  update: (data: Actor) => actorService.updateActor(data),
  delete: (id: number) => actorService.deleteActor(id)
});

const resolver = zodResolver(
    z.object({
      firstname: z.string().min(1, { message: 'firstname is required.' }),
      lastname: z.string().min(1, { message: 'lastname is required.' }),
    })
);

const onFormSubmit = (e: { valid: boolean; values: any }) => {
  if (e.valid && dataTable.value) {
    dataTable.value.handleSubmit(e.values, () => {
      initialValues.value.firstname = '';
      initialValues.value.lastname = '';
    });
  }
};
</script>