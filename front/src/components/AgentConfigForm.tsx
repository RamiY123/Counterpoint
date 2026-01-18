import { useState } from 'react';
import { AgentConfig } from '../types';

interface AgentConfigFormProps {
  label: string;
  color: string;
  value: AgentConfig;
  onChange: (config: AgentConfig) => void;
}

export function AgentConfigForm({ label, color, value, onChange }: AgentConfigFormProps) {
  const [isExpanded, setIsExpanded] = useState(true);

  const handleChange = (field: keyof AgentConfig, val: string) => {
    onChange({ ...value, [field]: val });
  };

  return (
    <div className={`border-2 ${color} rounded-lg p-4`}>
      <button
        type="button"
        onClick={() => setIsExpanded(!isExpanded)}
        className="w-full flex justify-between items-center text-lg font-semibold mb-2"
      >
        <span>{label}</span>
        <span>{isExpanded ? '▼' : '▶'}</span>
      </button>

      {isExpanded && (
        <div className="space-y-3">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Agent Name
            </label>
            <input
              type="text"
              value={value.name}
              onChange={(e) => handleChange('name', e.target.value)}
              placeholder="e.g., The Pragmatist"
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Stance / Position
            </label>
            <textarea
              value={value.stance}
              onChange={(e) => handleChange('stance', e.target.value)}
              placeholder="e.g., Strongly in favor of..."
              rows={2}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Personality (Optional)
            </label>
            <input
              type="text"
              value={value.personality || ''}
              onChange={(e) => handleChange('personality', e.target.value)}
              placeholder="e.g., Analytical and data-driven"
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
        </div>
      )}
    </div>
  );
}
