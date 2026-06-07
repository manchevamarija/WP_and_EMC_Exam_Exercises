import { useCallback, useEffect, useMemo, useState } from 'react';
import * as React from 'react';
import useSnackbar from '../hooks/useSnackbar.ts';
import type { CreateOrUpdateCourseRequest, DisplayCourseResponse } from '../api/types/course.ts';
import courseApi from '../api/courseApi.ts';
import CoursesContext from '../contexts/coursesContext.ts';

const CoursesProvider = ({ children }: { children: React.ReactNode }) => {
  const { showSnackbar } = useSnackbar();

  const [courses, setCourses] = useState<DisplayCourseResponse[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [availabilityFilter, setAvailabilityFilter] = useState<string>('all');

  const fetch = useCallback(async () => {
    setLoading(true);

    try {
        const response = await courseApi.findAll(availabilityFilter);
        setCourses(response.data);
    } catch (err) {
      showSnackbar(err instanceof Error ? err.message : 'Failed to load products.', 'error');
    } finally {
      setLoading(false);
    }
  }, [availabilityFilter, showSnackbar]);

  const onAdd = useCallback(async (data: CreateOrUpdateCourseRequest) => {
    try {
        await courseApi.create(data);
        await fetch();
    } catch (err) {
      showSnackbar(err instanceof Error ? err.message : 'Failed to add course.', 'error');
    }
  }, [fetch, showSnackbar]);

  const onEdit = useCallback(async (id: number, data: CreateOrUpdateCourseRequest) => {
    try {
        await courseApi.update(id.toString(), data);
        await fetch();
    } catch (err) {
      showSnackbar(err instanceof Error ? err.message : 'Failed to edit course.', 'error');
    }
  }, [fetch, showSnackbar]);

  const onDelete = useCallback(async (id: number) => {
    try {
        await courseApi.delete(id.toString());
        await fetch();
    } catch (err) {
      showSnackbar(err instanceof Error ? err.message : 'Failed to delete course.', 'error');
    }
  }, [fetch, showSnackbar]);

  useEffect(() => {
    void fetch();
  }, [fetch]);

  const value = useMemo(
    () => ({ courses, loading, availabilityFilter, setAvailabilityFilter, onAdd, onEdit, onDelete }),
    [courses, loading, availabilityFilter, onAdd, onEdit, onDelete]
  );

  return <CoursesContext value={value}>{children}</CoursesContext>;
};

export default CoursesProvider;