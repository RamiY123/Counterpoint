import { ConfigRequest, SessionResponse, HistoryResponse } from '../types';

const API_BASE = 'http://localhost:8081/api';

export const debateApi = {
  configure: async (config: ConfigRequest): Promise<SessionResponse> => {
    const response = await fetch(`${API_BASE}/debate/configure`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(config),
    });
    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Failed to configure debate');
    }
    return response.json();
  },

  start: async (debateId: number): Promise<void> => {
    const response = await fetch(`${API_BASE}/debate/${debateId}/start`, {
      method: 'POST',
    });
    if (!response.ok) {
      throw new Error('Failed to start debate');
    }
  },

  getHistory: async (debateId: number): Promise<HistoryResponse> => {
    const response = await fetch(`${API_BASE}/debate/${debateId}`);
    if (!response.ok) {
      throw new Error('Failed to get debate history');
    }
    return response.json();
  },

  getStatus: async (debateId: number): Promise<SessionResponse> => {
    const response = await fetch(`${API_BASE}/debate/${debateId}/status`);
    if (!response.ok) {
      throw new Error('Failed to get debate status');
    }
    return response.json();
  },
};

export const configApi = {
  getStatus: async (): Promise<{ apiKeyConfigured: boolean; message: string }> => {
    const response = await fetch(`${API_BASE}/config/status`);
    if (!response.ok) {
      throw new Error('Failed to get config status');
    }
    return response.json();
  },

  setApiKey: async (apiKey: string): Promise<{ success: boolean; message: string }> => {
    const response = await fetch(`${API_BASE}/config/api-key`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ apiKey }),
    });
    return response.json();
  },
};
