import { Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from '@mui/material';
import type { DisplayCourseResponse } from '../../../../api/types/course.ts';
import useCourses from '../../../../hooks/useCourses.ts';

interface DeleteCourseDialogProps {
  course: DisplayCourseResponse;
  open: boolean,
  onClose: () => void;
}

const DeleteCourseDialog = ({ course, open, onClose }: DeleteCourseDialogProps) => {
  const { onDelete } = useCourses();

  const handleSubmit = async () => {
      await onDelete(course.id);
      onClose();

  };

  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>Delete Product</DialogTitle>
      <DialogContent>
        <DialogContentText>
          Are you sure you want to delete <strong>{course.title}</strong>? This action cannot be undone.
        </DialogContentText>
        <DialogActions>
          <Button onClick={onClose}>Cancel</Button>
          <Button onClick={handleSubmit} color='error' variant='contained' className='submit-btn'>Delete</Button>
        </DialogActions>
      </DialogContent>
    </Dialog>
  );
};

export default DeleteCourseDialog;