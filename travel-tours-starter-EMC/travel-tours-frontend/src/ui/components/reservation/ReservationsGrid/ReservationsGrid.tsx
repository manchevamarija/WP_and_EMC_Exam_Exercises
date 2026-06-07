import { Grid } from '@mui/material';
import ReservationCard from '../ReservationCard/ReservationCard';
import type { DisplayReservationDetailsResponse } from '../../../../api/types/reservation.ts';

interface ReservationsGridProps {
  reservations: DisplayReservationDetailsResponse[];
  onCancel: (tourId: number) => void;
}

const ReservationsGrid = ({ reservations, onCancel }: ReservationsGridProps) => {
  return (
    <Grid container spacing={{ xs: 2, md: 3 }}>
      {reservations.map((reservation) => (
        <Grid key={reservation.id} size={{ xs: 12, sm: 6, md: 4, lg: 3 }}>
          <ReservationCard reservation={reservation} onCancel={onCancel}/>
        </Grid>
      ))}
    </Grid>
  );
};

export default ReservationsGrid;
