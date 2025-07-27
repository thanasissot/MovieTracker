import {
  DeleteButton,
  EditButton,
  List,
  ShowButton,
  useDataGrid,
} from "@refinedev/mui";
import { DataGrid, type GridColDef } from "@mui/x-data-grid";
import { useMany, useApiUrl } from "@refinedev/core";
import { Typography, Box, FormControl, InputLabel, Select, MenuItem, Button } from "@mui/material";
import React, { useState, useEffect } from "react";
import axios from "axios";
import { type Genre } from "../../components/model/all";

export const MovieList = () => {
    const apiUrl = useApiUrl();
    const [genres, setGenres] = useState<Genre[]>([]);
    const [selectedGenreId, setSelectedGenreId] = useState<number | "">("");

    // Fetch all genres once when component mounts
    useEffect(() => {
        const fetchGenres = async () => {
            try {
                const response = await axios.get(`${apiUrl}/genres`, {
                    params: {
                        _sort: 'name',
                        _order: 'asc'
                    }
                });
                setGenres(response.data);
            } catch (error) {
                console.error("Error fetching genres:", error);
            }
        };

        fetchGenres();
    }, [apiUrl]);

    // Setup filters based on selected genre
    const { dataGridProps } = useDataGrid({
        filters: {
            permanent: [
                {
                    field: "genreId",
                    operator: "eq",
                    value: selectedGenreId || undefined,
                },
            ],
        },
    });

    // Clear genre filter
    const handleClearFilter = () => {
        setSelectedGenreId("");
    };

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
    []
  );

  return (
    <List>
        <Box sx={{ display: "flex", mb: 2, gap: 1 }}>
            <FormControl variant="outlined" sx={{ minWidth: 200 }}>
                <InputLabel id="genre-filter-label">Filter by Genre</InputLabel>
                <Select
                    labelId="genre-filter-label"
                    value={selectedGenreId}
                    onChange={(e) => setSelectedGenreId(e.target.value as number)}
                    label="Filter by Genre"
                >
                    <MenuItem value="">
                        <em>All Genres</em>
                    </MenuItem>
                    {genres.map((genre) => (
                        <MenuItem key={genre.id} value={genre.id}>
                            {genre.name}
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>
            {selectedGenreId && (
                <Button
                    variant="outlined"
                    onClick={handleClearFilter}
                    sx={{ height: 56 }}
                >
                    Clear Filter
                </Button>
            )}
        </Box>
      <DataGrid {...dataGridProps} columns={columns} />
    </List>
  );
};
