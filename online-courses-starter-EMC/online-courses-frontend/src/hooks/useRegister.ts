import { useState } from 'react';
import userApi from '../api/userApi.ts';
import { useNavigate } from 'react-router';
import type { RegisterUserRequest } from '../api/types/user.ts';
import useSnackbar from './useSnackbar.ts';
import { getErrorMessage } from '../api/getErrorMessage.ts';

const useRegister = () => {
  const navigate = useNavigate();
  const { showSnackbar } = useSnackbar();
  const [loading, setLoading] = useState<boolean>(false);

  const register = async (data: RegisterUserRequest) => {
    setLoading(true);

    try {
      await userApi.register(data);
      navigate('/login');
    } catch (err) {
      showSnackbar(getErrorMessage(err, 'Registration failed. Please try again!'), 'error');
    } finally {
      setLoading(false);
    }
  };

  return { loading, register };
};

export default useRegister;
