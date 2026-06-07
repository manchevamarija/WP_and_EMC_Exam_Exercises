import { useCallback, useEffect, useState } from 'react';
import useSnackbar from './useSnackbar.ts';
import type { DisplayDestinationResponse } from '../api/types/destination.ts';
import destinationApi from '../api/destinationApi.ts';

const useDestinations = () => {
  const { showSnackbar } = useSnackbar();

  const [destinations, setDestinations] = useState<DisplayDestinationResponse[]>([]);
  const [loading, setLoading] = useState<boolean>(false);

  const fetch = useCallback(async () => {
    setLoading(true);

    try {
      const response = await destinationApi.findAll();
      setDestinations(response.data);
    } catch (err) {
      showSnackbar(err instanceof Error ? err.message : 'Failed to load destinations.', 'error');
    } finally {
      setLoading(false);
    }
  }, [showSnackbar]);

  useEffect(() => {
    void fetch();
  }, [fetch]);

  return { destinations, loading, fetch };
};

export default useDestinations;