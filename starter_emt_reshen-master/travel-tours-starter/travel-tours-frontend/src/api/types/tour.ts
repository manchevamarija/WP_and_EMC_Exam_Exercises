import type { DisplayDestinationResponse } from './destination.ts';

export interface CreateOrUpdateTourRequest {
  title: string;
  description: string;
  destinationId: number;
  price: number;
  capacity: number;
  startDate: string;
  endDate: string;
}

export interface DisplayTourResponse {
  id: number;
  title: string;
  description: string;
  destinationId: number,
  price: number;
  capacity: number;
  startDate: string;
  endDate: string;
}

export interface DisplayTourDetailsResponse {
  id: number;
  title: string;
  description: string;
  destination: DisplayDestinationResponse,
  price: number;
  capacity: number;
  startDate: string,
  endDate: string;
  durationInDays: number
}