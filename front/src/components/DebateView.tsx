import { useEffect, useRef, useState } from 'react';
import { MessageDto, WebSocketMessage, SessionResponse } from '../types';
import { DebateWebSocket } from '../services/websocket';

interface DebateViewProps {
  session: SessionResponse;
  onReset: () => void;
}

export function DebateView({ session, onReset }: DebateViewProps) {
  const [messages, setMessages] = useState<MessageDto[]>([]);
  const [status, setStatus] = useState<string>('Connecting...');
  const [error, setError] = useState<string | null>(null);
  const [conclusion, setConclusion] = useState<string | null>(null);
  const [isComplete, setIsComplete] = useState(false);
  const messagesEndRef = useRef<HTMLDivElement>(null);
  const wsRef = useRef<DebateWebSocket | null>(null);

  useEffect(() => {
    const handleMessage = (message: WebSocketMessage) => {
      if (message.type === 'message') {
        setMessages((prev) => [...prev, message as MessageDto]);
      } else if (message.type === 'status') {
        setStatus(message.message);
        if (message.status === 'completed') {
          setIsComplete(true);
        }
      } else if (message.type === 'conclusion') {
        setConclusion(message.conclusion);
      } else if (message.type === 'error') {
        setError(message.message);
        setIsComplete(true);
      }
    };

    wsRef.current = new DebateWebSocket(session.debateId, handleMessage);
    wsRef.current.connect();

    return () => {
      wsRef.current?.disconnect();
    };
  }, [session.debateId]);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, conclusion]);

  const getAgentColor = (role: string) => {
    return role === 'PROPONENT' ? 'bg-blue-100 border-blue-500' : 'bg-red-100 border-red-500';
  };

  const getAgentTextColor = (role: string) => {
    return role === 'PROPONENT' ? 'text-blue-700' : 'text-red-700';
  };

  return (
    <div className="max-w-4xl mx-auto">
      <div className="bg-white rounded-lg shadow-lg overflow-hidden">
        {/* Header */}
        <div className="bg-gradient-to-r from-blue-600 to-purple-600 p-4">
          <h2 className="text-xl font-bold text-white">{session.topic}</h2>
          <p className="text-blue-100 text-sm mt-1">
            {session.language === 'ar' ? 'العربية' : 'English'} • {session.totalRounds} Rounds
          </p>
        </div>

        {/* Status Bar */}
        <div className="bg-gray-100 px-4 py-2 flex justify-between items-center">
          <span className="text-sm text-gray-600">{status}</span>
          {!isComplete && (
            <span className="flex items-center text-sm text-green-600">
              <span className="animate-pulse mr-2">●</span> Live
            </span>
          )}
        </div>

        {/* Error Display */}
        {error && (
          <div className="bg-red-50 border-l-4 border-red-500 p-4 m-4">
            <div className="flex">
              <div className="flex-shrink-0">
                <span className="text-red-500 text-xl">⚠️</span>
              </div>
              <div className="ml-3">
                <p className="text-sm text-red-700 whitespace-pre-wrap">{error}</p>
              </div>
            </div>
          </div>
        )}

        {/* Messages */}
        <div className="p-4 space-y-4 max-h-[500px] overflow-y-auto">
          {messages.map((msg, index) => (
            <div
              key={index}
              className={`p-4 rounded-lg border-l-4 ${getAgentColor(msg.agentRole)}`}
            >
              <div className="flex justify-between items-start mb-2">
                <span className={`font-semibold ${getAgentTextColor(msg.agentRole)}`}>
                  {msg.agentRole === 'PROPONENT' ? '🔵' : '🔴'} {msg.agentName}
                </span>
                <span className="text-xs text-gray-500">
                  Round {msg.roundNumber}
                </span>
              </div>
              <p className="text-gray-800 whitespace-pre-wrap">{msg.content}</p>
            </div>
          ))}

          {/* Conclusion */}
          {conclusion && (
            <div className="p-4 rounded-lg border-2 border-purple-500 bg-purple-50">
              <div className="flex items-center mb-2">
                <span className="text-xl mr-2">⚖️</span>
                <span className="font-bold text-purple-700">Debate Conclusion</span>
              </div>
              <p className="text-gray-800 whitespace-pre-wrap">{conclusion}</p>
            </div>
          )}

          <div ref={messagesEndRef} />
        </div>

        {/* Footer */}
        {isComplete && (
          <div className="bg-gray-100 p-4 flex justify-center">
            <button
              onClick={onReset}
              className="px-6 py-2 bg-blue-600 text-white font-semibold rounded-lg hover:bg-blue-700 transition-colors"
            >
              Start New Debate
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
