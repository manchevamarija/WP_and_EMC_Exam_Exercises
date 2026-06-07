import { Link, useNavigate, useParams } from 'react-router';
import {
  Box, Breadcrumbs, Button, Chip, CircularProgress, Paper, Stack, Typography
} from '@mui/material';
import { ArrowBack, Cancel, CheckCircle, Category, CalendarMonth } from '@mui/icons-material';
import './TourDetailsPage.css';
import useTourDetails from '../../../../hooks/useTourDetails.ts';
import useAuth from '../../../../hooks/useAuth.ts';

const TourDetailsPage = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const { tourDetails: tour, availableSpots, isReserved, reserve } = useTourDetails(id);

  const { user } = useAuth();
  const isStudent = user?.roles.includes('ROLE_CUSTOMER') ?? false;

  if (!tour) {
    return <Box className='progress-box'><CircularProgress/></Box>;
  }

  return (
    <Box>
      <Breadcrumbs aria-label='breadcrumb' sx={{ mb: 3 }}>
        <Link
          to='/tours'
          style={{ textDecoration: 'none', color: 'inherit' }}
          onMouseEnter={e => (e.currentTarget.style.textDecoration = 'underline')}
          onMouseLeave={e => (e.currentTarget.style.textDecoration = 'none')}
        >
          Tours
        </Link>
        <Typography color='text.primary'>{tour.title}</Typography>
      </Breadcrumbs>

      <Paper elevation={2} sx={{ p: 4, borderRadius: 4 }}>
        <Stack spacing={3}>
          <Stack direction='column' spacing={1}>
            <Typography variant='h4' className='tour-title'>
              {tour.title}
            </Typography>

            <Typography variant='subtitle1' className='tour-description'>
              {tour.description}
            </Typography>
          </Stack>

          <Typography variant='h4' color='primary.main' sx={{ fontWeight: 750 }} className='tour-price'>
            ${tour.price}
          </Typography>

          <Stack direction='row' sx={{ justifyContent: 'space-between', alignItems: 'center' }}>
            <Chip
              icon={<Category/>}
              label={tour.destination.name}
              className='destination-name'
              color='secondary'
              variant='outlined'
              sx={{ width: 'fit-content', p: 2 }}
            />

            <Box sx={{ display: 'flex', alignItems: 'center' }}>
              {availableSpots ? (
                <CheckCircle color='success' sx={{ mr: 1 }}/>
              ) : (
                <Cancel color='error' sx={{ mr: 1 }}/>
              )}
              <Typography
                variant='body1'
                color={availableSpots ? 'success.main' : 'error.main'}
                className={availableSpots ? 'available' : 'not-available'}
              >
                <strong className='tour-available'>
                  <span className='available-spots'>{availableSpots}</span>
                  <span> out of </span>
                  <span className='tour-capacity'>{tour.capacity}</span>
                  <span> available</span>
                </strong>
              </Typography>
            </Box>
          </Stack>

          {tour.startDate && tour.endDate && (
            <Stack direction='row' spacing={2} sx={{ alignItems: 'center' }} className='tour-schedule'>
              <CalendarMonth color='primary'/>
              <Typography variant='body1'>
                From <strong className='tour-start-date'>{tour.startDate}</strong>{' '}
                To <strong className='tour-end-date'>{tour.endDate}</strong>{' '}
                (<span className='tour-duration'>{tour.durationInDays}</span> days)
              </Typography>
            </Stack>
          )}
          <Stack direction='row' spacing={2} sx={{ justifyContent: 'space-between', mt: 2 }}>
            <Button variant='outlined' startIcon={<ArrowBack/>} onClick={() => navigate('/tours')}>
              Back to Tours
            </Button>
            {isStudent && (
              <Button
                variant='contained'
                color='primary'
                onClick={() => reserve()}
                className='reserve-btn'
                disabled={isReserved === true || availableSpots === 0}
              >
                {isReserved ? 'Already Reserved' : availableSpots === 0 ? 'Full' : 'Reserve'}
              </Button>
            )}
          </Stack>
        </Stack>
      </Paper>
    </Box>
  );
};

export default TourDetailsPage;