export interface CreateOrUpdateTopicRequest {
  name: string;
  description: string;
}

export interface DisplayTopicResponse {
  id: number;
  name: string;
  description: string;
}