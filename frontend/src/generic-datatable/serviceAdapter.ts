// frontend/src/generic-datatable/serviceAdapter.ts
import type { DataService, BaseEntity } from './dataTable';

export function createServiceAdapter<T extends BaseEntity>(mappers: {
    getAll: () => Promise<T[]>;
    create: (data: any) => Promise<T>;
    update: (data: T) => Promise<T>;
    delete: (id: number) => Promise<void>;
}): DataService<T> {
    return {
        getAll: mappers.getAll,
        create: mappers.create,
        update: mappers.update,
        delete: mappers.delete
    };
}