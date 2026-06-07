import axiosInstance from '../axios/axios.ts';
import type {
  CreateOrUpdateTourRequest, DisplayTourDetailsResponse, DisplayTourResponse
} from './types/tour.ts';

const tourApi = {
  findAll: async (availabilityFilter = 'all') => {
    if (availabilityFilter === 'all') {
      return await axiosInstance.get('/tours');
    }
    return await axiosInstance.get('/tours', {
      params: {
        available: availabilityFilter === 'available'
      }
    });
  },

  findWithDetailsById: async (id: number) => {
    return await axiosInstance.get<DisplayTourDetailsResponse>(`/tours/${id}/details`);
  },
  create: async (data: CreateOrUpdateTourRequest) => {
    return await axiosInstance.post<DisplayTourResponse>('/tours/add', data);
  },
  update: async (id: string, data: CreateOrUpdateTourRequest) => {
    return await axiosInstance.put<DisplayTourResponse>(`/tours/${id}/edit`, data);
  },
  delete: async (id: string) => {
    return await axiosInstance.delete<DisplayTourResponse>(`/tours/${id}/delete`);
  }
};

export default tourApi;