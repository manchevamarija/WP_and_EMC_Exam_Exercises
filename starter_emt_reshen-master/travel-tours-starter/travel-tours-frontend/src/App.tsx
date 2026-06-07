import './App.css';
import { BrowserRouter, Outlet, Route, Routes } from 'react-router';
import Layout from './ui/components/layout/Layout/Layout.tsx';
import HomePage from './ui/pages/home/HomePage/HomePage.tsx';
import RegisterPage from './ui/pages/auth/RegisterPage/RegisterPage.tsx';
import LoginPage from './ui/pages/auth/LoginPage/LoginPage.tsx';
import ProtectedRoute from './ui/components/routing/ProtectedRoute/ProtectedRoute.tsx';
import ToursProvider from './providers/toursProvider.tsx';
import ToursPage from './ui/pages/tour/ToursPage/ToursPage.tsx';
import TourDetailsPage from './ui/pages/tour/TourDetailsPage/TourDetailsPage.tsx';
import ReservationsProvider from './providers/reservationsProvider.tsx';
import ReservationsPage from './ui/pages/reservation/ReservationsPage/ReservationsPage.tsx';
import NotificationsPage from './ui/pages/notification/NotificationsPage/NotificationsPage.tsx';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path='/register' element={<RegisterPage/>}/>
        <Route path='/login' element={<LoginPage/>}/>
        <Route path='/' element={<Layout/>}>
          <Route index element={<HomePage/>}/>
          <Route element={<ProtectedRoute/>}>
            <Route element={<ToursProvider><Outlet/></ToursProvider>}>
              <Route path='tours' element={<ToursPage/>}/>
              <Route path='tours/:id' element={<TourDetailsPage/>}/>
            </Route>
          </Route>
          <Route element={<ProtectedRoute role='ROLE_CUSTOMER'/>}>
            <Route element={<ReservationsProvider><Outlet/></ReservationsProvider>}>
              <Route path='reservations' element={<ReservationsPage/>}/>
            </Route>
            <Route path='notifications' element={<NotificationsPage/>}/>
          </Route>
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
