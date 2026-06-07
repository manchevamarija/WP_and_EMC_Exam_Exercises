import axiosInstance from '../axios/axios.ts';
import type { DisplayReservationDetailsResponse } from './types/reservation.ts';

class DisplayReservationResponseDto {
}

const reservationApi = {
  findAllWithDetailsByUser: async () => {
    return await axiosInstance.get<DisplayReservationDetailsResponse[]>('/reservations');
  },
  existsByTourAndUser: async (tourId: number) => {
    return await axiosInstance.get<boolean>(`/reservations/tour/${tourId}/is-reserved`);
  },
  getAvailableSpotsByTour: async (tourId: number) => {
    return await axiosInstance.get<number>(`/reservations/tour/${tourId}/available-spots`);
  },
  reserveByTour: async (tourId: number) => {
    return await axiosInstance.post<DisplayReservationResponseDto>(`/reservations/tour/${tourId}/reserve`);
  },
  cancelByTour: async (tourId: number) => {
    return await axiosInstance.delete<DisplayReservationResponseDto>(`/reservations/tour/${tourId}/cancel`);
  }
};

export default reservationApi;