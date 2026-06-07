import type { DisplayTourDetailsResponse, DisplayTourResponse } from './tour.ts';

export interface DisplayReservationResponse {
  id: number;
  tour: DisplayTourResponse,
  username: string
}

export interface DisplayReservationDetailsResponse {
  id: number;
  tour: DisplayTourDetailsResponse,
  username: string
}
