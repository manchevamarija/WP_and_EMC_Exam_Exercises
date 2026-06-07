import useAuth from "../../../../hooks/useAuth";
import { Navigate, Outlet } from "react-router";

interface ProtectedRouteProps {
  role?: string;
}

const ProtectedRoute = ({ role }: ProtectedRouteProps) => {
  const { user } = useAuth();

  if (user === null)
    return <Navigate to="/login" replace />;

  if (role && !user.roles.includes(role))
    return <Navigate to="/login" replace />;

  return <Outlet />;
};

export default ProtectedRoute;
