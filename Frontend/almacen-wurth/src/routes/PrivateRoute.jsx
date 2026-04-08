import { Navigate } from "react-router-dom";

function PrivateRoute({ children, roles }) {
  const token = localStorage.getItem("token");
  const rol = localStorage.getItem("rol");

  if (!token) {
    return <Navigate to="/" replace />;
  }

  // Validar rol si la ruta lo requiere
  if (roles && !roles.includes(rol)) {
    return <Navigate to="/no-autorizado" replace />;
  }

  return children;
}

export default PrivateRoute;