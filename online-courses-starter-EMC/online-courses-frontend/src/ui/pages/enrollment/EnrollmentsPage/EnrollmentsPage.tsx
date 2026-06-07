import { Box, CircularProgress } from '@mui/material';
import './EnrollmentsPage.css';
import EnrollmentsGrid from '../../../components/enrollment/EnrollmentsGrid/EnrollmentsGrid.tsx';
import useEnrollments from '../../../../hooks/useEnrollments.ts';

const EnrollmentsPage = () => {
  const { enrollments, loading, onUnenroll } = useEnrollments();

  return (
    <Box className='courses-box'>
      {loading && (
        <Box className='progress-box'>
          <CircularProgress/>
        </Box>
      )}
      {!loading && <EnrollmentsGrid enrollments={enrollments} onUnenroll={onUnenroll}/>}
    </Box>
  );
};

export default EnrollmentsPage;
