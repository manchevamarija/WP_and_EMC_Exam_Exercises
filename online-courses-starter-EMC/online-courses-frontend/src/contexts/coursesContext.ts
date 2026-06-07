import { createContext } from 'react';
import type { CreateOrUpdateCourseRequest, DisplayCourseResponse } from '../api/types/course.ts';

export interface CoursesContextType {
  courses: DisplayCourseResponse[];
  loading: boolean;
  availabilityFilter: string;
  setAvailabilityFilter: (value: string) => void;
  onAdd: (data: CreateOrUpdateCourseRequest) => Promise<void>;
  onEdit: (id: number, data: CreateOrUpdateCourseRequest) => Promise<void>;
  onDelete: (id: number) => Promise<void>;
}

const CoursesContext = createContext<CoursesContextType>({} as CoursesContextType);

export default CoursesContext;
