import { Grid } from '@mui/material';
import TourCard from '../TourCard/TourCard.tsx';
import type { DisplayTourResponse } from '../../../../api/types/tour.ts';

interface TourGridProps {
  tours: DisplayTourResponse[];
}

const TourGrid = ({ tours }: TourGridProps) => {
  return (
    <Grid container spacing={{ xs: 2, md: 3 }}>
      {tours.map((tour) => (
        <Grid key={tour.id} size={{ xs: 12, sm: 6, md: 4, lg: 3 }}>
          <TourCard tour={tour}/>
        </Grid>
      ))}
    </Grid>
  );
};

export default TourGrid;