import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { CssBaseline } from "@mui/material";

import Layout from "./Layout";
import PageNotFound from "./lib/PageNotFound";
import PrivateRoute from "./routes/PrivateRoute";

import Empacadores from "./pages/Envasadores";
import LoginScreen from "./pages/Login";
import Productos from "./pages/Articulos";
import ProcesoEnvasado from "./pages/ProcesoEnvasado";
import ArticulosEnvasados from "./pages/ArticulosEnvasados";
import { AlertProvider } from "./components/AlertProvider";
import Montacargas from "./pages/Montacargas";
import GestionTarimas from "./pages/GestionTarimas";
import Cajas from "./pages/Cajas";


function App() {
  return (
    <AlertProvider>
      <Router>
        <CssBaseline />
        <Routes>

          {/* LOGIN */}
          <Route path="/" element={<LoginScreen />} />

          {/* RUTAS PROTEGIDAS */}
          <Route
            path="/empacadores"
            element={
              <PrivateRoute roles={["ADMIN", "MONTACARGAS"]}>
                <Layout currentPageName="Empacadores">
                  <Empacadores />
                </Layout>
              </PrivateRoute>
            }
          />

          <Route
            path="/articulos"
            element={
              <PrivateRoute roles={["ADMIN", "MONTACARGAS"]}>
                <Layout currentPageName="Articulos">
                  <Productos />
                </Layout>
              </PrivateRoute>
            }
          />

          <Route
            path="/proceso-envasado"
            element={
              <PrivateRoute roles={["ADMIN", "MONTACARGAS"]}>
                <Layout currentPageName="ProcesoEnvasado">
                  <ProcesoEnvasado />
                </Layout>
              </PrivateRoute>
            }
          />

          <Route
            path="/articulos-envasados"
            element={
              <PrivateRoute roles={["ADMIN", "MONTACARGAS"]}>
                <Layout currentPageName="ArticulosEnvasados">
                  <ArticulosEnvasados />
                </Layout>
              </PrivateRoute>
            }
          />

          <Route
            path="/montacargas"
            element={
              <PrivateRoute roles={["ADMIN"]}>
                <Layout currentPageName="Montacargas">
                  <Montacargas />
                </Layout>
              </PrivateRoute>
            }
          />

          <Route
            path="/control-inventario"
            element={
              <PrivateRoute roles={["MONTACARGAS"]}>
                <Layout currentPageName="ControlInventario">
                  <GestionTarimas />
                </Layout>
              </PrivateRoute>
            }
          />

          <Route
            path="/cajas"
            element={
              <PrivateRoute roles={["ADMIN"]}>
                <Layout currentPageName="Cajas">
                  <Cajas />
                </Layout>
              </PrivateRoute>
            }
          />


          <Route path="*" element={<PageNotFound />} />

        </Routes>
      </Router>
    </AlertProvider>
  );
}

export default App;