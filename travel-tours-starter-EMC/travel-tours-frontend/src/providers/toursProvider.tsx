import { useCallback, useEffect, useMemo, useState } from 'react';
import * as React from 'react';
import useSnackbar from '../hooks/useSnackbar.ts';
import type { CreateOrUpdateTourRequest, DisplayTourResponse } from '../api/types/tour.ts';
import tourApi from '../api/tourApi.ts';
import ToursContext from '../contexts/toursContext.ts';

const ToursProvider = ({ children }: { children: React.ReactNode }) => {
  const { showSnackbar } = useSnackbar();

  const [tours, setTours] = useState<DisplayTourResponse[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [availabilityFilter, setAvailabilityFilter] = useState<string>('all');

  const fetch = useCallback(async () => {
    setLoading(true);

    try {
      const response = await tourApi.findAll(availabilityFilter);
      setTours(response.data);
    } catch (err) {
      showSnackbar(err instanceof Error ? err.message : 'Failed to load tours.', 'error');
    } finally {
      setLoading(false);
    }
  }, [availabilityFilter, showSnackbar]);

  // TODO: Implement this.
  const onAdd =
      useCallback(async (data: CreateOrUpdateTourRequest) => {
          try {
              await tourApi.create(data);
              await fetch();
          } catch (err) {
              showSnackbar(err instanceof Error ? err.message : 'Failed to add product.', 'error');
          }
      }, [fetch, showSnackbar]);


    // TODO: Implement this.
  const onEdit =
      useCallback(async (id: number, data: CreateOrUpdateTourRequest) => {
          try {
              await tourApi.update(id.toString(), data);
              await fetch();
          } catch (err) {
              showSnackbar(err instanceof Error ? err.message : 'Failed to edit product.', 'error');
          }
      }, [fetch, showSnackbar]);


    // TODO: Implement this.
  const onDelete =
      useCallback(async (id: number) => {
          try {
              await tourApi.delete(id.toString());
              await fetch();
          } catch (err) {
              showSnackbar(err instanceof Error ? err.message : 'Failed to delete product.', 'error');
          }
      }, [fetch, showSnackbar]);


    useEffect(() => {
    void fetch();
  }, [fetch]);

  const value = useMemo(
    () => ({ tours, loading, availabilityFilter, setAvailabilityFilter, onAdd, onEdit, onDelete }),
    [tours, loading, availabilityFilter, onAdd, onEdit, onDelete]
  );

  return <ToursContext value={value}>{children}</ToursContext>;
};

export default ToursProvider;