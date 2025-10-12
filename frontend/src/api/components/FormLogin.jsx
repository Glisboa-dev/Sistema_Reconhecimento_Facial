import { useState } from 'react';
import { loginRequest } from '../authApi.js';

const FormLogin = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errors, setErrors] = useState({ username: '', password: '' });
  const [isLoading, setIsLoading] = useState(false);
  const [loginError, setLoginError] = useState('');

  const validateForm = () => {
    let isValid = true;
    const newErrors = { username: '', password: '' };

    if (username.trim() === '') {
      newErrors.username = 'O campo de usuário é obrigatório.';
      isValid = false;
    }

    if (password === '') {
      newErrors.password = 'O campo de senha é obrigatório.';
      isValid = false;
    }

    setErrors(newErrors);
    return isValid;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoginError('');

    if (!validateForm()) {
      return;
    }

    setIsLoading(true);

    try {
      const { token, role } = await loginRequest(username, password);
      
      alert('Login realizado com sucesso!');
      
      window.location.href = '/dashboard';
    } catch (error) {
      setLoginError(
        error.response?.data?.message || 
        'Erro ao fazer login. Verifique suas credenciais.'
      );
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="flex justify-center items-center min-h-screen bg-gray-100">
      <div className="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
        <h2 className="text-2xl font-bold text-center mb-6 text-gray-800">
          Login
        </h2>

        <div className="space-y-4">
          <div>
            <label 
              htmlFor="username" 
              className="block mb-2 text-sm font-medium text-gray-700"
            >
              Usuário
            </label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Digite seu usuário"
              className="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition"
              onKeyDown={(e) => e.key === 'Enter' && handleSubmit(e)}
            />
            {errors.username && (
              <p className="text-red-500 text-xs mt-1">{errors.username}</p>
            )}
          </div>

          <div>
            <label 
              htmlFor="password" 
              className="block mb-2 text-sm font-medium text-gray-700"
            >
              Senha
            </label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Digite sua senha"
              className="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition"
              onKeyDown={(e) => e.key === 'Enter' && handleSubmit(e)}
            />
            {errors.password && (
              <p className="text-red-500 text-xs mt-1">{errors.password}</p>
            )}
          </div>

          {loginError && (
            <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md text-sm">
              {loginError}
            </div>
          )}

          <button
            type="button"
            onClick={handleSubmit}
            disabled={isLoading}
            className="w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 focus:ring-4 focus:ring-blue-300 transition disabled:bg-blue-400 disabled:cursor-not-allowed font-medium"
          >
            {isLoading ? 'Entrando...' : 'Entrar'}
          </button>
        </div>
      </div>
    </div>
  );
};

export default FormLogin;