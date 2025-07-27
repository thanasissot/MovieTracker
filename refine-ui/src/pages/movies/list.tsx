import { DataGrid, type GridColDef } from "@mui/x-data-grid";
import { useMany } from "@refinedev/core";
import {
  DateField,
  DeleteButton,
  EditButton,
  List,
  ShowButton,
  useDataGrid,
} from "@refinedev/mui";
import { Typography } from "@mui/material";
import React from "react";

export const MovieList = () => {
  const { dataGridProps } = useDataGrid({});

    const { data: movieData, isLoading: movieIsLoading } = useMany({
        resource: "movies",
        ids:
            dataGridProps?.rows
                ?.map((item: any) => item?.id)
                .filter(Boolean) ?? [],
        queryOptions: {
            enabled: !!dataGridProps?.rows,
        },
    });

  const columns = React.useMemo<GridColDef[]>(
    () => [
      {
        field: "id",
        headerName: "ID",
        type: "number",
        minWidth: 50,
        display: "flex",
        align: "left",
        headerAlign: "left",
      },
      {
        field: "title",
        headerName: "Title",
        minWidth: 200,
        display: "flex",
      },
      {
        field: "year",
        headerName: "Year",
        minWidth: 200,
        display: "flex",
      },
      {
        field: "actions",
        headerName: "Actions",
        align: "right",
        headerAlign: "right",
        minWidth: 120,
        sortable: false,
        display: "flex",
        renderCell: function render({ row }) {
          return (
            <>
              <EditButton hideText recordItemId={row.id} />
              <ShowButton hideText recordItemId={row.id} />
              <DeleteButton hideText recordItemId={row.id} />
            </>
          );
        },
      },
    ],
    [movieData, movieIsLoading]
  );

  return (
    <List>
      <DataGrid {...dataGridProps} columns={columns} />
    </List>
  );
};
