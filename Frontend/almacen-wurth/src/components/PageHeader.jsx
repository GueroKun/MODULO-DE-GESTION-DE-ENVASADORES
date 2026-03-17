import React from "react";
import { Box, Typography, IconButton, Stack } from "@mui/material";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import { Link } from "react-router-dom";


export default function PageHeader({ title, backPage, children }) {
  return (
    <Box
      sx={{
        background: "linear-gradient(to right, #7f1d1d, #b91c1c)", 
        borderRadius: 3,
        boxShadow: 3,
        mb: 3,
        p:1,
        overflow: "hidden",
      }}
    >
      <Box sx={{ px: 3, py: 2 }}>
        <Stack direction="row" alignItems="center" justifyContent="space-between">
          
          {/* IZQUIERDA */}
          <Stack direction="row" alignItems="center" spacing={2}>
            
            {backPage && (
              <IconButton
                component={Link}
                to={createPageUrl(backPage)}
                sx={{
                  color: "white",
                  "&:hover": {
                    backgroundColor: "rgba(127, 29, 29, 0.5)",
                  },
                }}
              >
                <ArrowBackIcon />
              </IconButton>
            )}

            <Typography
              variant="h6"
              sx={{
                fontWeight: "bold",
                color: "white",
                letterSpacing: 0.5,
              }}
            >
              {title}
            </Typography>
          </Stack>

          {/* DERECHA */}
          <Stack direction="row" spacing={1}>
            {children}
          </Stack>
        </Stack>
      </Box>
    </Box>
  );
}
