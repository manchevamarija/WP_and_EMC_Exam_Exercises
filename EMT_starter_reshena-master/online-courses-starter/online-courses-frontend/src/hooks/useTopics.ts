import { useCallback, useEffect, useState } from 'react';
import useSnackbar from './useSnackbar.ts';
import type { DisplayTopicResponse } from '../api/types/topic.ts';
import topicApi from '../api/topicApi.ts';

const useTopics = () => {
  const { showSnackbar } = useSnackbar();

  const [topics, setTopics] = useState<DisplayTopicResponse[]>([]);
  const [loading, setLoading] = useState<boolean>(false);

  const fetch = useCallback(async () => {
    setLoading(true);

    try {
      const response = await topicApi.findAll();
      setTopics(response.data);
    } catch (err) {
      showSnackbar(err instanceof Error ? err.message : 'Failed to load topics.', 'error');
    } finally {
      setLoading(false);
    }
  }, [showSnackbar]);

  useEffect(() => {
    void fetch();
  }, [fetch]);

  return { topics, loading, fetch };
};

export default useTopics;