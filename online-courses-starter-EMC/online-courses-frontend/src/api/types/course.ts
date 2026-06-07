import type { DisplayTopicResponse } from './topic.ts';

export interface CreateOrUpdateCourseRequest {
  title: string;
  description: string;
  topicId: number;
  price: number;
  capacity: number;
  startDate: string;
  endDate: string;
}

export interface DisplayCourseResponse {
  id: number;
  title: string;
  description: string;
  topicId: number,
  price: number;
  capacity: number;
  startDate: string;
  endDate: string;
}

export interface DisplayCourseDetailsResponse {
  id: number;
  title: string;
  description: string;
  topic: DisplayTopicResponse,
  price: number;
  capacity: number;
  startDate: string,
  endDate: string;
  durationInDays: number
}