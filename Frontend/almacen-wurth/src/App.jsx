import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { CssBaseline } from "@mui/material";

import Layout from "./Layout";
import PageNotFound from "./lib/PageNotFound";
import PrivateRoute from "./routes/PrivateRoute";

import Empacadores from "./pages/Envasadores";
import LoginScreen from "./pages/Login";
import Productos from "./pages/Articulos";
import AnalisisABC from "./pages/Analisis";
import ProcesoEnvasado from "./pages/ProcesoEnvasado";
import ArticulosEnvasados from "./pages/ArticulosEnvasados";

function App() {
  return (
    <Router>
      <CssBaseline />
      <Routes>

        {/* LOGIN */}
        <Route path="/" element={<LoginScreen />} />

        {/* RUTAS PROTEGIDAS */}
        <Route
          path="/empacadores"
          element={
            <PrivateRoute>
              <Layout currentPageName="Empacadores">
                <Empacadores />
              </Layout>
            </PrivateRoute>
          }
        />

        <Route
          path="/articulos"
          element={
            <PrivateRoute>
              <Layout currentPageName="Articulos">
                <Productos />
              </Layout>
            </PrivateRoute>
          }
        />

        <Route
          path="/analisis"
          element={
            <PrivateRoute>
              <Layout currentPageName="Analisis">
                <AnalisisABC />
              </Layout>
            </PrivateRoute>
          }
        />

          <Route
          path="/proceso-envasado"
          element={
            <PrivateRoute>
              <Layout currentPageName="ProcesoEnvasado">
                <ProcesoEnvasado />
              </Layout>
            </PrivateRoute>
          }
        />

         <Route
          path="/articulos-envasados"
          element={
            <PrivateRoute>
              <Layout currentPageName="ArticulosEnvasados">
                <ArticulosEnvasados />
              </Layout>
            </PrivateRoute>
          }
        />

        <Route path="*" element={<PageNotFound />} />

      </Routes>
    </Router>
  );
}

export default App;