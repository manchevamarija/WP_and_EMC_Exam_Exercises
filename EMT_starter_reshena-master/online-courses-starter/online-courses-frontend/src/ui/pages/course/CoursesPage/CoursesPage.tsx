import './CoursesPage.css';
import { Box, Button, CircularProgress, ToggleButton, ToggleButtonGroup } from '@mui/material';
import useAuth from '../../../../hooks/useAuth.ts';
import useCourses from '../../../../hooks/useCourses.ts';
import CourseGrid from '../../../components/course/CourseGrid/CourseGrid.tsx';
import AddOrEditCourseDialog from '../../../components/course/AddOrEditCourseDialog/AddOrEditCourseDialog.tsx';
import { useState } from 'react';
import * as React from 'react';

const CoursesPage = () => {
  const { user } = useAuth();
  const isAdministrator = user?.roles.includes('ROLE_ADMINISTRATOR') ?? false;

  const { courses, loading, availabilityFilter, setAvailabilityFilter } = useCourses();

  const [addCourseDialogOpen, setAddCourseDialogOpen] = useState<boolean>(false);

  const handleAvailabilityFilterChange = (_event: React.MouseEvent<HTMLElement>, value: string | null) => {
    setAvailabilityFilter(value ?? 'all');
  };

  return (
    <Box className='courses-box'>
      {loading && (
        <Box className='progress-box'>
          <CircularProgress/>
        </Box>
      )}
      {!loading &&
       <>
         {isAdministrator && (
           <Box sx={{ display: 'flex', justifyContent: 'flex-end', mb: 2 }}>
             <Button
               variant='contained'
               color='primary'
               onClick={() => setAddCourseDialogOpen(true)}
               className='add-item'
             >
               Add Course
             </Button>
           </Box>
         )}
         <ToggleButtonGroup
           exclusive
           value={availabilityFilter}
           onChange={handleAvailabilityFilterChange}
           sx={{mb: 2}}
         >
           <ToggleButton className='all-courses' value='all' size='small'>
             All
           </ToggleButton>
           <ToggleButton className='available-courses' value='available' size='small'>
             Available
           </ToggleButton>
           <ToggleButton className='unavailable-courses' value='unavailable' size='small'>
             Unavailable
           </ToggleButton>
         </ToggleButtonGroup>
         <CourseGrid courses={courses}/>
         <AddOrEditCourseDialog
           open={addCourseDialogOpen}
           onClose={() => setAddCourseDialogOpen(false)}
         />
       </>}
    </Box>
  );
};

export default CoursesPage;