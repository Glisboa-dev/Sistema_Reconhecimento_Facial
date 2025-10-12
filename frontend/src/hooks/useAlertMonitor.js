import { useState, useEffect, useCallback, useRef } from 'react';
import { checkForAlert, clearAlert } from '../api/alertApi';

export const useAlertMonitor = (pollingInterval = 3000, cooldownPeriod = 30000) => {
  const [alerts, setAlerts] = useState([]);
  const [isMonitoring, setIsMonitoring] = useState(true);
  const lastAlertTime = useRef(0);

  const checkForAlerts = useCallback(async () => {
    if (!isMonitoring) return;
    
    try {
      const response = await checkForAlert();
      
      const hasAlert = response?.success && (
        response?.data === true || 
        response?.data === 'true' || 
        response?.data?.hasAlert === true
      );
      
      if (hasAlert) {
        const now = Date.now();
        const timeSinceLastAlert = now - lastAlertTime.current;
        
        if (timeSinceLastAlert >= cooldownPeriod) {
          const newAlert = {
            id: now,
            message: 'Pessoa não reconhecida detectada pela câmera!',
            timestamp: new Date().toISOString()
          };
          
          setAlerts(prev => [...prev, newAlert]);
          lastAlertTime.current = now;
        }
        
        await clearAlert().catch(() => {});
      }
    } catch (error) {
      
    }
  }, [isMonitoring, cooldownPeriod]);

  useEffect(() => {
    if (!isMonitoring) return;

    checkForAlerts();
    
    const interval = setInterval(() => {
      checkForAlerts();
    }, pollingInterval);

    return () => clearInterval(interval);
  }, [checkForAlerts, pollingInterval, isMonitoring]);

  const removeAlert = (alertId) => {
    setAlerts(prev => prev.filter(alert => alert.id !== alertId));
  };

  const pauseMonitoring = () => setIsMonitoring(false);
  const resumeMonitoring = () => setIsMonitoring(true);

  return {
    alerts,
    removeAlert,
    pauseMonitoring,
    resumeMonitoring,
    isMonitoring
  };
};

