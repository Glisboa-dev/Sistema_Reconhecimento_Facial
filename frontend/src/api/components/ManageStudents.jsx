import { useState, useEffect } from 'react';
import { searchStudents } from '../studentApi';
import { addStudentRecord, updateRecordName, deleteRecordById, updateRecordPhoto, deactivateRecord, activateRecord } from '../recordApi';

const GRADES = [
  { value: 'PRIMEIRO_ANO', label: '1 ANO' },
  { value: 'SEGUNDO_ANO', label: '2 ANO' },
  { value: 'TERCEIRO_ANO', label: '3 ANO' },
  { value: 'QUARTO_ANO', label: '4 ANO' },
  { value: 'QUINTO_ANO', label: '5 ANO' },
  { value: 'SEXTO_ANO', label: '6 ANO' },
  { value: 'SETIMO_ANO', label: '7 ANO' },
  { value: 'OITAVO_ANO', label: '8 ANO' },
  { value: 'NONO_ANO', label: '9 ANO' },
  { value: 'PRIMEIRO_EM', label: '1 EM' },
  { value: 'SEGUNDO_EM', label: '2 EM' },
  { value: 'TERCEIRO_EM', label: '3 EM' }
];

const ManageStudents = () => {
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [modalType, setModalType] = useState('');
  const [searchFilters, setSearchFilters] = useState({});
  const [formData, setFormData] = useState({});
  const [selectedFile, setSelectedFile] = useState(null);
  const [notification, setNotification] = useState({ show: false, type: '', message: '' });

  useEffect(() => {
    loadStudents();
  }, []);

  const showNotification = (type, message) => {
    setNotification({ show: true, type, message });
    setTimeout(() => {
      setNotification({ show: false, type: '', message: '' });
    }, 4000); // Auto-dismiss after 4 seconds
  };

  const loadStudents = async (filters = {}) => {
    setLoading(true);
    setError('');
    try {
      const cleanFilters = {};
      if (filters.name) cleanFilters.name = filters.name;
      if (filters.studentId) cleanFilters.studentId = filters.studentId;
      if (filters.grade) cleanFilters.grade = filters.grade;
      if (filters.status) cleanFilters.status = filters.status;

      const response = await searchStudents(cleanFilters);
      const data = response.data;
      
      if (data && data.content && Array.isArray(data.content)) {
        setStudents(data.content);
      } else if (Array.isArray(data)) {
        setStudents(data);
      } else {
        setStudents([]);
      }
    } catch (err) {
      setError(err.message || 'Erro ao carregar alunos');
      setStudents([]);
    } finally {
      setLoading(false);
    }
  };

  const openModal = (type, student = null) => {
    setModalType(type);
    setShowModal(true);
    if (student) {
      setFormData({ id: student.id, name: student.name, grade: student.grade });
    } else {
      setFormData({});
    }
    setSelectedFile(null);
  };

  const closeModal = () => {
    setShowModal(false);
    setModalType('');
    setFormData({});
    setSelectedFile(null);
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      if (modalType === 'add') {
        if (!formData.name || !formData.studentId || !formData.grade || !selectedFile) {
          setError('Por favor, preencha todos os campos');
          setLoading(false);
          return;
        }
        await addStudentRecord(
          formData.name,
          { studentId: formData.studentId, grade: formData.grade },
          selectedFile
        );
        showNotification('success', '✓ Aluno adicionado com sucesso!');
      } else if (modalType === 'updateName') {
        if (!formData.newName) {
          setError('Por favor, informe o novo nome');
          setLoading(false);
          return;
        }
        await updateRecordName(formData.id, formData.newName);
        showNotification('success', '✓ Nome do aluno atualizado com sucesso!');
      } else if (modalType === 'updatePhoto') {
        if (!selectedFile) {
          setError('Por favor, selecione uma foto');
          setLoading(false);
          return;
        }
        await updateRecordPhoto(formData.id, selectedFile);
        showNotification('success', '✓ Foto do aluno atualizada com sucesso!');
      }
      closeModal();
      loadStudents(searchFilters);
    } catch (err) {
      const errorMsg = err.message || 'Erro ao processar operação';
      setError(errorMsg);
      showNotification('error', '✗ ' + errorMsg);
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
      showNotification('success', '✓ Registro deletado com sucesso!');
      loadStudents(searchFilters);
    } catch (err) {
      const errorMsg = err.message || 'Erro ao deletar registro';
      setError(errorMsg);
      showNotification('error', '✗ ' + errorMsg);
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
      showNotification('success', '✓ Registro desativado com sucesso!');
      loadStudents(searchFilters);
    } catch (err) {
      const errorMsg = err.message || 'Erro ao desativar registro';
      setError(errorMsg);
      showNotification('error', '✗ ' + errorMsg);
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
      showNotification('success', '✓ Registro ativado com sucesso!');
      loadStudents(searchFilters);
    } catch (err) {
      const errorMsg = err.message || 'Erro ao ativar registro';
      setError(errorMsg);
      showNotification('error', '✗ ' + errorMsg);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = () => {
    loadStudents(searchFilters);
  };

  const handleClearFilters = () => {
    setSearchFilters({});
    loadStudents({});
  };

  const getGradeLabel = (gradeValue) => {
    const grade = GRADES.find(g => g.value === gradeValue);
    return grade ? grade.label : gradeValue;
  };

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      {/* Notification Toast */}
      {notification.show && (
        <div className={`fixed top-4 right-4 z-50 px-6 py-4 rounded-lg shadow-lg transform transition-all duration-300 animate-slide-in ${
          notification.type === 'success' 
            ? 'bg-green-500 text-white' 
            : 'bg-red-500 text-white'
        }`}>
          <div className="flex items-center gap-3">
            <span className="text-lg font-semibold">{notification.message}</span>
            <button 
              onClick={() => setNotification({ show: false, type: '', message: '' })}
              className="text-white hover:text-gray-200 font-bold text-xl"
            >
              ×
            </button>
          </div>
        </div>
      )}

      <div className="max-w-7xl mx-auto">
        <div className="bg-white rounded-lg shadow-md p-6">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold text-gray-800">Gerenciar Alunos</h2>
            <button 
              onClick={() => openModal('add')} 
              className="bg-green-600 text-white px-6 py-2 rounded-lg hover:bg-green-700 transition-colors font-medium"
            >
              + Adicionar Aluno
            </button>
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
                placeholder="Matrícula"
                value={searchFilters.studentId || ''}
                onChange={(e) => setSearchFilters({...searchFilters, studentId: e.target.value})}
                className="border border-gray-300 px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <select
                value={searchFilters.grade || ''}
                onChange={(e) => setSearchFilters({...searchFilters, grade: e.target.value})}
                className="border border-gray-300 px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Todas as Séries</option>
                {GRADES.map(grade => (
                  <option key={grade.value} value={grade.value}>{grade.label}</option>
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
          ) : students.length === 0 ? (
            <div className="text-center py-12">
              <p className="text-gray-500 text-lg">Nenhum aluno encontrado</p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full border-collapse">
                <thead>
                  <tr className="bg-gray-100 border-b-2 border-gray-300">
                    <th className="px-6 py-3 text-left text-sm font-semibold text-gray-700">Matrícula</th>
                    <th className="px-6 py-3 text-left text-sm font-semibold text-gray-700">Nome</th>
                    <th className="px-6 py-3 text-left text-sm font-semibold text-gray-700">Série</th>
                    <th className="px-6 py-3 text-left text-sm font-semibold text-gray-700">Status</th>
                    <th className="px-6 py-3 text-left text-sm font-semibold text-gray-700">Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {students.map((student) => {
                    const studentIdValue = student.id;
                    
                    return (
                      <tr key={student.id} className="border-b border-gray-200 hover:bg-gray-50 transition-colors">
                        <td className="px-6 py-4 text-sm text-gray-800">{student.studentId}</td>
                        <td className="px-6 py-4 text-sm text-gray-800">{student.name}</td>
                        <td className="px-6 py-4 text-sm text-gray-800">{getGradeLabel(student.grade)}</td>
                        <td className="px-6 py-4 text-sm">
                          <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                            student.status === 'ATIVO' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                          }`}>
                            {student.status === 'ATIVO' ? 'Ativo' : 'Inativo'}
                          </span>
                        </td>
                        <td className="px-6 py-4 text-sm">
                          <div className="flex gap-2">
                            <button 
                              onClick={() => openModal('updateName', student)} 
                              className="text-blue-600 hover:text-blue-800 font-medium"
                            >
                              Editar
                            </button>
                            <button 
                              onClick={() => openModal('updatePhoto', student)} 
                              className="text-green-600 hover:text-green-800 font-medium"
                            >
                              Foto
                            </button>
                            {student.status === 'ATIVO' ? (
                              <button 
                                onClick={(e) => {
                                  e.preventDefault();
                                  handleDeactivate(studentIdValue);
                                }} 
                                className="text-yellow-600 hover:text-yellow-800 font-medium"
                              >
                                Desativar
                              </button>
                            ) : (
                              <button 
                                onClick={(e) => {
                                  e.preventDefault();
                                  handleActivate(studentIdValue);
                                }} 
                                className="text-green-600 hover:text-green-800 font-medium"
                              >
                                Ativar
                              </button>
                            )}
                            <button 
                              onClick={(e) => {
                                e.preventDefault();
                                handleDelete(studentIdValue);
                              }} 
                              className="text-red-600 hover:text-red-800 font-medium"
                            >
                              Deletar
                            </button>
                          </div>
                        </td>
                      </tr>
                    );
                  })}
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
                {modalType === 'add' && 'Adicionar Novo Aluno'}
                {modalType === 'updateName' && 'Atualizar Nome do Aluno'}
                {modalType === 'updatePhoto' && 'Atualizar Foto do Aluno'}
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
                        <label className="block text-sm font-medium text-gray-700 mb-2">Matrícula</label>
                        <input
                          type="text"
                          placeholder="Digite a matrícula"
                          value={formData.studentId || ''}
                          onChange={(e) => {
                            const value = e.target.value;
                            if (value.length <= 10) {
                              setFormData({...formData, studentId: value});
                            }
                          }}
                          className="w-full border border-gray-300 px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                          maxLength={10}
                          required
                        />
                        <p className="text-xs text-gray-500 mt-1">
                          {(formData.studentId || '').length}/10 caracteres
                        </p>
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">Série</label>
                        <select
                          value={formData.grade || ''}
                          onChange={(e) => setFormData({...formData, grade: e.target.value})}
                          className="w-full border border-gray-300 px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                          required
                        >
                          <option value="">Selecione a série</option>
                          {GRADES.map(grade => (
                            <option key={grade.value} value={grade.value}>{grade.label}</option>
                          ))}
                        </select>
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">Foto</label>
                        <div className="relative">
                          <input
                            type="file"
                            accept="image/*"
                            onChange={(e) => setSelectedFile(e.target.files[0])}
                            className="hidden"
                            id="file-upload-add"
                            required
                          />
                          <label
                            htmlFor="file-upload-add"
                            className="flex items-center justify-center w-full border-2 border-dashed border-gray-300 px-4 py-3 rounded-lg cursor-pointer hover:border-blue-500 hover:bg-blue-50 transition-colors"
                          >
                            <div className="text-center">
                              <svg className="mx-auto h-12 w-12 text-gray-400" stroke="currentColor" fill="none" viewBox="0 0 48 48" aria-hidden="true">
                                <path d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                              </svg>
                              <p className="mt-2 text-sm text-gray-600">
                                {selectedFile ? selectedFile.name : 'Clique para selecionar uma foto'}
                              </p>
                              <p className="text-xs text-gray-500 mt-1">PNG, JPG, JPEG até 10MB</p>
                            </div>
                          </label>
                        </div>
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
                      <div className="relative">
                        <input
                          type="file"
                          accept="image/*"
                          onChange={(e) => setSelectedFile(e.target.files[0])}
                          className="hidden"
                          id="file-upload-update"
                          required
                        />
                        <label
                          htmlFor="file-upload-update"
                          className="flex items-center justify-center w-full border-2 border-dashed border-gray-300 px-4 py-3 rounded-lg cursor-pointer hover:border-blue-500 hover:bg-blue-50 transition-colors"
                        >
                          <div className="text-center">
                            <svg className="mx-auto h-12 w-12 text-gray-400" stroke="currentColor" fill="none" viewBox="0 0 48 48" aria-hidden="true">
                              <path d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                            </svg>
                            <p className="mt-2 text-sm text-gray-600">
                              {selectedFile ? selectedFile.name : 'Clique para selecionar uma nova foto'}
                            </p>
                            <p className="text-xs text-gray-500 mt-1">PNG, JPG, JPEG até 10MB</p>
                          </div>
                        </label>
                      </div>
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

export default ManageStudents;