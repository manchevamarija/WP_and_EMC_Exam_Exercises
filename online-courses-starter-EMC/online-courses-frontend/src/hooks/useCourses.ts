import { useContext } from 'react';
import CoursesContext, { type CoursesContextType } from '../contexts/coursesContext.ts';

const useCourses = () => useContext<CoursesContextType>(CoursesContext);

export default useCourses;