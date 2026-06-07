import { createContext } from 'react';
import type { DisplayReservationDetailsResponse } from '../api/types/reservation.ts';

export interface ReservationsContextType {
  reservations: DisplayReservationDetailsResponse[];
  loading: boolean;
  onCancel: (tourId: number) => Promise<void>;
}

const ReservationsContext = createContext<ReservationsContextType>({} as ReservationsContextType);

export default ReservationsContext;
