import { useCallback, useEffect, useMemo, useState } from 'react';
import * as React from 'react';
import useSnackbar from '../hooks/useSnackbar.ts';
import type { DisplayReservationDetailsResponse } from '../api/types/reservation.ts';
import reservationApi from '../api/reservationApi.ts';
import ReservationsContext from '../contexts/reservationsContext.ts';

const ReservationsProvider = ({ children }: { children: React.ReactNode }) => {
  const { showSnackbar } = useSnackbar();

  const [reservations, setReservations] = useState<DisplayReservationDetailsResponse[]>([]);
  const [loading, setLoading] = useState<boolean>(false);

  const fetch = useCallback(async () => {
    setLoading(true);

    try {
      const response = await reservationApi.findAllWithDetailsByUser();
      setReservations(response.data);
    } catch (err) {
      showSnackbar(err instanceof Error ? err.message : 'Failed to load reservations.', 'error');
    } finally {
      setLoading(false);
    }
  }, [showSnackbar]);

  // TODO: Implement this.
  const onCancel = useCallback(async (tourId: number) => {
      try {
          await reservationApi.cancelByTour(tourId);
          await fetch();
      } catch (err) {
          showSnackbar(err instanceof Error ? err.message : 'Failed to delete product.', 'error');
      }
  },
      [fetch, showSnackbar]);


    useEffect(() => {
    void fetch();
  }, [fetch]);

  const value = useMemo(
    () => ({ reservations, loading, onCancel }),
    [reservations, loading, onCancel]
  );

  return <ReservationsContext value={value}>{children}</ReservationsContext>;
};

export default ReservationsProvider;