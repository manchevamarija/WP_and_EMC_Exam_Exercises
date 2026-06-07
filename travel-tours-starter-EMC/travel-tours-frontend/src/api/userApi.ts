import axiosInstance from '../axios/axios.ts';
import type { LoginUserRequest, LoginUserResponse, RegisterUserRequest, RegisterUserResponse } from './types/user.ts';

const userApi = {
  register: async (data: RegisterUserRequest) => {
    return await axiosInstance.post<RegisterUserResponse>('/user/register', data);
  },
  login: async (data: LoginUserRequest) => {
    return await axiosInstance.post<LoginUserResponse>('/user/login', data);
  }
};

export default userApi;