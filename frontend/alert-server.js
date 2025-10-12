import express from 'express';
import cors from 'cors';

const app = express();
const PORT = 5174;

app.use(cors());
app.use(express.json());

let hasAlert = false;

app.post('/alert', (req, res) => {
  console.log('🚨 Alert received from recognition system!');
  hasAlert = true;
  
  setTimeout(() => {
    hasAlert = false;
  }, 10000);
  
  res.status(200).json({ received: true });
});

app.get('/alert/check', (req, res) => {
  res.json({ hasAlert });
});

app.post('/alert/clear', (req, res) => {
  hasAlert = false;
  res.json({ cleared: true });
});

app.listen(PORT, () => {
  console.log(`🚀 Alert server running on http://localhost:${PORT}`);
  console.log(`📡 Ready to receive alerts at http://localhost:${PORT}/alert`);
});

