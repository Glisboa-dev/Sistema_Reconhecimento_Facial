import { useState, useEffect } from 'react';
import { searchPresences } from '../presenceApi';

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

const POSTS = [
  { value: 'PROFESSOR', label: 'Professor' },
  { value: 'COORDENADOR', label: 'Coordenador' },
  { value: 'DIRETOR', label: 'Diretor' },
  { value: 'SECRETARIO', label: 'Secretário' },
  { value: 'AUXILIAR', label: 'Auxiliar' },
  { value: 'MONITOR', label: 'Monitor' }
];

const Presences = () => {
  const [presences, setPresences] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [searchFilters, setSearchFilters] = useState({
    searchStudents: true
  });

  useEffect(() => {
    loadPresences();
  }, []);

  const loadPresences = async (filters = {}) => {
    setLoading(true);
    setError('');
    try {
      const cleanFilters = {};
      if (filters.from) cleanFilters.from = filters.from;
      if (filters.to) cleanFilters.to = filters.to;
      if (filters.name) cleanFilters.name = filters.name;
      if (filters.grade) cleanFilters.grade = filters.grade;
      if (filters.post) cleanFilters.post = filters.post;
      cleanFilters.searchStudents = filters.searchStudents !== undefined ? filters.searchStudents : true;

      console.log('=== SEARCH PRESENCES REQUEST ===');
      console.log('Filters sent:', cleanFilters);

      const response = await searchPresences(cleanFilters);
      
      console.log('=== SEARCH PRESENCES RESPONSE ===');
      console.log('Full response:', response);
      console.log('Response data:', response.data);
      
      const data = response.data;
      
      if (data && data.content && Array.isArray(data.content)) {
        console.log('Using data.content:', data.content);
        setPresences(data.content);
      } else if (Array.isArray(data)) {
        console.log('Using data directly:', data);
        setPresences(data);
      } else {
        console.log('No valid data structure found');
        setPresences([]);
      }
    } catch (err) {
      console.error('=== SEARCH PRESENCES ERROR ===');
      console.error('Error:', err);
      setError(err.message || 'Erro ao carregar presenças');
      setPresences([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = () => {
    loadPresences(searchFilters);
  };

  const handleClearFilters = () => {
    setSearchFilters({ searchStudents: true });
    loadPresences({ searchStudents: true });
  };

  const formatDateTime = (timestamp) => {
    if (!timestamp) return '-';
    const date = new Date(timestamp);
    return date.toLocaleString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    });
  };

  const getGradeLabel = (gradeValue) => {
    const grade = GRADES.find(g => g.value === gradeValue);
    return grade ? grade.label : gradeValue;
  };

  const getPostLabel = (postValue) => {
    const post = POSTS.find(p => p.value === postValue);
    return post ? post.label : postValue;
  };

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="max-w-7xl mx-auto">
        <div className="bg-white rounded-lg shadow-md p-6">
          <div className="mb-6">
            <h2 className="text-2xl font-bold text-gray-800">Consultar Presenças</h2>
            <p className="text-gray-600 mt-2">Visualize o histórico de presenças registradas</p>
          </div>

          <div className="bg-gray-50 p-4 rounded-lg mb-6">
            <div className="grid grid-cols-1 md:grid-cols-5 gap-3 mb-3">
              <div>
                <label className="block text-xs font-medium text-gray-600 mb-1">De</label>
                <input
                  type="date"
                  value={searchFilters.from || ''}
                  onChange={(e) => setSearchFilters({...searchFilters, from: e.target.value})}
                  className="w-full border border-gray-300 px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  placeholder="Data Início"
                />
              </div>
              <div>
                <label className="block text-xs font-medium text-gray-600 mb-1">Até</label>
                <input
                  type="date"
                  value={searchFilters.to || ''}
                  onChange={(e) => setSearchFilters({...searchFilters, to: e.target.value})}
                  className="w-full border border-gray-300 px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  placeholder="Data Fim"
                />
              </div>
              <div>
                <label className="block text-xs font-medium text-gray-600 mb-1">Nome</label>
                <input
                  type="text"
                  placeholder="Digite o nome"
                  value={searchFilters.name || ''}
                  onChange={(e) => setSearchFilters({...searchFilters, name: e.target.value})}
                  className="w-full border border-gray-300 px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              {searchFilters.searchStudents ? (
                <div>
                  <label className="block text-xs font-medium text-gray-600 mb-1">Série</label>
                  <select
                    value={searchFilters.grade || ''}
                    onChange={(e) => setSearchFilters({...searchFilters, grade: e.target.value})}
                    className="w-full border border-gray-300 px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">Todas as Séries</option>
                    {GRADES.map(grade => (
                      <option key={grade.value} value={grade.value}>{grade.label}</option>
                    ))}
                  </select>
                </div>
              ) : (
                <div>
                  <label className="block text-xs font-medium text-gray-600 mb-1">Cargo</label>
                  <select
                    value={searchFilters.post || ''}
                    onChange={(e) => setSearchFilters({...searchFilters, post: e.target.value})}
                    className="w-full border border-gray-300 px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">Todos os Cargos</option>
                    {POSTS.map(post => (
                      <option key={post.value} value={post.value}>{post.label}</option>
                    ))}
                  </select>
                </div>
              )}
              <div>
                <label className="block text-xs font-medium text-gray-600 mb-1">&nbsp;</label>
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
            <div className="flex items-center gap-4 mt-3">
              <label className="flex items-center gap-2 cursor-pointer">
                <input
                  type="radio"
                  name="searchType"
                  checked={searchFilters.searchStudents === true}
                  onChange={() => setSearchFilters({...searchFilters, searchStudents: true})}
                  className="w-4 h-4 text-blue-600"
                />
                <span className="text-sm text-gray-700">Buscar Alunos</span>
              </label>
              <label className="flex items-center gap-2 cursor-pointer">
                <input
                  type="radio"
                  name="searchType"
                  checked={searchFilters.searchStudents === false}
                  onChange={() => setSearchFilters({...searchFilters, searchStudents: false})}
                  className="w-4 h-4 text-blue-600"
                />
                <span className="text-sm text-gray-700">Buscar Funcionários</span>
              </label>
            </div>
          </div>

          {error && (
            <div className="bg-red-50 border border-red-300 text-red-700 px-4 py-3 rounded-lg mb-4 flex justify-between items-center">
              <span>{error}</span>
              <button onClick={() => setError('')} className="text-red-700 hover:text-red-900 font-bold">×</button>
            </div>
          )}

          {loading ? (
            <div className="text-center py-12">
              <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
              <p className="mt-4 text-gray-600">Carregando...</p>
            </div>
          ) : presences.length === 0 ? (
            <div className="text-center py-12">
              <p className="text-gray-500 text-lg">Nenhuma presença encontrada</p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full border-collapse">
                <thead>
                  <tr className="bg-gray-100 border-b-2 border-gray-300">
                    <th className="px-6 py-3 text-left text-sm font-semibold text-gray-700">ID</th>
                    <th className="px-6 py-3 text-left text-sm font-semibold text-gray-700">Nome</th>
                    <th className="px-6 py-3 text-left text-sm font-semibold text-gray-700">Tipo</th>
                    <th className="px-6 py-3 text-left text-sm font-semibold text-gray-700">Data e Hora</th>
                    <th className="px-6 py-3 text-left text-sm font-semibold text-gray-700">Status</th>
                  </tr>
                </thead>
                <tbody>
                  {presences.map((presence) => (
                    <tr key={presence.id} className="border-b border-gray-200 hover:bg-gray-50 transition-colors">
                      <td className="px-6 py-4 text-sm text-gray-800">{presence.id}</td>
                      <td className="px-6 py-4 text-sm text-gray-800">{presence.name}</td>
                      <td className="px-6 py-4 text-sm">
                        <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                          searchFilters.searchStudents ? 'bg-blue-100 text-blue-800' : 'bg-purple-100 text-purple-800'
                        }`}>
                          {searchFilters.searchStudents ? 'Aluno' : 'Funcionário'}
                        </span>
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-800">{formatDateTime(presence.createdAt)}</td>
                      <td className="px-6 py-4 text-sm">
                        <span className="px-3 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800">
                          Presente
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              <div className="mt-4 text-sm text-gray-600">
                Total de registros: <span className="font-semibold">{presences.length}</span>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Presences;

