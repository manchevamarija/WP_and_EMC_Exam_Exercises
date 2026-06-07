import { Card, CardContent, Typography, CardActions, Button, Chip, Stack } from '@mui/material';
import { Category, PersonOutlined } from '@mui/icons-material';
import useAuth from '../../../../hooks/useAuth';
import type { DisplayReservationDetailsResponse } from '../../../../api/types/reservation.ts';

interface ReservationCardProps {
  reservation: DisplayReservationDetailsResponse;
  onCancel: (tourId: number) => void;
}

const ReservationCard = ({ reservation, onCancel }: ReservationCardProps) => {
  const { user } = useAuth();
  const isStudent = user?.roles.includes('ROLE_CUSTOMER') ?? false;

  return (
    <Card sx={{ maxWidth: 300, height: '100%', display: 'flex', flexDirection: 'column' }} className='card'
          data-id={reservation.id}>
      <CardContent sx={{ flexGrow: 1, display: 'flex', flexDirection: 'column' }}>
        <Typography gutterBottom variant='h5' component='div' className='tour-title'>
          {reservation.tour.title}
        </Typography>
        <Typography gutterBottom variant='subtitle1' component='div' className='tour-description'
                    sx={{ flexGrow: 1 }}>
          {reservation.tour.description}
        </Typography>
        <Stack direction='row' spacing={1} sx={{ alignItems: 'center', mt: 1 }}>
          <PersonOutlined fontSize='small' color='action'/>
          <Typography variant='body2' color='text.secondary' className='reservation-username'>
            {reservation.username}
          </Typography>
        </Stack>
      </CardContent>
      <CardActions sx={{ justifyContent: 'space-between' }}>
        <Chip
          icon={<Category fontSize='small'/>}
          label={reservation.tour.destination.name}
          className='destination-name'
          color='secondary'
          variant='outlined'
          sx={{ width: 'fit-content', p: 1 }}
        />
        {isStudent && (
          <Button
            variant='outlined'
            color='error'
            className='cancel-item'
            onClick={() => onCancel(reservation.tour.id)}
          >
            Cancel
          </Button>
        )}
      </CardActions>
    </Card>
  );
};

export default ReservationCard;
