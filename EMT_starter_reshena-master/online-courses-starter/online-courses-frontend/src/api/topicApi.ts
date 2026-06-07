import axiosInstance from '../axios/axios.ts';
import type { CreateOrUpdateTopicRequest, DisplayTopicResponse } from './types/topic.ts';

const topicApi = {
  findAll: async () => {
    return await axiosInstance.get<DisplayTopicResponse[]>('/topics');
  },
  create: async (data: CreateOrUpdateTopicRequest) => {
    return await axiosInstance.post<DisplayTopicResponse>('/topics/add', data);
  },
  update: async (id: string, data: CreateOrUpdateTopicRequest) => {
    return await axiosInstance.put<DisplayTopicResponse>(`/topics/${id}/edit`, data);
  },
  delete: async (id: string) => {
    return await axiosInstance.delete<DisplayTopicResponse>(`/topics/${id}/delete`);
  }
};

export default topicApi;