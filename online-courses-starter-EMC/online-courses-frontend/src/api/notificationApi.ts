import axiosInstance from '../axios/axios.ts';
import type { DisplayNotificationResponse } from './types/notification.ts';

const notificationApi = {
  findAllByUser: async () => {
    return await axiosInstance.get<DisplayNotificationResponse[]>('/notifications/me');
  }
};

export default notificationApi;