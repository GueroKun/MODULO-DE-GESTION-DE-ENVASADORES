import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Box,
  Card,
  CardContent,
  Typography,
  TextField,
  Button,
  InputAdornment,
  IconButton,
} from "@mui/material";
import Person from "@mui/icons-material/Person";
import LockIcon from "@mui/icons-material/Lock";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import ArrowForwardIcon from "@mui/icons-material/ArrowForward";

export default function LoginScreen() {
  const navigate = useNavigate();

  const [nombre, setNombre] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);

  const login = async (nombreUsuario, pass) => {
    const res = await fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        nombre: nombreUsuario,
        password: pass,
      }),
    });

    if (!res.ok) {
      let msg = "Credenciales incorrectas";
      try {
        const err = await res.json();
        if (err?.message) msg = err.message;
      } catch {}
      throw new Error(msg);
    }

    return res.json();
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const data = await login(nombre, password);

      localStorage.setItem("token", data.token ?? "");
      localStorage.setItem("usuario", data.nombre ?? nombre);
      localStorage.setItem("rol", data.rol ?? "");

      const rolNormalizado = String(data.rol || "")
        .toUpperCase()
        .replace("ROLE_", "");

      if (rolNormalizado === "ADMIN") {
        navigate("/Empacadores", { replace: true });
      } else {
        navigate("/", { replace: true });
      }
    } catch (err) {
      console.error("Error login:", err?.message);
      alert("Usuario o contraseña incorrectos");
    } finally {
      setLoading(false);
    }
  };

  const features = [
    {
      number: "1",
      title: "Gestión de Envasadores",
      description: "Control de personal y asignación de tareas",
    },
    {
      number: "2",
      title: "Proceso en Tiempo Real",
      description: "Monitoreo continuo del trabajo de envasado",
    },
    {
      number: "3",
      title: "Análisis de Productividad",
      description: "Reportes y métricas de rendimiento",
    },
  ];

  return (
    <Box
      sx={{
        minHeight: "100vh",
        display: "flex",
        flexDirection: { xs: "column", md: "row", margin: "0" },
      }}
    >
      {/* Panel izquierdo - Rojo */}
      <Box
        sx={{
          flex: 1,
          background: "linear-gradient(135deg, #b91c1c 0%, #991b1b 100%)",
          display: "flex",
          flexDirection: "column",
          justifyContent: "center",
          p: { xs: 4, md: 6, lg: 8 },
          position: "relative",
          overflow: "hidden",
        }}
      >
        {/* Círculo decorativo */}
        <Box
          sx={{
            position: "absolute",
            top: "-10%",
            right: "-20%",
            width: "60%",
            height: "80%",
            background:
              "radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 70%)",
            borderRadius: "50%",
          }}
        />

        {/* Logo */}
        <Box sx={{ mb: 4, display: "flex", alignItems: "center", gap: 1 }}>
          <Box
            component="span"
            sx={{
              width: 24,
              height: 24,
              backgroundColor: "white",
              borderRadius: "4px",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
            }}
          >
            <Typography
              sx={{
                color: "#b91c1c",
                fontWeight: "bold",
                fontSize: "14px",
              }}
            >
              W
            </Typography>
          </Box>
          <Typography
            sx={{
              color: "white",
              fontWeight: 600,
              fontSize: "14px",
              letterSpacing: "0.5px",
            }}
          >
            WÜRTH
          </Typography>
        </Box>

        {/* Título principal */}
        <Typography
          variant="h3"
          sx={{
            color: "white",
            fontWeight: 700,
            mb: 2,
            fontSize: { xs: "2rem", md: "2.5rem", lg: "3rem" },
            lineHeight: 1.2,
          }}
        >
          Sistema de Gestión de Envasado
        </Typography>

        <Typography
          sx={{
            color: "rgba(255,255,255,0.9)",
            fontSize: "1.1rem",
            mb: 6,
            maxWidth: "400px",
          }}
        >
          Control total de tu proceso productivo
        </Typography>

        {/* Características */}
        <Box sx={{ display: "flex", flexDirection: "column", gap: 3 }}>
          {features.map((feature) => (
            <Box key={feature.number} sx={{ display: "flex", gap: 2 }}>
              <Box
                sx={{
                  width: 32,
                  height: 32,
                  borderRadius: "8px",
                  backgroundColor: "rgba(255,255,255,0.2)",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  flexShrink: 0,
                }}
              >
                <Typography
                  sx={{
                    color: "white",
                    fontWeight: 600,
                    fontSize: "14px",
                  }}
                >
                  {feature.number}
                </Typography>
              </Box>
              <Box>
                <Typography
                  sx={{
                    color: "white",
                    fontWeight: 600,
                    fontSize: "1rem",
                    mb: 0.5,
                  }}
                >
                  {feature.title}
                </Typography>
                <Typography
                  sx={{
                    color: "rgba(255,255,255,0.7)",
                    fontSize: "0.9rem",
                  }}
                >
                  {feature.description}
                </Typography>
              </Box>
            </Box>
          ))}
        </Box>
      </Box>

      {/* Panel derecho - Formulario */}
      <Box
        sx={{
          flex: 1,
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          backgroundColor: "#f8fafc",
          p: { xs: 3, md: 4 },
        }}
      >
        <Card
          sx={{
            width: "100%",
            maxWidth: 420,
            borderRadius: 2,
            boxShadow: "0 4px 20px rgba(0,0,0,0.08)",
            border: "1px solid rgba(0,0,0,0.05)",
          }}
        >
          <CardContent sx={{ p: 4 }}>
            <Typography
              variant="h4"
              sx={{
                fontWeight: 700,
                color: "#1e293b",
                mb: 1,
              }}
            >
              Bienvenido
            </Typography>

            <Typography
              sx={{
                color: "#64748b",
                mb: 4,
                fontSize: "0.95rem",
              }}
            >
              Ingresa tus credenciales para acceder al sistema
            </Typography>

            <Box
              component="form"
              onSubmit={handleSubmit}
              sx={{ display: "flex", flexDirection: "column", gap: 2.5 }}
            >
              <Box>
                <Typography
                  sx={{
                    fontSize: "0.875rem",
                    fontWeight: 500,
                    color: "#374151",
                    mb: 0.5,
                  }}
                >
                  Correo Electrónico
                </Typography>
                <TextField
                  type="text"
                  value={nombre}
                  onChange={(e) => setNombre(e.target.value)}
                  fullWidth
                  required
                  disabled={loading}
                  placeholder="usuario"
                  variant="outlined"
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <Person sx={{ color: "#9ca3af", fontSize: 20 }} />
                      </InputAdornment>
                    ),
                  }}
                  sx={{
                    "& .MuiOutlinedInput-root": {
                      backgroundColor: "#f9fafb",
                      "& fieldset": {
                        borderColor: "#e5e7eb",
                      },
                      "&:hover fieldset": {
                        borderColor: "#d1d5db",
                      },
                      "&.Mui-focused fieldset": {
                        borderColor: "#b91c1c",
                      },
                    },
                  }}
                />
              </Box>

              <Box>
                <Typography
                  sx={{
                    fontSize: "0.875rem",
                    fontWeight: 500,
                    color: "#374151",
                    mb: 0.5,
                  }}
                >
                  Contraseña
                </Typography>
                <TextField
                  type={showPassword ? "text" : "password"}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  fullWidth
                  required
                  disabled={loading}
                  placeholder="••••••••"
                  variant="outlined"
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <LockIcon sx={{ color: "#9ca3af", fontSize: 20 }} />
                      </InputAdornment>
                    ),
                    endAdornment: (
                      <InputAdornment position="end">
                        <IconButton
                          onClick={() => setShowPassword((p) => !p)}
                          edge="end"
                          disabled={loading}
                          size="small"
                        >
                          {showPassword ? (
                            <VisibilityOff sx={{ color: "#9ca3af" }} />
                          ) : (
                            <Visibility sx={{ color: "#9ca3af" }} />
                          )}
                        </IconButton>
                      </InputAdornment>
                    ),
                  }}
                  sx={{
                    "& .MuiOutlinedInput-root": {
                      backgroundColor: "#f9fafb",
                      "& fieldset": {
                        borderColor: "#e5e7eb",
                      },
                      "&:hover fieldset": {
                        borderColor: "#d1d5db",
                      },
                      "&.Mui-focused fieldset": {
                        borderColor: "#b91c1c",
                      },
                    },
                  }}
                />
              </Box>
              <Button
                type="submit"
                variant="contained"
                fullWidth
                disabled={loading}
                endIcon={<ArrowForwardIcon />}
                sx={{
                  mt: 1,
                  py: 1.5,
                  backgroundColor: "#b91c1c",
                  textTransform: "none",
                  fontSize: "1rem",
                  fontWeight: 500,
                  borderRadius: "8px",
                  boxShadow: "none",
                  "&:hover": {
                    backgroundColor: "#991b1b",
                    boxShadow: "none",
                  },
                }}
              >
                {loading ? "Entrando..." : "Iniciar Sesión"}
              </Button>

           
            </Box>
          </CardContent>
        </Card>
      </Box>
    </Box>
  );
}