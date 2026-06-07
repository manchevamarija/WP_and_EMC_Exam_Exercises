import { useCallback, useEffect, useState } from 'react';
import useSnackbar from './useSnackbar.ts';
import type { DisplayCourseDetailsResponse } from '../api/types/course.ts';
import courseApi from '../api/courseApi.ts';
import { getErrorMessage } from '../api/getErrorMessage.ts';
import enrollmentApi from '../api/enrollmentApi.ts';

const useCourseDetails = (id?: string) => {
  const { showSnackbar } = useSnackbar();

  const [courseDetails, setCourseDetails] = useState<DisplayCourseDetailsResponse | null>(null);
  const [availableSpots, setAvailableSpots] = useState<number | null>(null);
  const [isEnrolled, setIsEnrolled] = useState<boolean | null>(null);

  const fetch = useCallback(async () => {
    if (!id) {
      return;
    }

    const courseId = Number(id);

    try {
        const response = await courseApi.findWithDetailsById(courseId);
        setCourseDetails(response.data);
    } catch (err) {
      showSnackbar(getErrorMessage(err, 'Failed to load course details.'), 'error');
    }

    try {
        const response = await enrollmentApi.getAvailableSpotsByCourse(courseId);
        setAvailableSpots(response.data);
    } catch (err) {
      showSnackbar(getErrorMessage(err, 'Failed to load available spots.'), 'error');
    }

    try {
        const response = await enrollmentApi.existsByCourseAndUser(courseId);
        setIsEnrolled(response.data);
    } catch (err) {
      showSnackbar(getErrorMessage(err, 'Failed to load enrollment status.'), 'error');
    }
  }, [id, showSnackbar]);

  const enroll = useCallback(async () => {
      if (!id) {
          return;
      }

      const courseId = Number(id);
      try {
        await enrollmentApi.enrollByCourse(courseId);
        await fetch();
    } catch (err) {
      showSnackbar(err instanceof Error ? err.message : 'Failed to enroll.', 'error');
    }
  }, [fetch, id, showSnackbar]);

    useEffect(() => {
        void fetch();
    }, [fetch]);

  return { courseDetails, availableSpots, isEnrolled, enroll};
};

export default useCourseDetails;
