import { useCallback, useEffect, useState } from 'react';
import type { DisplayNotificationResponse } from '../api/types/notification.ts';
import notificationApi from '../api/notificationApi.ts';
import useSnackbar from './useSnackbar.ts';

const useNotifications = () => {
  const [notifications, setNotifications] = useState<DisplayNotificationResponse[]>([]);
  const [loading, setLoading] = useState<boolean>(false);

  const { showSnackbar } = useSnackbar();

  const fetch = useCallback(async () => {
    setLoading(true);

    try {
      const response = await notificationApi.findAllByUser();
      setNotifications(response.data);
    } catch (err) {
      showSnackbar(err instanceof Error ? err.message : 'Failed to load notifications.', 'error');
    } finally {
      setLoading(false);
    }
  }, [showSnackbar]);

  useEffect(() => {
    void fetch();
  }, [fetch]);

  return { notifications, loading, fetch };
};

export default useNotifications;
