import './App.css';
import { BrowserRouter, Outlet, Route, Routes } from 'react-router';
import Layout from './ui/components/layout/Layout/Layout.tsx';
import HomePage from './ui/pages/home/HomePage/HomePage.tsx';
import RegisterPage from './ui/pages/auth/RegisterPage/RegisterPage.tsx';
import LoginPage from './ui/pages/auth/LoginPage/LoginPage.tsx';
import ProtectedRoute from './ui/components/routing/ProtectedRoute/ProtectedRoute.tsx';
import CoursesProvider from './providers/coursesProvider.tsx';
import CoursesPage from './ui/pages/course/CoursesPage/CoursesPage.tsx';
import CourseDetailsPage from './ui/pages/course/CourseDetailsPage/CourseDetailsPage.tsx';
import EnrollmentsProvider from './providers/enrollmentsProvider.tsx';
import EnrollmentsPage from './ui/pages/enrollment/EnrollmentsPage/EnrollmentsPage.tsx';
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
            <Route element={<CoursesProvider><Outlet/></CoursesProvider>}>
              <Route path='courses' element={<CoursesPage/>}/>
              <Route path='courses/:id' element={<CourseDetailsPage/>}/>
            </Route>
          </Route>
          <Route element={<ProtectedRoute role='ROLE_STUDENT'/>}>
            <Route element={<EnrollmentsProvider><Outlet/></EnrollmentsProvider>}>
              <Route path='enrollments' element={<EnrollmentsPage/>}/>
            </Route>
            <Route path='notifications' element={<NotificationsPage/>}/>
          </Route>
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
