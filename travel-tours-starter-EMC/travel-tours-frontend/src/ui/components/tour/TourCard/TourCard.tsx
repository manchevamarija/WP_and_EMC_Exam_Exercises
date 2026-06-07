import { Box, Button, Card, CardActions, CardContent, Typography } from '@mui/material';
import InfoIcon from '@mui/icons-material/Info';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { useNavigate } from 'react-router';
import { useState } from 'react';
import useAuth from '../../../../hooks/useAuth.ts';
import type { DisplayTourResponse } from '../../../../api/types/tour.ts';
import AddOrEditTourDialog from '../AddOrEditTourDialog/AddOrEditTourDialog.tsx';
import DeleteTourDialog from '../DeleteTourDialog/DeleteTourDialog.tsx';

interface TourCardProps {
  tour: DisplayTourResponse;
}

const TourCard = ({ tour }: TourCardProps) => {
  const { user } = useAuth();
  const isAdministrator = user?.roles.includes('ROLE_ADMINISTRATOR') ?? false;

  const navigate = useNavigate();

  const [editTourDialogOpen, setEditTourDialogOpen] = useState<boolean>(false);
  const [deleteTourDialogOpen, setDeleteTourDialogOpen] = useState<boolean>(false);

  return (
    <>
      <Card sx={{ maxWidth: 300, height: '100%', display: 'flex', flexDirection: 'column' }} className='card'
            data-id={tour.id}>
        <CardContent sx={{ flexGrow: 1, display: 'flex', flexDirection: 'column' }}>
          <Typography variant='h5' className='tour-title'>{tour.title}</Typography>
          <Typography
            variant='subtitle1'
            className='tour-description'
            sx={{ flexGrow: 1 }}
          >
            {tour.description}
          </Typography>
          <Typography variant='h6' className='tour-price' sx={{ textAlign: 'right' }}>${tour.price}</Typography>
          <Typography variant='body2' className='tour-capacity'
                      sx={{ textAlign: 'left' }}>Capacity: {tour.capacity}</Typography>
        </CardContent>
        <CardActions sx={{ justifyContent: 'space-between' }}>
          <Button
            startIcon={<InfoIcon/>}
            onClick={() => navigate(`/tours/${tour.id}`)}
            className='info-item'
          >
            Info
          </Button>
          {isAdministrator && (
            <Box>
              <Button
                startIcon={<EditIcon/>}
                color='warning'
                onClick={() => setEditTourDialogOpen(true)}
                className='edit-item'
              >
                Edit
              </Button>
              <Button
                startIcon={<DeleteIcon/>}
                color='error'
                onClick={() => setDeleteTourDialogOpen(true)}
                className='delete-item'
              >
                Delete
              </Button>
            </Box>
          )}
        </CardActions>
      </Card>
      <AddOrEditTourDialog
        tour={tour}
        open={editTourDialogOpen}
        onClose={() => setEditTourDialogOpen(false)}
      />
      <DeleteTourDialog
        tour={tour}
        open={deleteTourDialogOpen}
        onClose={() => setDeleteTourDialogOpen(false)}
      />
    </>
  );
};

export default TourCard;