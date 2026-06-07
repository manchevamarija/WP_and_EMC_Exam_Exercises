import { useContext } from 'react';
import ReservationsContext, { type ReservationsContextType } from '../contexts/reservationsContext.ts';

const useReservations = () => useContext<ReservationsContextType>(ReservationsContext);

export default useReservations;
