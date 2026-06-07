import { useCallback, useEffect, useMemo, useState } from 'react';
import * as React from 'react';
import useSnackbar from '../hooks/useSnackbar.ts';
import type { DisplayEnrollmentDetailsResponse } from '../api/types/enrollment.ts';
import enrollmentApi from '../api/enrollmentApi.ts';
import EnrollmentsContext from '../contexts/enrollmentsContext.ts';

const EnrollmentsProvider = ({ children }: { children: React.ReactNode }) => {
  const { showSnackbar } = useSnackbar();

  const [enrollments, setEnrollments] = useState<DisplayEnrollmentDetailsResponse[]>([]);
  const [loading, setLoading] = useState<boolean>(false);

  const fetch = useCallback(async () => {
    setLoading(true);

    try {
      const response = await enrollmentApi.findAllWithDetailsByUser();
      setEnrollments(response.data);
    } catch (err) {
      showSnackbar(err instanceof Error ? err.message : 'Failed to load enrollments.', 'error');
    } finally {
      setLoading(false);
    }
  }, [showSnackbar]);

  const onUnenroll = useCallback(async (courseId: number) => {
    try {
        await enrollmentApi.unenrollByCourse(courseId);
        await fetch();
    } catch (err) {
      showSnackbar(err instanceof Error ? err.message : 'Failed to unenroll.', 'error');
    }
  }, [fetch, showSnackbar]);

  useEffect(() => {
    void fetch();
  }, [fetch]);

  const value = useMemo(
    () => ({ enrollments, loading, onUnenroll }),
    [enrollments, loading, onUnenroll]
  );

  return <EnrollmentsContext value={value}>{children}</EnrollmentsContext>;
};

export default EnrollmentsProvider;