import { Grid } from '@mui/material';
import CourseCard from '../CourseCard/CourseCard.tsx';
import type { DisplayCourseResponse } from '../../../../api/types/course.ts';

interface CourseGridProps {
  courses: DisplayCourseResponse[];
}

const CourseGrid = ({ courses }: CourseGridProps) => {
  return (
    <Grid container spacing={{ xs: 2, md: 3 }}>
      {courses.map((course) => (
        <Grid key={course.id} size={{ xs: 12, sm: 6, md: 4, lg: 3 }}>
          <CourseCard course={course}/>
        </Grid>
      ))}
    </Grid>
  );
};

export default CourseGrid;