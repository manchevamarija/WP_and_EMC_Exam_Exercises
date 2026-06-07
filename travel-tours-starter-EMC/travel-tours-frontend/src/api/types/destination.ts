export interface CreateOrUpdateDestinationRequest {
  name: string;
  description: string;
}

export interface DisplayDestinationResponse {
  id: number;
  name: string;
  description: string;
}