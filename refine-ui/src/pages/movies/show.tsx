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
    Button,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    Card, CardContent,
} from "@mui/material";
import {useShow, useApiUrl} from "@refinedev/core";
import {Show} from "@refinedev/mui";
import {DataGrid, GridColDef, GridRenderCellParams} from "@mui/x-data-grid";
import {Delete as DeleteIcon} from "@mui/icons-material";
import React, {useState, useEffect} from "react";
import axios from "axios";
import {type Movie, type Genre, type Actor} from "../../components/model/all";

export const MovieShow = () => {
    const apiUrl = useApiUrl();
    const [genreOptions, setGenreOptions] = useState<Genre[]>([]);
    const [selectedGenreId, setSelectedGenreId] = useState<number | "">("");
    const [genres, setGenres] = useState<Genre[]>([]);
    const [allGenres, setAllGenres] = useState<Genre[]>([]);

    const {query} = useShow({});
    const {data, isLoading} = query;
    const record = data?.data as Movie | undefined;

    // Fetch all genres once
    useEffect(() => {
        if (record) {
            fetchAllGenres().then(r => console.log("Genres fetched"));
        }
    }, [record]);

    const fetchAllGenres = async () => {
        try {
            const response = await axios.get(`${apiUrl}/genres`, {
                params: {
                    _sort: 'name',
                    _order: 'asc'
                }
            });
            const tempGenres = response.data as Genre[];
            setAllGenres(tempGenres);
            if (record?.genreIds && record.genreIds.length > 0 && tempGenres.length > 0) {
                const movieGenres = record.genreIds
                    .map(id => tempGenres.find((genre: Genre) => genre.id === id))
                    .filter(Boolean); // Remove any undefined entries

                setGenres(movieGenres ? movieGenres as Genre[] : []);
                // Update available genre options
                updateGenreOptions(record.genreIds, tempGenres);
            } else if (tempGenres.length > 0) {
                setGenres([]);
                setGenreOptions(tempGenres);
            }

        } catch (error) {
            console.error("Error fetching all genres:", error);
        }
    };

    // Helper function to update available genre options
    const updateGenreOptions = (usedGenreIds: number[], tempGenres: Genre[]) => {
        const filteredOptions = tempGenres.filter(
            genre => !usedGenreIds.includes(genre.id)
        );
        setGenreOptions(filteredOptions);
    };

    const handleRemoveGenre = async (genreId: number) => {
        if (!record?.id) return;

        try {
            await axios.delete(`${apiUrl}/movies/${record.id}/genres`, {
                data: [genreId]
            });

            // Update local state
            const updatedGenres = genres.filter(genre => genre.id !== genreId);
            setGenres(updatedGenres);

            // Update available options
            updateGenreOptions(updatedGenres.map(g => g.id), allGenres);
        } catch (error) {
            console.error("Error removing genre:", error);
        }
    };

    const handleAddGenre = async () => {
        if (selectedGenreId === "" || !record?.id) return;

        try {
            await axios.post(`${apiUrl}/movies/${record.id}/genres`, [selectedGenreId]);

            // Find the selected genre object
            const selectedGenre = genreOptions.find(g => g.id === selectedGenreId);

            if (selectedGenre) {
                const updatedGenres = [...genres, selectedGenre];
                setGenres(updatedGenres);

                // Update available options
                updateGenreOptions(updatedGenres.map(g => g.id), genreOptions);
            }

            // Reset selection
            setSelectedGenreId("");
        } catch (error) {
            console.error("Error adding genre:", error);
        }
    };

    const genreColumns: GridColDef[] = [
        {
            field: "id",
            headerName: "ID",
            width: 60,
            align: "center",
            headerAlign: "center",
        },
        {
            field: "name",
            headerName: "Genre Name",
            minWidth: 200,
            flex: 1,
            renderHeader: (params) => (
                <div style={{paddingLeft: '22px', display: 'flex', alignItems: 'center', height: '100%'}}>
                    {params.colDef.headerName}
                </div>
            ),
            renderCell: (params) => (
                <div style={{
                    width: '100%',
                    fontWeight: 500,
                    fontSize: '0.875rem',
                    padding: '0px 0 6px 22px'
                }}>
                    {params.value}
                </div>
            ),
        },
        {
            field: "actions",
            headerName: "Actions",
            sortable: false,
            minWidth: 150,
            flex: 1,
            align: "center",
            headerAlign: "center",
            renderCell: (params: GridRenderCellParams) => (
                <Button
                    color="error"
                    onClick={() => handleRemoveGenre(params.row.id)}
                    startIcon={<DeleteIcon/>}
                    size="small"
                >
                    Remove
                </Button>
            ),
        },
    ];

    // Add columns definition for actors
    const actorColumns: GridColDef[] = [
        {
            field: "id",
            headerName: "ID",
            width: 60,
            align: "center",
            headerAlign: "center",
        },
        {
            field: "firstname",
            headerName: "First Name",
            renderHeader: (params) => (
                <div style={{paddingLeft: '22px', display: 'flex', alignItems: 'center', height: '100%'}}>
                    {params.colDef.headerName}
                </div>
            ),
            minWidth: 150,
            flex: 1,
            renderCell: (params) => (
                <div style={{
                    width: '100%',
                    fontWeight: 500,
                    fontSize: '0.875rem',
                    padding: '0px 0 6px 22px'
                }}>
                    {params.value}
                </div>
            ),
        },
        {
            field: "lastname",
            headerName: "Last Name",
            minWidth: 150,
            flex: 1,
            renderHeader: (params) => (
                <div style={{paddingLeft: '22px', display: 'flex', alignItems: 'center', height: '100%'}}>
                    {params.colDef.headerName}
                </div>
            ),
            renderCell: (params) => (
                <div style={{
                    width: '100%',
                    fontWeight: 500,
                    fontSize: '0.875rem',
                    padding: '0px 0 6px 22px'
                }}>
                    {params.value}
                </div>
            ),
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
                                        <TableCell component="th" scope="row" width="50px"
                                                   sx={{fontWeight: "bold"}}>ID</TableCell>
                                        <TableCell component="th" scope="row" width="150px"
                                                   sx={{fontWeight: "bold"}}>Title</TableCell>
                                        <TableCell component="th" scope="row" width="150px" sx={{fontWeight: "bold"}}>Release
                                            Year</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>{record?.id}</TableCell>
                                        <TableCell>{record?.title}</TableCell>
                                        <TableCell>{record?.year}</TableCell>
                                    </TableRow>
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </CardContent>
                </Card>

                <Divider sx={{my: 2}}/>
                <Card>
                    <CardContent>
                        <Typography variant="h6" sx={{mb: 2}}>Manage Genres</Typography>

                        {/* Search and Add Genres */}
                        <Box sx={{display: "flex", mb: 2, gap: 1}}>
                            <FormControl fullWidth variant="outlined">
                                <InputLabel id="genre-select-label">Select Genre</InputLabel>
                                <Select
                                    labelId="genre-select-label"
                                    value={selectedGenreId}
                                    onChange={(e) => setSelectedGenreId(e.target.value as number)}
                                    label="Select Genre"
                                >
                                    <MenuItem value="">
                                        <em>Select a genre</em>
                                    </MenuItem>
                                    {genreOptions.map((genre) => (
                                        <MenuItem key={genre.id} value={genre.id}>
                                            {genre.name}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </FormControl>
                            <Button
                                variant="contained"
                                onClick={handleAddGenre}
                                disabled={selectedGenreId === ""}
                                sx={{minWidth: 100}}
                            >
                                Add Genre
                            </Button>
                        </Box>

                        <Typography variant="h6">Genres</Typography>
                        {genres?.length > 0 ? (
                            <Box sx={{height: 400, maxWidth: '1200px'}}>
                                <DataGrid
                                    rows={genres}
                                    columns={genreColumns}
                                    disableRowSelectionOnClick
                                    autoHeight
                                    pageSizeOptions={[10, 15, 25]}
                                    initialState={{
                                        pagination: {
                                            paginationModel: {pageSize: 10},
                                        },
                                    }}
                                />
                            </Box>
                        ) : (
                            <Typography variant="body2" color="text.secondary">
                                This movie has not been assigned any genres.
                            </Typography>
                        )}
                    </CardContent>
                </Card>

                <Card>
                    <CardContent>
                        <Divider sx={{my: 2}}/>
                        <Typography variant="h6">Actors</Typography>
                        {(record?.actors && record.actors.length > 0) ? (
                            <Box sx={{height: 400, maxWidth: '1200px'}}>
                                <DataGrid
                                    rows={record?.actors}
                                    columns={actorColumns}
                                    disableRowSelectionOnClick
                                    autoHeight
                                    pageSizeOptions={[10, 15, 25]}
                                    initialState={{
                                        pagination: {
                                            paginationModel: {pageSize: 10},
                                        },
                                    }}
                                />
                            </Box>
                        ) : (
                            <Typography variant="body2" color="text.secondary">
                                This movie has no actors assigned.
                            </Typography>
                        )}
                    </CardContent>
                </Card>
            </Stack>
        </Show>
    );
};
