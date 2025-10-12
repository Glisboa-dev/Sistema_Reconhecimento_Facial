import { useState, useEffect } from 'react';

const AlertNotification = ({ alert, onClose }) => {
  const [isVisible, setIsVisible] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      handleClose();
    }, 10000);

    return () => clearTimeout(timer);
  }, []);

  const handleClose = () => {
    setIsVisible(false);
    setTimeout(() => {
      onClose();
    }, 300);
  };

  if (!isVisible) return null;

  return (
    <div className={`fixed top-4 right-4 z-50 transition-all duration-300 ${
      isVisible ? 'opacity-100 translate-x-0' : 'opacity-0 translate-x-full'
    }`}>
      <div className="bg-red-600 text-white rounded-lg shadow-2xl p-4 max-w-sm border-2 border-red-700 animate-pulse">
        <div className="flex items-start gap-3">
          <div className="flex-shrink-0">
            <svg 
              className="w-6 h-6" 
              fill="none" 
              stroke="currentColor" 
              viewBox="0 0 24 24"
            >
              <path 
                strokeLinecap="round" 
                strokeLinejoin="round" 
                strokeWidth={2} 
                d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" 
              />
            </svg>
          </div>
          <div className="flex-1">
            <h3 className="font-bold text-lg mb-1">⚠️ Alerta de Segurança</h3>
            <p className="text-sm mb-2">
              {alert.message || 'Pessoa não reconhecida detectada pela câmera!'}
            </p>
            <p className="text-xs opacity-90">
              {new Date(alert.timestamp || Date.now()).toLocaleString('pt-BR')}
            </p>
          </div>
          <button
            onClick={handleClose}
            className="flex-shrink-0 text-white hover:text-gray-200 transition-colors"
          >
            <svg 
              className="w-5 h-5" 
              fill="none" 
              stroke="currentColor" 
              viewBox="0 0 24 24"
            >
              <path 
                strokeLinecap="round" 
                strokeLinejoin="round" 
                strokeWidth={2} 
                d="M6 18L18 6M6 6l12 12" 
              />
            </svg>
          </button>
        </div>
      </div>
    </div>
  );
};

export default AlertNotification;

