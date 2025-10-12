import { useState, useEffect } from 'react';
import { searchEmployees } from '../employeeApi';
import { addEmployeeRecord, updateRecordName, deleteRecordById, updateRecordPhoto, deactivateRecord, activateRecord } from '../recordApi';
import { registerEmployee } from '../authApi';
import { formatCPF, removeCPFFormatting, validateCPF, handleCPFInput } from '../../utils/cpfUtils';

const POSTS = [
  { value: 'PROFESSOR', label: 'Professor' },
  { value: 'COORDENADOR', label: 'Coordenador' },
  { value: 'DIRETOR', label: 'Diretor' },
  { value: 'SECRETARIO', label: 'Secretário' },
  { value: 'AUXILIAR', label: 'Auxiliar' },
  { value: 'MONITOR', label: 'Monitor' }
];

const ManageEmployees = () => {
  const [employees, setEmployees] = useState([]);
  const [allEmployees, setAllEmployees] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [modalType, setModalType] = useState('');
  const [searchFilters, setSearchFilters] = useState({});
  const [formData, setFormData] = useState({});
  const [selectedFile, setSelectedFile] = useState(null);
  const [userRole, setUserRole] = useState('');
  const [cpfError, setCpfError] = useState('');

  useEffect(() => {
    const role = localStorage.getItem('userRole');
    setUserRole(role || '');
    loadEmployees();
  }, []);

  const loadEmployees = async (filters = {}) => {
    setLoading(true);
    setError('');
    try {
      const cleanFilters = {};
      if (filters.name) cleanFilters.name = filters.name;
      if (filters.cpf) cleanFilters.cpf = filters.cpf;
      if (filters.post) cleanFilters.post = filters.post;
      if (filters.status) cleanFilters.status = filters.status;

      const response = await searchEmployees(cleanFilters);
      const data = response.data;
      
      if (data && data.content && Array.isArray(data.content)) {
        setEmployees(data.content);
      } else if (Array.isArray(data)) {
        setEmployees(data);
      } else {
        setEmployees([]);
      }
    } catch (err) {
      setError(err.message || 'Erro ao carregar funcionários');
      setEmployees([]);
    } finally {
      setLoading(false);
    }
  };

  const openModal = async (type, employee = null) => {
    setModalType(type);
    setShowModal(true);
    if (employee) {
      setFormData({ id: employee.id, name: employee.name });
    } else {
      setFormData({});
    }
    setSelectedFile(null);
    
    if (type === 'register') {
      try {
        const response = await searchEmployees({ status: 'ATIVO' });
        const data = response.data;
        let employeesList = [];
        
        if (data && data.content && Array.isArray(data.content)) {
          employeesList = data.content;
        } else if (Array.isArray(data)) {
          employeesList = data;
        }
        
        console.log('Loaded employees for registration:', employeesList);
        setAllEmployees(employeesList);
      } catch (err) {
        console.error('Error loading employees:', err);
        setAllEmployees([]);
      }
    }
  };

  const closeModal = () => {
    setShowModal(false);
    setModalType('');
    setFormData({});
    setSelectedFile(null);
    setError('');
    setCpfError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setCpfError('');

    try {
      if (modalType === 'add') {
        if (!formData.name || !formData.cpf || !formData.post || !selectedFile) {
          setError('Por favor, preencha todos os campos');
          setLoading(false);
          return;
        }
        
        const cpfValidation = validateCPF(formData.cpf);
        if (!cpfValidation.valid) {
          setCpfError(cpfValidation.message);
          setLoading(false);
          return;
        }
        
        const cleanCPF = removeCPFFormatting(formData.cpf);
        await addEmployeeRecord(
          formData.name,
          { cpf: cleanCPF, post: formData.post, description: formData.description || '' },
          selectedFile
        );
      } else if (modalType === 'register') {
        if (!formData.username || !formData.password || !formData.recordId) {
          setError('Por favor, preencha todos os campos');
          setLoading(false);
          return;
        }
        const recordId = parseInt(formData.recordId, 10);
        if (isNaN(recordId)) {
          setError('ID do registro inválido');
          setLoading(false);
          return;
        }
        console.log('Registering employee with ID:', recordId);
        await registerEmployee(formData.username, formData.password, recordId);
      } else if (modalType === 'updateName') {
        if (!formData.newName) {
          setError('Por favor, informe o novo nome');
          setLoading(false);
          return;
        }
        await updateRecordName(formData.id, formData.newName);
      } else if (modalType === 'updatePhoto') {
        if (!selectedFile) {
          setError('Por favor, selecione uma foto');
          setLoading(false);
          return;
        }
        await updateRecordPhoto(formData.id, selectedFile);
      }
      closeModal();
      loadEmployees(searchFilters);
    } catch (err) {
      setError(err.message || 'Erro ao processar operação');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Tem certeza que deseja deletar este registro?')) return;
    
    setLoading(true);
    setError('');
    try {
      await deleteRecordById(id);
      loadEmployees(searchFilters);
    } catch (err) {
      setError(err.message || 'Erro ao deletar registro');
    } finally {
      setLoading(false);
    }
  };

  const handleDeactivate = async (id) => {
    if (!window.confirm('Tem certeza que deseja desativar este registro?')) return;
    
    setLoading(true);
    setError('');
    try {
      await deactivateRecord(id);
      loadEmployees(searchFilters);
    } catch (err) {
      setError(err.message || 'Erro ao desativar registro');
    } finally {
      setLoading(false);
    }
  };

  const handleActivate = async (id) => {
    if (!window.confirm('Tem certeza que deseja ativar este registro?')) return;
    
    setLoading(true);
    setError('');
    try {
      await activateRecord(id);
      loadEmployees(searchFilters);
    } catch (err) {
      setError(err.message || 'Erro ao ativar registro');
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = () => {
    loadEmployees(searchFilters);
  };

  const handleClearFilters = () => {
    setSearchFilters({});
    loadEmployees({});
  };

  const getPostLabel = (postValue) => {
    const post = POSTS.find(p => p.value === postValue);
    return post ? post.label : postValue;
  };

  const canManageEmployees = userRole === 'ADMIN' || userRole === 'DBA';

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="max-w-7xl mx-auto">
        <div className="bg-white rounded-lg shadow-md p-6">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold text-gray-800">Gerenciar Funcionários</h2>
            {canManageEmployees && (
              <div className="flex gap-2">
                <button 
                  onClick={() => openModal('add')} 
                  className="bg-green-600 text-white px-6 py-2 rounded-lg hover:bg-green-700 transition-colors font-medium"
                >
                  + Adicionar Funcionário
                </button>
                <button 
                  onClick={() => openModal('register')} 
                  className="bg-purple-600 text-white px-6 py-2 rounded-lg hover:bg-purple-700 transition-colors font-medium"
                >
                  Registrar Usuário
                </button>
              </div>
            )}
          </div>

          <div className="bg-gray-50 p-4 rounded-lg mb-6">
            <div className="grid grid-cols-1 md:grid-cols-5 gap-3 mb-3">
              <input
                type="text"
                placeholder="Nome"
                value={searchFilters.name || ''}
                onChange={(e) => setSearchFilters({...searchFilters, name: e.target.value})}
                className="border border-gray-300 px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <input
                type="text"
                placeholder="CPF (apenas números)"
                value={searchFilters.cpf || ''}
                onChange={(e) => setSearchFilters({...searchFilters, cpf: removeCPFFormatting(e.target.value)})}
                className="border border-gray-300 px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <select
                value={searchFilters.post || ''}
                onChange={(e) => setSearchFilters({...searchFilters, post: e.target.value})}
                className="border border-gray-300 px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Todos os Cargos</option>
                {POSTS.map(post => (
                  <option key={post.value} value={post.value}>{post.label}</option>
                ))}
              </select>
              <select
                value={searchFilters.status || ''}
                onChange={(e) => setSearchFilters({...searchFilters, status: e.target.value})}
                className="border border-gray-300 px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Todos os Status</option>
                <option value="ATIVO">Ativo</option>
                <option value="INATIVO">Inativo</option>
              </select>
              <div className="flex gap-2">
                <button 
                  onClick={handleSearch} 
                  className="flex-1 bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors font-medium"
                >
                  Buscar
                </button>
                <button 
                  onClick={handleClearFilters} 
                  className="bg-gray-400 text-white px-4 py-2 rounded-lg hover:bg-gray-500 transition-colors"
                >
                  Limpar
                </button>
              </div>
            </div>
          </div>

          {error && (
            <div className="bg-red-50 border border-red-300 text-red-700 px-4 py-3 rounded-lg mb-4 flex justify-between items-center">
              <span>{error}</span>
              <button onClick={() => setError('')} className="text-red-700 hover:text-red-900 font-bold">×</button>
            </div>
          )}

          {loading && !showModal ? (
            <div className="text-center py-12">
              <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
              <p className="mt-4 text-gray-600">Carregando...</p>
            </div>
          ) : employees.length === 0 ? (
            <div className="text-center py-12">
              <p className="text-gray-500 text-lg">Nenhum funcionário encontrado</p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full border-collapse">
                <thead>
                  <tr className="bg-gray-100 border-b-2 border-gray-300">
                    <th className="px-6 py-3 text-left text-sm font-semibold text-gray-700">CPF</th>
                    <th className="px-6 py-3 text-left text-sm font-semibold text-gray-700">Nome</th>
                    <th className="px-6 py-3 text-left text-sm font-semibold text-gray-700">Cargo</th>
                    <th className="px-6 py-3 text-left text-sm font-semibold text-gray-700">Descrição</th>
                    <th className="px-6 py-3 text-left text-sm font-semibold text-gray-700">Status</th>
                    <th className="px-6 py-3 text-left text-sm font-semibold text-gray-700">Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {employees.map((employee) => (
                    <tr key={employee.id} className="border-b border-gray-200 hover:bg-gray-50 transition-colors">
                      <td className="px-6 py-4 text-sm text-gray-800">{formatCPF(employee.cpf)}</td>
                      <td className="px-6 py-4 text-sm text-gray-800">{employee.name}</td>
                      <td className="px-6 py-4 text-sm text-gray-800">{getPostLabel(employee.post)}</td>
                      <td className="px-6 py-4 text-sm text-gray-800">{employee.description || '-'}</td>
                      <td className="px-6 py-4 text-sm">
                        <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                          employee.status === 'ATIVO' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                        }`}>
                          {employee.status === 'ATIVO' ? 'Ativo' : 'Inativo'}
                        </span>
                      </td>
                      <td className="px-6 py-4 text-sm">
                        <div className="flex gap-2">
                          <button 
                            onClick={() => openModal('updateName', employee)} 
                            className="text-blue-600 hover:text-blue-800 font-medium"
                          >
                            Editar
                          </button>
                          <button 
                            onClick={() => openModal('updatePhoto', employee)} 
                            className="text-green-600 hover:text-green-800 font-medium"
                          >
                            Foto
                          </button>
                          {canManageEmployees && (
                            <>
                              {employee.status === 'ATIVO' ? (
                                <button 
                                  onClick={() => handleDeactivate(employee.id)} 
                                  className="text-yellow-600 hover:text-yellow-800 font-medium"
                                >
                                  Desativar
                                </button>
                              ) : (
                                <button 
                                  onClick={() => handleActivate(employee.id)} 
                                  className="text-green-600 hover:text-green-800 font-medium"
                                >
                                  Ativar
                                </button>
                              )}
                              <button 
                                onClick={() => handleDelete(employee.id)} 
                                className="text-red-600 hover:text-red-800 font-medium"
                              >
                                Deletar
                              </button>
                            </>
                          )}
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-lg shadow-xl w-full max-w-md max-h-[90vh] overflow-y-auto">
            <div className="p-6">
              <h3 className="text-2xl font-bold mb-6 text-gray-800">
                {modalType === 'add' && 'Adicionar Novo Funcionário'}
                {modalType === 'register' && 'Registrar Novo Usuário'}
                {modalType === 'updateName' && 'Atualizar Nome do Funcionário'}
                {modalType === 'updatePhoto' && 'Atualizar Foto do Funcionário'}
              </h3>

              <form onSubmit={handleSubmit}>
                <div className="space-y-4">
                  {modalType === 'add' && (
                    <>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">Nome Completo</label>
                        <input
                          type="text"
                          placeholder="Digite o nome completo"
                          value={formData.name || ''}
                          onChange={(e) => setFormData({...formData, name: e.target.value})}
                          className="w-full border border-gray-300 px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                          required
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">CPF</label>
                        <input
                          type="text"
                          placeholder="000.000.000-00"
                          value={formData.cpf || ''}
                          onChange={(e) => {
                            const formatted = handleCPFInput(e.target.value);
                            setFormData({...formData, cpf: formatted});
                            setCpfError('');
                          }}
                          className={`w-full border px-4 py-2 rounded-lg focus:outline-none focus:ring-2 ${
                            cpfError ? 'border-red-500 focus:ring-red-500' : 'border-gray-300 focus:ring-blue-500'
                          }`}
                          maxLength="14"
                          required
                        />
                        {cpfError && (
                          <p className="text-red-500 text-xs mt-1">{cpfError}</p>
                        )}
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">Cargo</label>
                        <select
                          value={formData.post || ''}
                          onChange={(e) => setFormData({...formData, post: e.target.value})}
                          className="w-full border border-gray-300 px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                          required
                        >
                          <option value="">Selecione o cargo</option>
                          {POSTS.map(post => (
                            <option key={post.value} value={post.value}>{post.label}</option>
                          ))}
                        </select>
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">Descrição</label>
                        <textarea
                          placeholder="Digite a descrição (opcional)"
                          value={formData.description || ''}
                          onChange={(e) => setFormData({...formData, description: e.target.value})}
                          className="w-full border border-gray-300 px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                          rows="3"
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">Foto</label>
                        <input
                          type="file"
                          accept="image/*"
                          onChange={(e) => setSelectedFile(e.target.files[0])}
                          className="w-full border border-gray-300 px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                          required
                        />
                      </div>
                    </>
                  )}

                  {modalType === 'register' && (
                    <>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">Selecionar Funcionário</label>
                        <select
                          value={formData.recordId || ''}
                          onChange={(e) => {
                            const selectedId = e.target.value;
                            const selectedEmployee = allEmployees.find(emp => emp.id === parseInt(selectedId, 10));
                            console.log('Selected employee ID:', selectedId);
                            console.log('Selected employee data:', selectedEmployee);
                            setFormData({...formData, recordId: selectedId ? parseInt(selectedId, 10) : ''});
                          }}
                          className="w-full border border-gray-300 px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                          required
                        >
                          <option value="">Selecione um funcionário</option>
                          {allEmployees.map((emp) => (
                            <option key={emp.id} value={emp.id}>
                              ID: {emp.id} - {emp.name} - CPF: {formatCPF(emp.cpf)}
                            </option>
                          ))}
                        </select>
                        <p className="text-xs text-gray-500 mt-1">
                          Selecione o funcionário para associar ao usuário
                        </p>
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">Nome de Usuário</label>
                        <input
                          type="text"
                          placeholder="Digite o nome de usuário"
                          value={formData.username || ''}
                          onChange={(e) => setFormData({...formData, username: e.target.value})}
                          className="w-full border border-gray-300 px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                          required
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">Senha</label>
                        <input
                          type="password"
                          placeholder="Digite a senha"
                          value={formData.password || ''}
                          onChange={(e) => setFormData({...formData, password: e.target.value})}
                          className="w-full border border-gray-300 px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                          required
                        />
                      </div>
                    </>
                  )}

                  {modalType === 'updateName' && (
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">Novo Nome</label>
                      <input
                        type="text"
                        placeholder="Digite o novo nome"
                        defaultValue={formData.name}
                        onChange={(e) => setFormData({...formData, newName: e.target.value})}
                        className="w-full border border-gray-300 px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                      />
                    </div>
                  )}

                  {modalType === 'updatePhoto' && (
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">Nova Foto</label>
                      <input
                        type="file"
                        accept="image/*"
                        onChange={(e) => setSelectedFile(e.target.files[0])}
                        className="w-full border border-gray-300 px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                      />
                    </div>
                  )}

                  {error && (
                    <div className="bg-red-50 border border-red-300 text-red-700 px-4 py-2 rounded-lg text-sm">
                      {error}
                    </div>
                  )}

                  <div className="flex gap-3 pt-4">
                    <button
                      type="submit"
                      disabled={loading}
                      className="flex-1 bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 disabled:bg-blue-400 disabled:cursor-not-allowed transition-colors font-medium"
                    >
                      {loading ? 'Processando...' : 'Confirmar'}
                    </button>
                    <button
                      type="button"
                      onClick={closeModal}
                      disabled={loading}
                      className="flex-1 bg-gray-300 text-gray-700 px-6 py-3 rounded-lg hover:bg-gray-400 disabled:bg-gray-200 disabled:cursor-not-allowed transition-colors font-medium"
                    >
                      Cancelar
                    </button>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ManageEmployees;

