import { Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from '@mui/material';
import type { DisplayTourResponse } from '../../../../api/types/tour.ts';
import useTours from '../../../../hooks/useTours.ts';

interface DeleteTourDialogProps {
  tour: DisplayTourResponse;
  open: boolean,
  onClose: () => void;
}

const DeleteTourDialog = ({ tour, open, onClose }: DeleteTourDialogProps) => {
    const { onDelete } = useTours();

    const handleSubmit = async () => {
        await onDelete(tour.id);
        onClose();
    };

    return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>Delete Tour</DialogTitle>
      <DialogContent>
        <DialogContentText>
          Are you sure you want to delete <strong>{tour.title}</strong>? This action cannot be undone.
        </DialogContentText>
        <DialogActions>
          <Button onClick={onClose}>Cancel</Button>
          <Button onClick={handleSubmit} color='error' variant='contained' className='submit-btn'>Delete</Button>
        </DialogActions>
      </DialogContent>
    </Dialog>
  );
};

export default DeleteTourDialog;