# Alert System Setup

## Overview
The alert system receives POST requests from the recognition service when an unrecognized person is detected.

## Architecture
- **Alert Server** (port 5174): Receives POST requests from recognition system
- **React App** (port 5173): Polls alert server and displays notifications
- **Recognition System**: Sends POST to `http://localhost:5174/alert`

## Installation

```bash
npm install
```

This will install the required dependencies:
- express
- cors
- concurrently

## Running the Application

### Option 1: Run everything together (Recommended)
```bash
npm run dev:all
```
This runs both the Vite dev server and the alert server simultaneously.

### Option 2: Run separately
Terminal 1:
```bash
npm run dev
```

Terminal 2:
```bash
npm run alert-server
```

## How It Works

1. Recognition system detects unrecognized person
2. Recognition system sends: `POST http://localhost:5174/alert`
3. Alert server (Node.js) receives request and sets flag
4. React app polls alert server every 3 seconds
5. When flag is true, React shows red notification (max once per 30 seconds)
6. React calls clear endpoint to reset flag
7. Notification auto-dismisses after 10 seconds
8. New alerts are ignored during 30-second cooldown period

## Endpoints

### Alert Server (localhost:5174)

**POST /alert**
- Receives alerts from recognition system
- No body required
- Sets internal flag for 10 seconds

**GET /alert/check**
- Returns: `{ hasAlert: true/false }`
- Called by React app every 3 seconds

**POST /alert/clear**
- Clears the alert flag
- Called by React app after showing notification

## Testing

Send a test alert:
```bash
curl -X POST http://localhost:5174/alert
```

You should see:
1. Alert server console: "🚨 Alert received from recognition system!"
2. Red notification appears in dashboard within 3 seconds
3. Notification message: "Pessoa não reconhecida detectada pela câmera!"

## Recognition System Configuration

Configure your recognition system to send alerts to:
```python
url = "http://localhost:5174/alert"
# Method: POST
# Body: (none required)
```

The Node.js alert server on port 5174 receives the request, and the React app on port 5173 polls it for updates.

## Production Deployment

For production, consider:
1. Using environment variables for ports
2. Adding authentication to the alert endpoint
3. Using WebSockets instead of polling
4. Deploying alert server as a separate service

