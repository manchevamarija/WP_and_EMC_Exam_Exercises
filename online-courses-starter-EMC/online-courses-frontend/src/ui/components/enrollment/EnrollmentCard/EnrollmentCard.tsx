import { Card, CardContent, Typography, CardActions, Button, Chip, Stack } from '@mui/material';
import { Category, PersonOutlined } from '@mui/icons-material';
import useAuth from '../../../../hooks/useAuth';
import type { DisplayEnrollmentDetailsResponse } from '../../../../api/types/enrollment.ts';

interface EnrollmentCardProps {
  enrollment: DisplayEnrollmentDetailsResponse;
  onUnenroll: (courseId: number) => void;
}

const EnrollmentCard = ({ enrollment, onUnenroll }: EnrollmentCardProps) => {
  const { user } = useAuth();
  const isStudent = user?.roles.includes('ROLE_STUDENT') ?? false;

  return (
    <Card sx={{ maxWidth: 300, height: '100%', display: 'flex', flexDirection: 'column' }} className='card'
          data-id={enrollment.id}>
      <CardContent sx={{ flexGrow: 1, display: 'flex', flexDirection: 'column' }}>
        <Typography gutterBottom variant='h5' component='div' className='course-title'>
          {enrollment.course.title}
        </Typography>
        <Typography gutterBottom variant='subtitle1' component='div' className='course-description'
                    sx={{ flexGrow: 1 }}>
          {enrollment.course.description}
        </Typography>
        <Stack direction='row' spacing={1} sx={{ alignItems: 'center', mt: 1 }}>
          <PersonOutlined fontSize='small' color='action'/>
          <Typography variant='body2' color='text.secondary' className='enrollment-username'>
            {enrollment.username}
          </Typography>
        </Stack>
      </CardContent>
      <CardActions sx={{ justifyContent: 'space-between' }}>
        <Chip
          icon={<Category fontSize='small'/>}
          label={enrollment.course.topic.name}
          className='topic-name'
          color='secondary'
          variant='outlined'
          sx={{ width: 'fit-content', p: 1 }}
        />
        {isStudent && (
          <Button
            variant='outlined'
            color='error'
            className='unenroll-item'
            onClick={() => onUnenroll(enrollment.course.id)}
          >
            Unenroll
          </Button>
        )}
      </CardActions>
    </Card>
  );
};

export default EnrollmentCard;
