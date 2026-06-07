import { Box, Button, Card, CardActions, CardContent, Typography } from '@mui/material';
import InfoIcon from '@mui/icons-material/Info';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { useNavigate } from 'react-router';
import { useState } from 'react';
import useAuth from '../../../../hooks/useAuth.ts';
import type { DisplayCourseResponse } from '../../../../api/types/course.ts';
import AddOrEditCourseDialog from '../AddOrEditCourseDialog/AddOrEditCourseDialog.tsx';
import DeleteCourseDialog from '../DeleteCourseDialog/DeleteCourseDialog.tsx';

interface CourseCardProps {
  course: DisplayCourseResponse;
}

const CourseCard = ({ course }: CourseCardProps) => {
  const { user } = useAuth();
  const isAdministrator = user?.roles.includes('ROLE_ADMINISTRATOR') ?? false;

  const navigate = useNavigate();

  const [editCourseDialogOpen, setEditCourseDialogOpen] = useState<boolean>(false);
  const [deleteCourseDialogOpen, setDeleteCourseDialogOpen] = useState<boolean>(false);

  return (
    <>
      <Card sx={{ maxWidth: 300, height: '100%', display: 'flex', flexDirection: 'column' }} className='card'
            data-id={course.id}>
        <CardContent sx={{ flexGrow: 1, display: 'flex', flexDirection: 'column' }}>
          <Typography variant='h5' className='course-title'>{course.title}</Typography>
          <Typography
            variant='subtitle1'
            className='course-description'
            sx={{ flexGrow: 1 }}
          >
            {course.description}
          </Typography>
          <Typography variant='h6' className='course-price' sx={{ textAlign: 'right' }}>${course.price}</Typography>
          <Typography variant='body2' className='course-capacity'
                      sx={{ textAlign: 'left' }}>Capacity: {course.capacity}</Typography>
        </CardContent>
        <CardActions sx={{ justifyContent: 'space-between' }}>
          <Button
            startIcon={<InfoIcon/>}
            onClick={() => navigate(`/courses/${course.id}`)}
            className='info-item'
          >
            Info
          </Button>
          {isAdministrator && (
            <Box>
              <Button
                startIcon={<EditIcon/>}
                color='warning'
                onClick={() => setEditCourseDialogOpen(true)}
                className='edit-item'
              >
                Edit
              </Button>
              <Button
                startIcon={<DeleteIcon/>}
                color='error'
                onClick={() => setDeleteCourseDialogOpen(true)}
                className='delete-item'
              >
                Delete
              </Button>
            </Box>
          )}
        </CardActions>
      </Card>
      <AddOrEditCourseDialog
        course={course}
        open={editCourseDialogOpen}
        onClose={() => setEditCourseDialogOpen(false)}
      />
      <DeleteCourseDialog
        course={course}
        open={deleteCourseDialogOpen}
        onClose={() => setDeleteCourseDialogOpen(false)}
      />
    </>
  );
};

export default CourseCard;