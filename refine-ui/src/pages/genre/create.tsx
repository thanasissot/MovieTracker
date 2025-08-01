import { Box, TextField, Card, CardContent } from "@mui/material";
import { Create, useAutocomplete } from "@refinedev/mui";
import { useForm } from "@refinedev/react-hook-form";
import React from "react";

export const GenreCreate = () => {
  const {
    saveButtonProps,
    refineCore: { formLoading },
    register,
    formState: { errors },
  } = useForm({});

  return (
      <Create isLoading={formLoading} saveButtonProps={saveButtonProps}>
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
      </Create>
  );
};