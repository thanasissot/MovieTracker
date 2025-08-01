import { Box, Card, CardContent, TextField } from "@mui/material";
import { Edit } from "@refinedev/mui";
import { useForm } from "@refinedev/react-hook-form";
import React from "react";

export const GenreEdit = () => {
    const {
        saveButtonProps,
        refineCore: { formLoading },
        register,
        formState: { errors },
    } = useForm({});

    return (
        <Edit isLoading={formLoading} saveButtonProps={saveButtonProps}>
            <Card>
                <CardContent>
                    <Box
                        component="form"
                        sx={{ display: "flex", flexDirection: "column" }}
                        autoComplete="off"
                    >
                        <TextField
                            {...register("name", {
                                required: "This field is required",
                                minLength: {
                                    value: 2,
                                    message: "Genre name must be at least 2 characters"
                                }
                            })}
                            error={!!errors?.name}
                            helperText={errors?.name?.message?.toString()}
                            margin="normal"
                            fullWidth
                            InputLabelProps={{ shrink: true }}
                            type="text"
                            label="Genre"
                            name="name"
                            variant="outlined"
                        />
                    </Box>
                </CardContent>
            </Card>
        </Edit>
    );
};