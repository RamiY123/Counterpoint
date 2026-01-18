export interface AgentConfig {
  name: string;
  stance: string;
  personality?: string;
}

export interface ConfigRequest {
  topic: string;
  language: string;
  rounds: number;
  agentA: AgentConfig;
  agentB: AgentConfig;
}

export interface SessionResponse {
  debateId: number;
  topic: string;
  language: string;
  totalRounds: number;
  status: DebateStatus;
}

export type DebateStatus = 'PENDING' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';

export type AgentRole = 'PROPONENT' | 'OPPONENT';

export interface MessageDto {
  id: number;
  content: string;
  agentName: string;
  agentRole: AgentRole;
  roundNumber: number;
  sequenceNumber: number;
  timestamp: string;
  type: 'message';
}

export interface StatusUpdate {
  type: 'status';
  status: string;
  message: string;
  timestamp: string;
}

export interface ConclusionUpdate {
  type: 'conclusion';
  conclusion: string;
  timestamp: string;
}

export interface ErrorUpdate {
  type: 'error';
  message: string;
  timestamp: string;
}

export type WebSocketMessage = MessageDto | StatusUpdate | ConclusionUpdate | ErrorUpdate;

export interface AgentDto {
  id: number;
  name: string;
  stance: string;
  role: AgentRole;
}

export interface HistoryResponse {
  debateId: number;
  topic: string;
  language: string;
  status: DebateStatus;
  totalRounds: number;
  currentRound: number;
  conclusion?: string;
  agents: AgentDto[];
  messages: MessageDto[];
}
