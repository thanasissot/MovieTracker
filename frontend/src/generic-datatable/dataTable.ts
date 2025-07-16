import { ref, onMounted} from 'vue';
import { FilterMatchMode } from '@primevue/core/api';
import { useToast } from "primevue/usetoast";
import { useConfirm } from "primevue/useconfirm";
import type { Ref } from 'vue';

// Base entity interface that all data types should implement
export interface BaseEntity {
    id: number;
    [key: string]: any;
}
// Service interface that all service classes should implement
export interface DataService<T extends BaseEntity> {
    getAll(): Promise<T[]>;
    create(data: any): Promise<T>;
    update(data: T): Promise<T>;
    delete(id: number): Promise<void>;
}

// Configuration for the data table
export interface DataTableConfig<T extends BaseEntity> {
    service: DataService<T>;
    entityName: string;
    filterFields: string[];
}

/**
 * Composable function for data table operations
 * @param config - Configuration object for the data table
 */
export function useDataTable<T extends BaseEntity>(config: DataTableConfig<T>) {
    const { service, entityName, filterFields } = config;

    // State
    const items = ref<T[]>([]) as Ref<T[]>;
    const loading = ref(true);
    const editingRows = ref([]);
    const toast = useToast();
    const confirm = useConfirm();

    // Create filters for all specified fields
    const filters = ref({
        global: { value: null, matchMode: FilterMatchMode.CONTAINS },
        ...filterFields.reduce((acc, field) => {
            acc[field] = { value: null, matchMode: FilterMatchMode.STARTS_WITH };
            return acc;
        },  {} as Record<string, { value: any; matchMode: string }>),
    });

    // Load data function
    const loadData = async () => {
        try {
            loading.value = true;
            items.value = await service.getAll();
        } catch (error) {
            toast.add({ severity: 'error', summary: 'Error', detail: `Failed to load ${entityName}s`, life: 3000 });
            console.error(`Error loading ${entityName}s:`, error);
        } finally {
            loading.value = false;
        }
    };

    // Initialize data
    onMounted(() => {
        loadData();
    });

    // Handle form submission
    const handleSubmit = async (values: any, resetForm: () => void) => {
        try {
            loading.value = true;
            await service.create(values);
            await loadData();
            toast.add({ severity: 'success', summary: 'Success', detail: `${entityName} created`, life: 3000 });
            resetForm();
        } catch (error) {
            toast.add({ severity: 'error', summary: 'Error', detail: `Failed to create ${entityName}`, life: 3000 });
            console.error(`Error creating ${entityName}:`, error);
        } finally {
            loading.value = false;
        }
    };

    // Handle item deletion
    const handleDelete = (event: Event, item: T) => {
        confirm.require({
            target: event.currentTarget as HTMLElement,
            message: `Do you want to delete this ${entityName}?`,
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
                    await service.delete(item.id);
                    await loadData();
                    toast.add({ severity: 'success', summary: 'Success', detail: `${entityName} deleted`, life: 3000 });
                } catch (error) {
                    console.log("error", error);
                    toast.add({ severity: 'error', summary: 'Error', detail: `Failed to delete ${entityName}`, life: 3000 });
                } finally {
                    loading.value = false;
                }
            },
            reject: () => {
                toast.add({ severity: 'error', summary: 'Rejected', detail: 'Operation cancelled', life: 3000 });
            }
        });
    };

    // Handle row edit save
    const handleRowEditSave = async (event: { newData: T }) => {
        try {
            loading.value = true;
            await service.update(event.newData);
            await loadData();
            toast.add({ severity: 'success', summary: 'Success', detail: `${entityName} updated`, life: 3000 });
        } catch (error) {
            toast.add({ severity: 'error', summary: 'Error', detail: `Failed to update ${entityName}`, life: 3000 });
            console.error(`Error updating ${entityName}:`, error);
        } finally {
            loading.value = false;
        }
    };

    return {
        items,
        loading,
        filters,
        editingRows,
        loadData,
        handleSubmit,
        handleDelete,
        handleRowEditSave
    };
}