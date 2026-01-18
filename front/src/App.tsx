import { useState, useEffect } from 'react';
import { TopicForm } from './components/TopicForm';
import { DebateView } from './components/DebateView';
import { ApiKeyPrompt } from './components/ApiKeyPrompt';
import { debateApi, configApi } from './services/api';
import { ConfigRequest, SessionResponse } from './types';
import './App.css';

function App() {
  const [session, setSession] = useState<SessionResponse | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [checkingApiKey, setCheckingApiKey] = useState(true);
  const [needsApiKey, setNeedsApiKey] = useState(false);

  useEffect(() => {
    checkApiKey();
  }, []);

  const checkApiKey = async () => {
    try {
      const status = await configApi.getStatus();
      setNeedsApiKey(!status.apiKeyConfigured);
    } catch {
      // If we can't reach the server, proceed anyway
      setNeedsApiKey(false);
    } finally {
      setCheckingApiKey(false);
    }
  };

  const handleSubmit = async (config: ConfigRequest) => {
    setIsLoading(true);
    try {
      const response = await debateApi.configure(config);
      setSession(response);
      await debateApi.start(response.debateId);
    } catch (error) {
      console.error('Failed to start debate:', error);
      alert('Failed to start debate. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleReset = () => {
    setSession(null);
  };

  const handleApiKeySuccess = () => {
    setNeedsApiKey(false);
  };

  if (checkingApiKey) {
    return (
      <div className="min-h-screen bg-gray-100 flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Loading...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-100 py-8 px-4">
      <header className="text-center mb-8">
        <h1 className="text-4xl font-bold bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent">
          ⚔️ Counterpoint
        </h1>
        <p className="text-gray-600 mt-2">Intelligence through opposition</p>
      </header>

      <main>
        {needsApiKey ? (
          <ApiKeyPrompt onSuccess={handleApiKeySuccess} />
        ) : session ? (
          <DebateView session={session} onReset={handleReset} />
        ) : (
          <TopicForm onSubmit={handleSubmit} isLoading={isLoading} />
        )}
      </main>
    </div>
  );
}

export default App;
