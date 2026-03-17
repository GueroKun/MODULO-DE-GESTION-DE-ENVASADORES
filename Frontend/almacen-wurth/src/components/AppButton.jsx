import { Button } from "@mui/material";
import { Plus } from "lucide-react";

export default function AppButton({ children, sx, ...props }) {
  return (
    <Button
      {...props}
      startIcon={<Plus size={16} />}
      sx={{
        textTransform: "none",
        backgroundColor: "grey.800",
            "&:hover": { backgroundColor: "grey.900" },
        ...sx,
      }}
    >
      {children}
    </Button>
  );
}