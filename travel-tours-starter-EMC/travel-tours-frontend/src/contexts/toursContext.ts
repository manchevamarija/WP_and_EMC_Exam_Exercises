import { createContext } from 'react';
import type { CreateOrUpdateTourRequest, DisplayTourResponse } from '../api/types/tour.ts';

export interface ToursContextType {
  tours: DisplayTourResponse[];
  loading: boolean;
  availabilityFilter: string;
  setAvailabilityFilter: (value: string) => void;
  onAdd: (data: CreateOrUpdateTourRequest) => Promise<void>;
  onEdit: (id: number, data: CreateOrUpdateTourRequest) => Promise<void>;
  onDelete: (id: number) => Promise<void>;
}

const ToursContext = createContext<ToursContextType>({} as ToursContextType);

export default ToursContext;
