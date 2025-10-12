import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import FormLogin from "./api/components/FormLogin";
import './index.css';
import Dashboard from "./api/components/Dashboard";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<FormLogin />} />
        <Route path="/dashboard" element={<Dashboard/>}/>
      </Routes>
    </Router>
  );
}

export default App;
