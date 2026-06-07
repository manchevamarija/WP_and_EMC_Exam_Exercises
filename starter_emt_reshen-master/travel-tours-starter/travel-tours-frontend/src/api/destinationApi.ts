import axiosInstance from '../axios/axios.ts';
import type { CreateOrUpdateDestinationRequest, DisplayDestinationResponse } from './types/destination.ts';

const destinationApi = {
  findAll: async () => {
    return await axiosInstance.get<DisplayDestinationResponse[]>('/destinations');
  },
  create: async (data: CreateOrUpdateDestinationRequest) => {
    return await axiosInstance.post<DisplayDestinationResponse>('/destinations/add', data);
  },
  update: async (id: string, data: CreateOrUpdateDestinationRequest) => {
    return await axiosInstance.put<DisplayDestinationResponse>(`/destinations/${id}/edit`, data);
  },
  delete: async (id: string) => {
    return await axiosInstance.delete<DisplayDestinationResponse>(`/destinations/${id}/delete`);
  }
};

export default destinationApi;