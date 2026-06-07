import './ToursPage.css';
import { Box, Button, CircularProgress, ToggleButton, ToggleButtonGroup } from '@mui/material';
import useAuth from '../../../../hooks/useAuth.ts';
import useTours from '../../../../hooks/useTours.ts';
import TourGrid from '../../../components/tour/TourGrid/TourGrid.tsx';
import AddOrEditTourDialog from '../../../components/tour/AddOrEditTourDialog/AddOrEditTourDialog.tsx';
import { useState } from 'react';
import * as React from 'react';

const ToursPage = () => {
  const { user } = useAuth();
  const isAdministrator = user?.roles.includes('ROLE_ADMINISTRATOR') ?? false;

  const { tours, loading, availabilityFilter, setAvailabilityFilter } = useTours();

  const [addTourDialogOpen, setAddTourDialogOpen] = useState<boolean>(false);

  const handleAvailabilityFilterChange = (_event: React.MouseEvent<HTMLElement>, value: string | null) => {
    setAvailabilityFilter(value ?? 'all');
  };

  return (
    <Box className='tours-box'>
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
               onClick={() => setAddTourDialogOpen(true)}
               className='add-item'
             >
               Add Tour
             </Button>
           </Box>
         )}
         <ToggleButtonGroup
           exclusive
           value={availabilityFilter}
           onChange={handleAvailabilityFilterChange}
           sx={{mb: 2}}
         >
           <ToggleButton className='all-tours' value='all' size='small'>
             All
           </ToggleButton>
           <ToggleButton className='available-tours' value='available' size='small'>
             Available
           </ToggleButton>
           <ToggleButton className='unavailable-tours' value='unavailable' size='small'>
             Unavailable
           </ToggleButton>
         </ToggleButtonGroup>
         <TourGrid tours={tours}/>
         <AddOrEditTourDialog
           open={addTourDialogOpen}
           onClose={() => setAddTourDialogOpen(false)}
         />
       </>}
    </Box>
  );
};

export default ToursPage;