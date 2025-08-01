import { DataGrid, type GridColDef } from "@mui/x-data-grid";
import {
  DeleteButton,
  EditButton,
  List,
  ShowButton,
  useDataGrid,
} from "@refinedev/mui";
import React from "react";
import { Box } from "@mui/material";

export const GenreList = () => {
  const { dataGridProps } = useDataGrid({});

  const columns = React.useMemo<GridColDef[]>(
      () => [
        {
          field: "id",
          headerName: "ID",
          type: "number",
          width: 70,
          align: "center",
          headerAlign: "center",
        },
        {
          field: "name",
          headerName: "Genre",
          flex: 2,
          minWidth: 350, // Fixed width instead of flex
          renderHeader: (params) => (
              <div style={{ paddingLeft: '26px', display: 'flex', alignItems: 'center', height: '100%' }}>
                {params.colDef.headerName}
              </div>
          ),
          renderCell: (params) => (
              <div
                  style={{
                    lineHeight: 2.8,
                    width: '100%',
                    fontWeight: 500,
                    fontSize: '0.875rem',
                    whiteSpace: 'normal',
                    wordWrap: 'break-word',
                    display: '-webkit-box',
                    overflow: 'hidden',
                    WebkitLineClamp: 2,
                    WebkitBoxOrient: 'vertical',
                    padding: '6px 0 0 26px'
                  }}
              >
                {params.value}
              </div>
          ),
        },
        {
          field: "actions",
          headerName: "Actions",
          minWidth: 100,
          flex: 1,
          align: "center",
          headerAlign: "center",
          sortable: false,
          renderCell: function render({ row }) {
            return (
                <Box display="flex" gap={1} justifyContent="center">
                  <EditButton hideText recordItemId={row.id} />
                  <ShowButton hideText recordItemId={row.id} />
                  <DeleteButton hideText recordItemId={row.id} />
                </Box>
            );
          },
        },
      ],
      []
  );

  return (
      <List
          wrapperProps={{
            style: {
              maxWidth: '800px', // Constrain the width
              mx: 'auto',       // Center horizontally
              width: '100%',    // Take full width up to maxWidth
              p: 2             // Add some padding
            },
          }}
      >
        <DataGrid {...dataGridProps} columns={columns} />
      </List>
  );
};
