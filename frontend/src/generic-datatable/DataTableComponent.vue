<script setup lang="ts">
import { useDataTable } from './dataTable.ts';
import type { BaseEntity, DataService } from './dataTable.ts';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import ConfirmPopup from 'primevue/confirmpopup';
import Toast from 'primevue/toast';

const props = defineProps({
  title: {
    type: String,
    required: true
  },
  service: {
    type: Object as () => DataService<BaseEntity>,
    required: true
  },
  entityName: {
    type: String,
    required: true
  },
  filterFields: {
    type: Array as () => string[],
    required: true
  },
  enableEdit: {
    type: Boolean,
    default: true
  },
  enableDelete: {
    type: Boolean,
    default: true
  }
});

const {
  items,
  loading,
  filters,
  editingRows,
  handleSubmit,
  handleDelete,
  handleRowEditSave
} = useDataTable({
  service: props.service,
  entityName: props.entityName,
  filterFields: props.filterFields
});

const onDelete = (event: Event, item: BaseEntity) => {
  handleDelete(event, item);
};

const onRowEditSave = (event: { newData: BaseEntity }) => {
  handleRowEditSave(event);
};

defineExpose({
  items,
  loading,
  handleSubmit
});
</script>

<template>
  <h1>{{ title }}</h1>
  <Toast />
  <div class="card">
    <DataTable
        :value="items"
        removableSort
        v-model:filters="filters"
        filterDisplay="row"
        :loading="loading"
        :globalFilterFields="filterFields"
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

      <slot name="columns"></slot>

      <Column :rowEditor="true" style="width: 10%; min-width: 8rem" bodyStyle="text-align:center" v-if="enableEdit"></Column>
      <Column class="w-24 !text-end" v-if="enableDelete">
        <template #body="slotProps">
          <ConfirmPopup></ConfirmPopup>
          <Button @click="onDelete($event, slotProps.data)" label="Delete" severity="danger" outlined></Button>
        </template>
      </Column>
    </DataTable>
  </div>

  <slot name="form"></slot>
</template>

<style scoped>

</style>