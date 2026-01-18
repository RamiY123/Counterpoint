# Counterpoint

> **Intelligence through opposition.**

![Status](https://img.shields.io/badge/Status-Prototype-orange)
![Stack](https://img.shields.io/badge/Stack-Spring_AI_WebSockets-blue)

## 📖 The Pitch

True insight doesn't come from agreement; it comes from the friction between opposing ideas.

**Counterpoint** is an AI simulation platform that engineers structured disagreement. By instantiating autonomous agents with conflicting "Prime Directives," Counterpoint allows leaders to stress-test their strategies against their harshest critics—without the social cost of a real-world argument.

**Don't just look for a consensus. Engineer a better one.**

---

## ⚠️ The Problem: "The Agreement Trap"

Strategic decision-making is often paralyzed by social dynamics.
* **Echo Chambers:** Teams gravitate toward the safest option to avoid conflict.
* **The "Yes-Man" AI:** Standard LLMs are trained to be helpful and agreeable, often drifting toward a "balanced average" rather than exposing edge-case risks.
* **Hidden Risks:** You rarely hear the raw, unvarnished downsides of a technical or business move until the project fails.

## 💡 The Solution

Counterpoint creates a safe harbor for rigorous debate. It allows you to configure two distinct **AI Personas**—each locked into a specific worldview—and orchestrates a multi-round, real-time dialectic.

It captures the *tension* between stability and speed, or risk and growth, giving you a transcript of the arguments you *should* be having.

---

## ⚡ Key Capabilities

### 1. 🎭 Adversarial Persona Configuration
Define your fighters via API. Pit distinct archetypes against each other to see where the sparks fly.
* *Example:* "The Purist Architect" (Values perfect abstractions) **VS** "The Pragmatic Founder" (Values shipping today).

### 2. ⚡ Real-Time Dialectic Stream
Watch the argument unfold live. Using **WebSockets**, Counterpoint streams the debate bubble-by-bubble. Witness the agents listen, dismantle each other's points, and rebuild their cases in real-time.

### 3. 🧠 Logic-Locked Agents
Counterpoint agents are architecturally constrained to maintain their stance. They cannot "agree to disagree" until they have exhausted their logical arguments, ensuring a deep exploration of the topic.

### 4. 🌍 Multi-Language Support
Configure debates in **English** or **Arabic** to explore perspectives across languages.

### 5. 📊 Debate Conclusion
At the end of each debate, Counterpoint generates a synthesized conclusion highlighting the key arguments from both sides.

---

## 🎯 Use Cases

| Domain | Agent A | Agent B | The Goal |
| :--- | :--- | :--- | :--- |
| **Tech Stack** | **"Rust Evangelist"**<br>(Focus: Performance, Safety) | **"Go Pragmatist"**<br>(Focus: Velocity, Simplicity) | Decide the language for the new microservice. |
| **Product** | **"Data Driven"**<br>(Focus: A/B Test everything) | **"Intuition First"**<br>(Focus: Brand cohesion, Speed) | Decide on a controversial UI change. |
| **Security** | **"Zero Trust"**<br>(Focus: Maximum Security) | **"User Friction"**<br>(Focus: Low Barriers) | Balance UX with authentication requirements. |

---

## 🛠 Technical Stack

* **Backend:** Java 17+, Spring Boot 3.4.1
* **AI Engine:** Spring AI 1.0.0-M5 (OpenAI GPT-4o)
* **Real-Time:** Spring WebSocket (STOMP)
* **Frontend:** React 19 + Vite + Tailwind CSS
* **Database:** H2 (dev) / PostgreSQL (prod)

---

## 🚀 Quick Start Guide

### Prerequisites

Before running Counterpoint, ensure you have:

| Requirement | Version | Check Command |
|-------------|---------|---------------|
| **Java** | 17 or higher | `java -version` |
| **Node.js** | 18 or higher | `node --version` |
| **npm** | 9 or higher | `npm --version` |
| **OpenAI API Key** | - | [Get one here](https://platform.openai.com/api-keys) |

### Step 1: Configure OpenAI API Key

You have **three options** to configure your API key:

#### Option A: Environment Variable (Recommended)
```powershell
# PowerShell
$env:OPENAI_API_KEY="sk-your-api-key-here"
```
```cmd
# Windows CMD
set OPENAI_API_KEY=sk-your-api-key-here
```
```bash
# Linux/Mac
export OPENAI_API_KEY="sk-your-api-key-here"
```

#### Option B: Edit Configuration File
Edit `demo/src/main/resources/application-dev.yml`:
```yaml
spring:
  ai:
    openai:
      api-key: sk-your-api-key-here
```

#### Option C: Enter via UI
If you start the application without an API key, you'll be prompted to enter it in the web interface.

### Step 2: Run the Application

#### Using the Startup Scripts (Easiest)

**PowerShell:**
```powershell
.\start.ps1
```

**Windows CMD:**
```cmd
start.bat
```

The scripts will:
1. ✅ Start the Spring Boot backend on port **8081**
2. ✅ Install frontend dependencies (first run only)
3. ✅ Start the React frontend on port **5173**

#### Manual Start (Alternative)

**Terminal 1 - Backend:**
```powershell
cd demo
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

**Terminal 2 - Frontend:**
```powershell
cd front
npm install    # First time only
npm run dev
```

### Step 3: Access the Application

| Component | URL |
|-----------|-----|
| 🖥️ **Frontend** | [http://localhost:5173](http://localhost:5173) |
| ⚙️ **Backend API** | [http://localhost:8081](http://localhost:8081) |
| 🗄️ **H2 Console** | [http://localhost:8081/h2-console](http://localhost:8081/h2-console) |

---

## 📁 Project Structure

```
Mastering_AI_Coding/
├── 📄 start.ps1           # PowerShell startup script
├── 📄 start.bat           # Windows CMD startup script
├── 📄 README.md           # This file
│
├── 📁 demo/               # Spring Boot Backend
│   ├── pom.xml
│   └── src/main/java/com/example/demo/
│       ├── config/        # WebSocket, OpenAI, Async configs
│       ├── controller/    # REST & WebSocket controllers
│       ├── dto/           # Data Transfer Objects
│       ├── model/         # JPA Entities
│       ├── repository/    # Database repositories
│       └── service/       # Business logic (AgentService, DebateOrchestrator)
│
└── 📁 front/              # React Frontend
    └── src/
        ├── components/    # React components (TopicForm, DebateView, etc.)
        ├── services/      # API & WebSocket clients
        └── types/         # TypeScript definitions
```

---

## 🎮 How to Use

1. **Open the app** at [http://localhost:5173](http://localhost:5173)
2. **Enter API Key** if prompted (or configure beforehand)
3. **Configure the Debate:**
   - Enter a topic (e.g., "Should companies adopt AI for hiring?")
   - Define Agent A's name and stance (e.g., "The Innovator" - Pro AI)
   - Define Agent B's name and stance (e.g., "The Ethicist" - Anti AI)
   - Select language (🇺🇸 English or 🇸🇦 Arabic)
   - Set number of rounds (default: 3)
4. **Start the Debate** and watch the AI agents argue in real-time!
5. **View the Conclusion** generated at the end summarizing both perspectives

---

## 🔧 Configuration

### Backend Configuration (`application-dev.yml`)

| Property | Default | Description |
|----------|---------|-------------|
| `server.port` | 8081 | Backend server port |
| `spring.ai.openai.api-key` | - | Your OpenAI API key |
| `spring.ai.openai.chat.options.model` | gpt-4o | AI model to use |
| `app.debate.max-response-words` | 100 | Max words per AI response |

### Frontend Configuration

The frontend connects to `http://localhost:8081` by default. To change this, edit `front/src/services/api.ts`.

---

## 🐛 Troubleshooting

### "Invalid OpenAI API Key" Error
```
❌ Invalid OpenAI API Key

Your API key is missing or invalid. Please configure it:

🔧 Option 1: Environment Variable
   PowerShell: $env:OPENAI_API_KEY="sk-your-key"
   Then restart the server

🔧 Option 2: Edit application-dev.yml
   spring.ai.openai.api-key: sk-your-key

🔗 Get your key: https://platform.openai.com/api-keys
```

### Port Already in Use
If port 8081 or 5173 is busy:
- Backend: Change `server.port` in `application-dev.yml`
- Frontend: Run `npm run dev -- --port 3000`

### WebSocket Connection Failed
Ensure the backend is running before starting the frontend. The WebSocket connects to `ws://localhost:8081/ws`.

---

## 📜 License

MIT License - Built for the "Mastering AI Engineering" Workshop.

---

*Built with ❤️ using Spring AI and React*
