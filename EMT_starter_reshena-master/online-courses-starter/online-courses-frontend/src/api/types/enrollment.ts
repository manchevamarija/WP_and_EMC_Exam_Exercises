import type { DisplayCourseDetailsResponse, DisplayCourseResponse } from './course.ts';

export interface DisplayEnrollmentResponse {
  id: number;
  course: DisplayCourseResponse,
  username: string
}

export interface DisplayEnrollmentDetailsResponse {
  id: number;
  course: DisplayCourseDetailsResponse,
  username: string
}
