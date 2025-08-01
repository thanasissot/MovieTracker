import { Box, TextField } from "@mui/material";
import { Create, useAutocomplete } from "@refinedev/mui";
import { useForm } from "@refinedev/react-hook-form";
import React from "react";

export const ActorCreate = () => {
  const {
    saveButtonProps,
    refineCore: { formLoading },
    register,
    control,
    formState: { errors },
  } = useForm({});

  const { autocompleteProps: categoryAutocompleteProps } = useAutocomplete({
    resource: "actors",
  });

  return (
      <Create isLoading={formLoading} saveButtonProps={saveButtonProps}
              wrapperProps={{
                style: {
                  maxWidth: '800px',
                  margin: '0 auto',
                  width: '100%',
                  padding: '16px'
                },
              }}
      >
        <Box
            component="form"
            sx={{ display: "flex", flexDirection: "column" }}
            autoComplete="off"
        >
        <TextField
          {...register("firstname", {
            required: "This field is required",
          })}
          error={!!(errors as any)?.firstname}
          helperText={(errors as any)?.firstname?.message}
          margin="normal"
          fullWidth
          InputLabelProps={{ shrink: true }}
          type="text"
          label={"Firstname"}
          name="firstname"
        />
        <TextField
            {...register("lastname", {
              required: "This field is required",
            })}
            error={!!(errors as any)?.lastname}
            helperText={(errors as any)?.lastname?.message}
            margin="normal"
            fullWidth
            InputLabelProps={{ shrink: true }}
            type="text"
            label={"Lastname"}
            name="lastname"
        />
      </Box>
    </Create>
  );
};
