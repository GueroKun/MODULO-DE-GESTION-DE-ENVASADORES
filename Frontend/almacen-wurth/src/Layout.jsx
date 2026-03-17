import React, { useState } from "react";
import { Link } from "react-router-dom";

import {
  Box,
  Drawer,
  AppBar,
  Toolbar,
  Typography,
  IconButton,
  List,
  ListItemButton,
} from "@mui/material";

import {
  Users,
  Package,
  History,
  Star,
  BarChart3,
  Menu,
  PackageOpen,
  X,
  ChevronRight,
} from "lucide-react";

const drawerWidth = 256;

const navItems = [
  { name: "Envasadores", path: "/empacadores", page: "Empacadores", icon: Users },
  { name: "Artículos", path: "/articulos", page: "Articulos", icon: Package },
  { name: "Análisis de Productividad", path: "/analisis", page: "Analisis", icon: BarChart3 },
  { name: "Proceso de Envasado", path: "/proceso-envasado", page: "ProcesoEnvasado", icon: PackageOpen },
  { name: "Artículos Envasados", path: "/articulos-envasados", page: "ArticulosEnvasados", icon: History },
];


export default function Layout({ children, currentPageName }) {
  const [sidebarOpen, setSidebarOpen] = useState(true);

  return (
    <Box sx={{ display: "flex", minHeight: "100vh", bgcolor: "#f8fafc" }}>
      
      {/* HEADER */}
      <AppBar
        position="fixed"
        sx={{
          background: "linear-gradient(to right, #7f1d1d, #b91c1c)",
          zIndex: (theme) => theme.zIndex.drawer + 1,
        }}
      >
        <Toolbar>

          <IconButton
            color="inherit"
            onClick={() => setSidebarOpen(!sidebarOpen)}
            sx={{ mr: 2 }}
          >
            {sidebarOpen ? <X size={20} /> : <Menu size={20} />}
          </IconButton>

          <Package size={22} style={{ marginRight: 8 }} />

          <Typography variant="h6" sx={{ fontWeight: 600 }}>
            SISTEMA DE EMPACADO
          </Typography>
        </Toolbar>
      </AppBar>

      {/* SIDEBAR */}
      <Drawer
        variant="persistent"
        open={sidebarOpen}
        sx={{
          width: drawerWidth,
          "& .MuiDrawer-paper": {
            width: drawerWidth,
            top: 64,
            height: "calc(100% - 64px)",
            boxSizing: "border-box",
            borderRight: "1px solid #e2e8f0",
          },
        }}
      >
        <List sx={{ p: 2 }}>
          {navItems.map((item) => {
            const Icon = item.icon;
            const isActive = currentPageName === item.page;

            return (
              <ListItemButton
                key={item.page}
                component={Link}
                to={item.path}
                sx={{
                  mb: 1,
                  borderRadius: 2,
                  bgcolor: isActive ? "#fef2f2" : "transparent",
                  borderLeft: isActive
                    ? "4px solid #b91c1c"
                    : "4px solid transparent",
                  color: isActive ? "#7f1d1d" : "#475569",
                  "&:hover": {
                    bgcolor: "#f8fafc",
                  },
                }}
              >
                <Box sx={{ display: "flex", alignItems: "center", gap: 2, width: "100%" }}>
                  
                  {/* Icono */}
                  <Icon
                    size={20}
                    color={isActive ? "#b91c1c" : "#94a3b8"}
                  />

                  {/* Texto */}
                  <Typography sx={{ fontSize: 14, fontWeight: 500 }}>
                    {item.name}
                  </Typography>

                  {/* Flecha activa */}
                  {isActive && (
                    <ChevronRight
                      size={16}
                      style={{ marginLeft: "auto", color: "#dc2626" }}
                    />
                  )}
                </Box>
              </ListItemButton>
            );
          })}
        </List>
      </Drawer>

      {/* CONTENIDO */}
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          mt: 8,
          ml: sidebarOpen ? `0` : '-256px',
          transition: "margin 0.5s",
          p: 3,
        }}
      >
        {children}
      </Box>
    </Box>
  );
}
