import { Box, CircularProgress } from '@mui/material';
import './ReservationsPage.css';
import ReservationsGrid from '../../../components/reservation/ReservationsGrid/ReservationsGrid.tsx';
import useReservations from '../../../../hooks/useReservations.ts';

const ReservationsPage = () => {
  const { reservations, loading, onCancel } = useReservations();

  return (
    <Box className='tours-box'>
      {loading && (
        <Box className='progress-box'>
          <CircularProgress/>
        </Box>
      )}
      {!loading && <ReservationsGrid reservations={reservations} onCancel={onCancel}/>}
    </Box>
  );
};

export default ReservationsPage;
