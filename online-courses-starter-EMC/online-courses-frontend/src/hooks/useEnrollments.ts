import { useContext } from 'react';
import EnrollmentsContext, { type EnrollmentsContextType } from '../contexts/enrollmentsContext.ts';

const useEnrollments = () => useContext<EnrollmentsContextType>(EnrollmentsContext);

export default useEnrollments;
