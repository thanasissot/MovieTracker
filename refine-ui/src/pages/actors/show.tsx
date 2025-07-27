import { Stack, Typography } from "@mui/material";
import { useOne, useShow } from "@refinedev/core";
import {
  DateField,
  MarkdownField,
  NumberField,
  Show,
  TextFieldComponent as TextField,
} from "@refinedev/mui";

export const ActorShow = () => {
  const { query } = useShow({});

  const { data, isLoading } = query;

  const record = data?.data;

  return (
    <Show isLoading={isLoading}>
      <Stack gap={1}>
        <Typography variant="body1" fontWeight="bold">
          {"ID"}
        </Typography>
        <TextField value={record?.id} />

        <Typography variant="body1" fontWeight="bold">
          {"Title"}
        </Typography>
        <TextField value={record?.firstname} />

        <Typography variant="body1" fontWeight="bold">
          {"Content"}
        </Typography>
        <MarkdownField value={record?.lastname} />
      </Stack>
    </Show>
  );
};
