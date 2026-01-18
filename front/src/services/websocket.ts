import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { WebSocketMessage } from '../types';

const WS_URL = 'http://localhost:8081/ws';

export class DebateWebSocket {
  private client: Client;
  private debateId: number;
  private onMessage: (message: WebSocketMessage) => void;

  constructor(debateId: number, onMessage: (message: WebSocketMessage) => void) {
    this.debateId = debateId;
    this.onMessage = onMessage;

    this.client = new Client({
      webSocketFactory: () => new SockJS(WS_URL) as WebSocket,
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    this.client.onConnect = () => {
      console.log('WebSocket connected');
      this.subscribe();
    };

    this.client.onDisconnect = () => {
      console.log('WebSocket disconnected');
    };

    this.client.onStompError = (frame) => {
      console.error('STOMP error:', frame.headers['message']);
    };
  }

  connect(): void {
    this.client.activate();
  }

  disconnect(): void {
    if (this.client.active) {
      this.client.deactivate();
    }
  }

  private subscribe(): void {
    this.client.subscribe(`/topic/debate/${this.debateId}`, (message) => {
      try {
        const data = JSON.parse(message.body) as WebSocketMessage;
        this.onMessage(data);
      } catch (error) {
        console.error('Failed to parse message:', error);
      }
    });
  }
}
