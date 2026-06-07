import { useState } from 'react';
import userApi from '../api/userApi.ts';
import { useNavigate } from 'react-router';
import useAuth from './useAuth.ts';
import type { LoginUserRequest } from '../api/types/user.ts';
import useSnackbar from './useSnackbar.ts';
import { getErrorMessage } from '../api/getErrorMessage.ts';

const useLogin = () => {
  const navigate = useNavigate();
  const { login: authLogin } = useAuth();
  const { showSnackbar } = useSnackbar();
  const [loading, setLoading] = useState<boolean>(false);

  const login = async (data: LoginUserRequest) => {
    setLoading(true);

    try {
      const response = await userApi.login(data);
      authLogin(response.data.token);
      navigate('/');
    } catch (err) {
      showSnackbar(getErrorMessage(err, 'Invalid username or password.'), 'error');
    } finally {
      setLoading(false);
    }
  };

  return { loading, login };
};

export default useLogin;