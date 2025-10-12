# Sistema de Reconhecimento Facial - Frontend

Sistema completo de gerenciamento de alunos, funcionários e presenças com reconhecimento facial.

## Tecnologias

- React 18
- Vite
- React Router DOM
- Axios
- Tailwind CSS

## Estrutura do Projeto

```
src/
├── api/
│   ├── components/
│   │   ├── Dashboard.jsx          # Dashboard principal com navegação por tabs
│   │   ├── FormLogin.jsx          # Formulário de login
│   │   ├── ManageStudents.jsx     # Gerenciamento completo de alunos
│   │   ├── ManageEmployees.jsx    # Gerenciamento completo de funcionários
│   │   └── Presences.jsx          # Consulta de presenças registradas
│   ├── apiClient.js               # Configuração do Axios e interceptors
│   ├── authApi.js                 # API de autenticação e registro
│   ├── studentApi.js              # API de consulta de alunos
│   ├── employeeApi.js             # API de consulta de funcionários
│   ├── presenceApi.js             # API de consulta de presenças
│   └── recordApi.js               # API de gerenciamento de registros
├── App.jsx                        # Configuração de rotas
└── main.jsx                       # Entrada da aplicação
```

## Funcionalidades

### Autenticação
- Login com validação de credenciais
- Armazenamento de token JWT
- Controle de acesso baseado em roles

### Gerenciamento de Alunos
- Adicionar aluno com foto, matrícula e série
- Buscar alunos por nome, matrícula, série ou status
- Editar nome do aluno
- Atualizar foto do aluno
- Desativar aluno
- Deletar registro de aluno
- Suporte completo para séries (1º ao 9º ano e 1º ao 3º EM)

### Gerenciamento de Funcionários
- Adicionar funcionário com foto, CPF, cargo e descrição
- Buscar funcionários por nome, CPF, cargo ou status
- Editar nome do funcionário
- Atualizar foto do funcionário
- Desativar funcionário
- Deletar registro de funcionário
- Registrar novo usuário funcionário (apenas ADMIN e DBA)
- Restrição de acesso para FUNCIONARIO (não pode adicionar/registrar funcionários)

### Consulta de Presenças
- Visualizar histórico de presenças
- Filtrar por período (data início e fim)
- Filtrar por nome, série ou cargo
- Alternar entre busca de alunos e funcionários
- Exibição formatada de data e hora

## Controle de Acesso

### Roles Disponíveis:
- **ADMIN**: Acesso total ao sistema
- **DBA**: Acesso total ao sistema
- **FUNCIONARIO**: Acesso restrito - não pode adicionar ou registrar funcionários

## Instalação

```bash
npm install
```

## Executar em Desenvolvimento

```bash
npm run dev
```

## Build para Produção

```bash
npm run build
```

## Configuração da API

O backend está configurado para rodar em `http://127.0.0.1:8080/api`

Para alterar, edite o arquivo `src/api/apiClient.js`:

```javascript
const apiClient = axios.create({
  baseURL: "http://127.0.0.1:8080/api",
  ...
});
```

## Rotas

- `/` - Redireciona para `/login`
- `/login` - Página de login
- `/dashboard` - Dashboard principal (requer autenticação)

## Endpoints da API

### Autenticação
- `POST /auth/login` - Login
- `POST /register/employee` - Registrar funcionário

### Alunos
- `GET /students/search` - Buscar alunos

### Funcionários
- `GET /employees/search` - Buscar funcionários

### Presenças
- `GET /presences/search` - Buscar presenças

### Registros
- `POST /records/add-student` - Adicionar registro de aluno
- `POST /records/add-employee` - Adicionar registro de funcionário
- `PUT /records/update-name/:id` - Atualizar nome
- `PUT /records/update-photo/:id` - Atualizar foto
- `PUT /records/deactivate/:id` - Desativar registro
- `DELETE /records/delete/:id` - Deletar registro

## Características Técnicas

- Interceptors para gerenciamento automático de tokens
- Redirecionamento automático para login em caso de 401
- Validação de formulários
- Loading states
- Tratamento de erros
- Interface responsiva com Tailwind CSS
- Componentes reutilizáveis
- Clean code sem comentários
