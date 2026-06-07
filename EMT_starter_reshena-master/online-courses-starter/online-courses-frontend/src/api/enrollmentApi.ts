import axiosInstance from '../axios/axios.ts';
import type { DisplayEnrollmentDetailsResponse } from './types/enrollment.ts';

class DisplayEnrollmentResponseDto {
}

const enrollmentApi = {
  findAllWithDetailsByUser: async () => {
    return await axiosInstance.get<DisplayEnrollmentDetailsResponse[]>('/enrollments');
  },
  existsByCourseAndUser: async (courseId: number) => {
    return await axiosInstance.get<boolean>(`/enrollments/course/${courseId}/is-enrolled`);
  },
  getAvailableSpotsByCourse: async (courseId: number) => {
    return await axiosInstance.get<number>(`/enrollments/course/${courseId}/available-spots`);
  },
  enrollByCourse: async (courseId: number) => {
    return await axiosInstance.post<DisplayEnrollmentResponseDto>(`/enrollments/course/${courseId}/enroll`);
  },
  unenrollByCourse: async (courseId: number) => {
    return await axiosInstance.delete<DisplayEnrollmentResponseDto>(`/enrollments/course/${courseId}/unenroll`);
  }
};

export default enrollmentApi;