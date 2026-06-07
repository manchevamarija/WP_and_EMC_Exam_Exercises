import { createContext } from 'react';
import type { DisplayEnrollmentDetailsResponse } from '../api/types/enrollment.ts';

export interface EnrollmentsContextType {
  enrollments: DisplayEnrollmentDetailsResponse[];
  loading: boolean;
  onUnenroll: (courseId: number) => Promise<void>;
}

const EnrollmentsContext = createContext<EnrollmentsContextType>({} as EnrollmentsContextType);

export default EnrollmentsContext;
