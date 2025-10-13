import { useState, useEffect } from 'react';
import ManageStudents from './ManageStudents';
import ManageEmployees from './ManageEmployees';
import Presences from './Presences';
import AlertNotification from '../../components/AlertNotification';
import { useAlertMonitor } from '../../hooks/useAlertMonitor';

const Dashboard = () => {
  const [userRole, setUserRole] = useState('');
  const [activeTab, setActiveTab] = useState('students');
  const { alerts, removeAlert } = useAlertMonitor(3000, 30000);

  useEffect(() => {
    const role = localStorage.getItem('userRole');
    setUserRole(role || '');
  }, []);

  const handleLogout = () => {
    localStorage.clear();
    window.location.href = '/login';
  };

  const getRoleLabel = (role) => {
    const roles = {
      'ADMIN': 'Administrador',
      'DBA': 'DBA',
      'FUNCIONARIO': 'Funcionário'
    };
    return roles[role] || role;
  };

  return (
    <div className="min-h-screen bg-gray-100">
      {alerts.map((alert, index) => (
        <AlertNotification
          key={alert.id || index}
          alert={alert}
          onClose={() => removeAlert(alert.id || index)}
        />
      ))}
      
      <nav className="bg-gradient-to-r from-blue-600 to-blue-700 text-white shadow-lg">
        <div className="max-w-7xl mx-auto px-6 py-4">
          <div className="flex justify-between items-center">
            <div>
              <h1 className="text-2xl font-bold">Sistema de Reconhecimento Facial</h1>
              <p className="text-blue-100 text-sm mt-1">Gerenciamento de Alunos e Funcionários</p>
            </div>
            <div className="flex items-center gap-4">
              <div className="text-right">
                <p className="text-sm text-blue-100">Função:</p>
                <p className="font-semibold">{getRoleLabel(userRole)}</p>
              </div>
              <button 
                onClick={handleLogout}
                className="bg-blue-800 hover:bg-blue-900 px-6 py-2 rounded-lg transition-colors font-medium shadow"
              >
                Sair
              </button>
            </div>
          </div>
        </div>
      </nav>

      <div className="max-w-7xl mx-auto px-6 py-6">
        <div className="bg-white rounded-lg shadow-md mb-6">
          <div className="flex border-b border-gray-200">
            <button
              onClick={() => setActiveTab('students')}
              className={`flex-1 px-6 py-4 text-center font-medium transition-colors ${
                activeTab === 'students'
                  ? 'bg-blue-600 text-white border-b-2 border-blue-600'
                  : 'text-gray-600 hover:bg-gray-50'
              }`}
            >
              Alunos
            </button>
            <button
              onClick={() => setActiveTab('employees')}
              className={`flex-1 px-6 py-4 text-center font-medium transition-colors ${
                activeTab === 'employees'
                  ? 'bg-blue-600 text-white border-b-2 border-blue-600'
                  : 'text-gray-600 hover:bg-gray-50'
              }`}
            >
              Funcionários
            </button>
            <button
              onClick={() => setActiveTab('presences')}
              className={`flex-1 px-6 py-4 text-center font-medium transition-colors ${
                activeTab === 'presences'
                  ? 'bg-blue-600 text-white border-b-2 border-blue-600'
                  : 'text-gray-600 hover:bg-gray-50'
              }`}
            >
              Presenças
            </button>
          </div>
        </div>

        <div>
          {activeTab === 'students' && <ManageStudents />}
          {activeTab === 'employees' && <ManageEmployees userRole={userRole} />}
          {activeTab === 'presences' && <Presences />}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
