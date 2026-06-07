import {
  Button,
  Dialog, DialogActions, DialogContent, DialogTitle, FormControl, InputLabel, MenuItem, Select, type SelectChangeEvent,
  TextField
} from '@mui/material';
import { useState } from 'react';
import * as React from 'react';
import type { CreateOrUpdateCourseRequest, DisplayCourseResponse } from '../../../../api/types/course.ts';
import useCourses from '../../../../hooks/useCourses.ts';
import useTopics from '../../../../hooks/useTopics.ts';

interface FormData {
  title: string;
  description: string;
  topicId: string;
  price: string;
  capacity: string;
  startDate: string;
  endDate: string;
}

const emptyFormData: FormData = {
    title: '',
    description: '',
    topicId: '',
    price: '',
    capacity: '',
    startDate: '',
    endDate: ''
};


const courseToFormData = (course: DisplayCourseResponse): FormData => ({
  title: course.title,
  description: course.description,
  topicId: course.topicId.toString(),
  price: course.price.toString(),
  capacity: course.capacity.toString(),
  startDate: course.startDate,
  endDate: course.endDate
});

interface CourseFormDialogProps {
  open: boolean;
  onClose: () => void;
  course?: DisplayCourseResponse;
}

const AddOrEditCourseDialog = ({ open, onClose, course }: CourseFormDialogProps) => {
  const { topics } = useTopics();
  const { onAdd, onEdit } = useCourses();

  const isEdit = course !== undefined;

    const [formData, setFormData] = useState<FormData>(
        course ? courseToFormData(course) : emptyFormData
    );

  const handleChange = (
    event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement> | SelectChangeEvent
  ) => {
      const { name, value } = event.target;
      setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async () => {
      const payload: CreateOrUpdateCourseRequest = {
          title: formData.title.trim(),
          description: formData.description.trim(),
          topicId: Number(formData.topicId),
          price: Number(formData.price),
          capacity: Number(formData.capacity),
          startDate: formData.startDate,
          endDate: formData.endDate
      };

      if (isEdit) {
          await onEdit(course.id, payload);
      } else {
          await onAdd(payload);
          setFormData(emptyFormData);
      }
      onClose();
  };


    return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth='sm'>
      <DialogTitle>{isEdit ? 'Edit Course' : 'Add Course'}</DialogTitle>
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
          <InputLabel>Topic</InputLabel>
          <Select
            variant='outlined'
            label='Topic'
            name='topicId'
            value={formData.topicId}
            onChange={handleChange}
            className='topic-select'
            MenuProps={{ slotProps: { paper: { style: { maxHeight: 200 } } } }}
          >
            {topics.map((topic) => (
              <MenuItem key={topic.id} value={topic.id.toString()} className='topic-option'>
                {topic.name}
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
          type='date'
          value={formData.startDate}
          onChange={handleChange}
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

export default AddOrEditCourseDialog;