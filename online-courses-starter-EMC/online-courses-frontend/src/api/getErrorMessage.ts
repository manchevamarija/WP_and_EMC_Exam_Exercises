import axios from 'axios';

export const getErrorMessage = (err: unknown, fallback: string): string => {
  if (axios.isAxiosError(err)) {
    return err.response?.data?.message ?? fallback;
  }
  return err instanceof Error ? err.message : fallback;
};
