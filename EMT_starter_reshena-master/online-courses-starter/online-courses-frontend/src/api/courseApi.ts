import axiosInstance from '../axios/axios.ts';
import type {
  CreateOrUpdateCourseRequest, DisplayCourseDetailsResponse, DisplayCourseResponse
} from './types/course.ts';

const courseApi = {
  findAll: async (availabilityFilter = 'all') => {
    if (availabilityFilter === 'all') {
      return await axiosInstance.get('/courses');
    }
    return await axiosInstance.get('/courses', {
      params: {
        available: availabilityFilter === 'available'
      }
    });
  },

  findWithDetailsById: async (id: number) => {
    return await axiosInstance.get<DisplayCourseDetailsResponse>(`/courses/${id}/details`);
  },
  create: async (data: CreateOrUpdateCourseRequest) => {
    return await axiosInstance.post<DisplayCourseResponse>('/courses/add', data);
  },
  update: async (id: string, data: CreateOrUpdateCourseRequest) => {
    return await axiosInstance.put<DisplayCourseResponse>(`/courses/${id}/edit`, data);
  },
  delete: async (id: string) => {
    return await axiosInstance.delete<DisplayCourseResponse>(`/courses/${id}/delete`);
  }
};

export default courseApi;