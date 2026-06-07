import type { JwtPayload } from 'jwt-decode';

export type Role = 'ROLE_CUSTOMER' | 'ROLE_GUIDE' | 'ROLE_ADMINISTRATOR';

export interface RegisterUserRequest {
  name: string;
  surname: string;
  email: string;
  username: string;
  password: string;
}

export interface RegisterUserResponse {
  username: string;
  name: string;
  surname: string;
  email: string;
  role: Role;
}

export interface LoginUserRequest {
  username: string;
  password: string;
}

export interface LoginUserResponse {
  token: string;
}

export interface UserPayload extends JwtPayload {
  username: string;
  roles: string[];
}