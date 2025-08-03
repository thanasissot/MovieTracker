import {
  Divider,
  Paper,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableRow,
  Typography,
  Box,
  Card, CardContent,
} from "@mui/material";
import {useShow, useApiUrl} from "@refinedev/core";
import {Show} from "@refinedev/mui";
import {DataGrid, GridColDef} from "@mui/x-data-grid";
import React from "react";
import axios from "axios";
import { useState, useEffect } from "react";
import { Movie, Actor } from "../../components/model/all";

export const ActorShow = () => {
  const apiUrl = useApiUrl();
  const [searchText] = useState<string>("");
  const [, setMovieOptions] = useState<Movie[]>([]);


  const { query } = useShow({});
  const { data, isLoading } = query;
  const record = data?.data as Actor | undefined;
  const [movies, setMovies] = useState<Movie[]>([]);

  useEffect(() => {
    if (record?.movies) {
      setMovies(record.movies || []);
    }
  }, [record]);

  const filterMovieOptions = (allMovies: Movie[]): Movie[] => {
    if (!movies.length) return allMovies;
    return allMovies.filter(movie =>
        !movies.some(existingMovie => existingMovie.id === movie.id)
    );
  };

  // Load default movies for dropdown
  const fetchDefaultMovies = async () => {
    try {
      const response = await axios.get(`${apiUrl}/movies`, {
        params: {
          _sort: 'title',
          _order: 'asc',
          _start: 0,
          _end: 20
        }
      });
      setMovieOptions(filterMovieOptions(response.data));
    } catch (error) {
      console.error("Error fetching default movies:", error);
    }
  };

  useEffect(() => {
    fetchDefaultMovies().then(() => {
      // nothing
    });
    // eslint-disable-next-line
  }, [movies]);

  // Search movies as user types
  useEffect(() => {
    const fetchMovies = async () => {
      if (searchText.length >= 2) {
        try {
          const response = await axios.get(`${apiUrl}/movies`, {
            params: {
              title_like: searchText,
              _sort: 'title',
              _order: 'asc',
              _start: 0,
              _end: 20
            }
          });
          setMovieOptions(filterMovieOptions(response.data));
        } catch (error) {
          console.error("Error fetching movies:", error);
        }
      }
    };

    const timeoutId = setTimeout(() => {
      fetchMovies();
    }, 300);

    return () => clearTimeout(timeoutId);
  }, [searchText, apiUrl, movies]);


  const movieColumns: GridColDef[] = [
    {
      field: "id",
      headerName: "ID",
      width: 120,
      align: "center",
      headerAlign: "center",
    },
    {
      field: "title",
      headerName: "Movie Title",
      flex: 1,
      minWidth: 350,
      align: "center",
      headerAlign: "center",
    },
    {
      field: "year",
      headerName: "Release Year",
      width: 120,
      align: "center",
      headerAlign: "center",
    }
  ];

  return (
    <Show isLoading={isLoading}
          wrapperProps={{
            style: {
              maxWidth: '1200px',
              margin: '0 auto',
              width: '100%',
              padding: '16px'
            },
          }}
    >
      <Stack gap={1}>
        <Card>
          <CardContent>
        <TableContainer component={Paper}>
          <Table>
            <TableBody>
              <TableRow>
                <TableCell component="th" scope="row" width="50px" sx={{ fontWeight: "bold" }}>ID</TableCell>
                <TableCell component="th" scope="row" width="150px" sx={{ fontWeight: "bold" }}>First Name</TableCell>
                <TableCell component="th" scope="row" width="150px" sx={{ fontWeight: "bold" }}>Last Name</TableCell>
              </TableRow>
              <TableRow>
                <TableCell>{record?.id}</TableCell>
                <TableCell>{record?.firstname}</TableCell>
                <TableCell>{record?.lastname}</TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>
      </CardContent>
    </Card>

        <Divider sx={{my: 2}}/>

        <Card>
          <CardContent>
        <Typography variant="h6">Movies</Typography>
        {movies?.length > 0 ? (
            <Box sx={{maXwidth: '1200px'}}>
              <DataGrid
                  rows={movies}
                  columns={movieColumns}
                  disableRowSelectionOnClick
                  initialState={{
                    pagination: {
                      paginationModel: {pageSize: 20},
                    },
                  }}
              />
            </Box>
        ) : (
            <Typography variant="body2" color="text.secondary">
              This actor has not been cast in any movies.
            </Typography>
        )}
      </CardContent>
    </Card>
      </Stack>
    </Show>
  );
};
