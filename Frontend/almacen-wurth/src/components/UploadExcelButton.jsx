import { Button } from "@mui/material";
import { FileSpreadsheet } from "lucide-react";

export default function UploadExcelButton({ children, sx, ...props }) {
  return (
    <Button
      {...props}
      startIcon={<FileSpreadsheet size={16} />}
      sx={{
        textTransform: "none",
        border: "1px solid #bbf7d0",
        color: "#15803d",
        bgcolor: "#f0fdf4",
        "&:hover": {
          bgcolor: "#dcfce7",
          borderColor: "#86efac",
        },
        ...sx,
      }}
    >
      {children}
    </Button>
  );
}