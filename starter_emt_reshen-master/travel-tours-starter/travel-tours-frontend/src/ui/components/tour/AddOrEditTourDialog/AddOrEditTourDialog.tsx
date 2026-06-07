import {
  Button,
  Dialog, DialogActions, DialogContent, DialogTitle, FormControl, InputLabel, MenuItem, Select, type SelectChangeEvent,
  TextField
} from '@mui/material';
import { useState } from 'react';
import * as React from 'react';
import type { CreateOrUpdateTourRequest, DisplayTourResponse } from '../../../../api/types/tour.ts';
import useTours from '../../../../hooks/useTours.ts';
import useDestinations from '../../../../hooks/useDestinations.ts';

interface FormData {
  title: string;
  description: string;
  destinationId: string;
  price: string;
  capacity: string;
  startDate: string;
  endDate: string;
}

const emptyFormData: FormData = {
    title: '',
    description: '',
    destinationId: '',
    price: '',
    capacity: '',
    startDate: '',
    endDate: ''
};


const tourToFormData = (tour: DisplayTourResponse): FormData => ({
  title: tour.title,
  description: tour.description,
  destinationId: tour.destinationId.toString(),
  price: tour.price.toString(),
  capacity: tour.capacity.toString(),
  startDate: tour.startDate,
  endDate: tour.endDate
});

interface TourFormDialogProps {
  open: boolean;
  onClose: () => void;
  tour?: DisplayTourResponse;
}

const AddOrEditTourDialog = ({ open, onClose, tour }: TourFormDialogProps) => {
  const { destinations } = useDestinations();
    const { onAdd, onEdit } = useTours();

  const isEdit = tour !== undefined;

    const [formData, setFormData] = useState<FormData>(
        tour ? tourToFormData(tour) : emptyFormData
    );

    const handleChange = (
    event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement> | SelectChangeEvent
  ) => {
        const { name, value } = event.target;
        setFormData((prev) => ({ ...prev, [name]: value }));

    };

  const handleSubmit = async () => {
      const payload: CreateOrUpdateTourRequest = {
          title: formData.title.trim(),
          description: formData.description.trim(),
          destinationId: Number(formData.destinationId),
          price: Number(formData.price),
          capacity: Number(formData.capacity),
          startDate: formData.startDate,
          endDate: formData.endDate

      };

      if (isEdit) {
          await onEdit(tour.id, payload);
      } else {
          await onAdd(payload);
          setFormData(emptyFormData);
      }
      onClose();
  };


    return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth='sm'>
      <DialogTitle>{isEdit ? 'Edit Tour' : 'Add Tour'}</DialogTitle>
      <DialogContent>
        <TextField
          margin='dense'
          fullWidth
          label='Title'
          name='title'
          value={formData.title}
          onChange={handleChange}


          type='text'
        />
        <TextField
          margin='dense'
          fullWidth
          label='Description'
          name='description'
          value={formData.description}
          onChange={handleChange}


          type='text'
        />
        <FormControl fullWidth margin='dense'>
          <InputLabel>Destination</InputLabel>
          <Select
            variant='outlined'
            label='Destination'
            name='destinationId'
            value={formData.destinationId}
            onChange={handleChange}


            className='destination-select'
            MenuProps={{ slotProps: { paper: { style: { maxHeight: 200 } } } }}
          >
            {destinations.map((destination) => (
              <MenuItem key={destination.id} value={destination.id.toString()} className='destination-option'>
                {destination.name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <TextField
          margin='dense'
          fullWidth
          label='Capacity'
          name='capacity'
          value={formData.capacity}
          onChange={handleChange}


          type='number'
        />
        <TextField
          margin='dense'
          fullWidth
          label='Price'
          name='price'
          value={formData.price}
          onChange={handleChange}


          type='number'
        />
        <TextField
          margin='dense'
          label='Start Date'
          name='startDate'
          value={formData.startDate}
          onChange={handleChange}


          type='date'
          fullWidth
          slotProps={{ inputLabel: { shrink: true } }}
        />
        <TextField
          margin='dense'
          label='End Date'
          name='endDate'
          value={formData.endDate}
          onChange={handleChange}


          type='date'
          fullWidth
          slotProps={{ inputLabel: { shrink: true } }}
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancel</Button>
        <Button onClick={handleSubmit}
                variant='contained'
                color='primary'
                className='submit-btn'
        >
          {isEdit ? 'Edit' : 'Add'}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default AddOrEditTourDialog;