import { Link, useNavigate, useParams } from 'react-router';
import {
  Box, Breadcrumbs, Button, Chip, CircularProgress, Paper, Stack, Typography
} from '@mui/material';
import { ArrowBack, Cancel, CheckCircle, Category, CalendarMonth } from '@mui/icons-material';
import './CourseDetailsPage.css';
import useCourseDetails from '../../../../hooks/useCourseDetails.ts';
import useAuth from '../../../../hooks/useAuth.ts';

const CourseDetailsPage = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const { courseDetails: course, availableSpots, isEnrolled, enroll } = useCourseDetails(id);

  const { user } = useAuth();
  const isStudent = user?.roles.includes('ROLE_STUDENT') ?? false;

  if (!course) {
    return <Box className='progress-box'><CircularProgress/></Box>;
  }

  return (
    <Box>
      <Breadcrumbs aria-label='breadcrumb' sx={{ mb: 3 }}>
        <Link
          to='/courses'
          style={{ textDecoration: 'none', color: 'inherit' }}
          onMouseEnter={e => (e.currentTarget.style.textDecoration = 'underline')}
          onMouseLeave={e => (e.currentTarget.style.textDecoration = 'none')}
        >
          Courses
        </Link>
        <Typography color='text.primary'>{course.title}</Typography>
      </Breadcrumbs>

      <Paper elevation={2} sx={{ p: 4, borderRadius: 4 }}>
        <Stack spacing={3}>
          <Stack direction='column' spacing={1}>
            <Typography variant='h4' className='course-title'>
              {course.title}
            </Typography>

            <Typography variant='subtitle1' className='course-description'>
              {course.description}
            </Typography>
          </Stack>

          <Typography variant='h4' color='primary.main' sx={{ fontWeight: 750 }} className='course-price'>
            ${course.price}
          </Typography>

          <Stack direction='row' sx={{ justifyContent: 'space-between', alignItems: 'center' }}>
            <Chip
              icon={<Category/>}
              label={course.topic.name}
              className='topic-name'
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
                <strong className='course-available'>
                  <span className='available-spots'>{availableSpots}</span>
                  <span> out of </span>
                  <span className='course-capacity'>{course.capacity}</span>
                  <span> available</span>
                </strong>
              </Typography>
            </Box>
          </Stack>

          {course.startDate && course.endDate && (
            <Stack direction='row' spacing={2} sx={{ alignItems: 'center' }} className='course-schedule'>
              <CalendarMonth color='primary'/>
              <Typography variant='body1'>
                From <strong className='course-start-date'>{course.startDate}</strong>{' '}
                To <strong className='course-end-date'>{course.endDate}</strong>{' '}
                (<span className='course-duration'>{course.durationInDays}</span> days)
              </Typography>
            </Stack>
          )}
          <Stack direction='row' spacing={2} sx={{ justifyContent: 'space-between', mt: 2 }}>
            <Button variant='outlined' startIcon={<ArrowBack/>} onClick={() => navigate('/courses')}>
              Back to Courses
            </Button>
            {isStudent && (
              <Button
                variant='contained'
                color='primary'
                onClick={() => enroll()}
                className='enroll-btn'
                disabled={isEnrolled === true || availableSpots === 0}
              >
                {isEnrolled ? 'Already Enrolled' : availableSpots === 0 ? 'Full' : 'Enroll'}
              </Button>
            )}
          </Stack>
        </Stack>
      </Paper>
    </Box>
  );
};

export default CourseDetailsPage;