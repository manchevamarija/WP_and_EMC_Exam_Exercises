import { Grid } from '@mui/material';
import EnrollmentCard from '../EnrollmentCard/EnrollmentCard';
import type { DisplayEnrollmentDetailsResponse } from '../../../../api/types/enrollment.ts';

interface EnrollmentsGridProps {
  enrollments: DisplayEnrollmentDetailsResponse[];
  onUnenroll: (courseId: number) => void;
}

const EnrollmentsGrid = ({ enrollments, onUnenroll }: EnrollmentsGridProps) => {
  return (
    <Grid container spacing={{ xs: 2, md: 3 }}>
      {enrollments.map((enrollment) => (
        <Grid key={enrollment.id} size={{ xs: 12, sm: 6, md: 4, lg: 3 }}>
          <EnrollmentCard enrollment={enrollment} onUnenroll={onUnenroll}/>
        </Grid>
      ))}
    </Grid>
  );
};

export default EnrollmentsGrid;
