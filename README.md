# Sistema de Reconhecimento Facial

Sistema completo de gerenciamento de presenças escolares utilizando reconhecimento facial com inteligência artificial.

## 📋 Visão Geral

Este sistema permite o gerenciamento de alunos, funcionários e registro automático de presenças através de reconhecimento facial. O sistema é composto por três componentes principais:

- **Frontend**: Interface web em React para gerenciamento
- **Backend**: API REST em Spring Boot para lógica de negócio
- **Serviço de Reconhecimento**: Processamento de reconhecimento facial em Python

## 🏗️ Arquitetura do Sistema

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│    Frontend     │    │    Backend      │    │ Reconhecimento  │
│    (React)      │◄──►│ (Spring Boot)   │◄──►│    (Python)     │
│   Porta: 5173   │    │   Porta: 8080   │    │   Porta: 5001   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                              ▼
                       ┌─────────────────┐
                       │     MinIO       │
                       │ (Armazenamento) │
                       │  Porta: 9000/1  │
                       └─────────────────┘
                              │
                              ▼
                       ┌─────────────────┐
                       │     MySQL       │
                       │   (Database)    │
                       │   Porta: 3306   │
                       └─────────────────┘
```

## 🚀 Pré-requisitos

Antes de executar o sistema, certifique-se de ter instalado:

- **Docker** e **Docker Compose**
- **Java 17** ou superior
- **Node.js 18** ou superior
- **Python 3.8** ou superior
- **MySQL 8.0** ou superior
- **Webcam** para o serviço de reconhecimento facial

## 🐳 Configuração do MinIO

O MinIO é usado para armazenar as imagens das faces dos usuários. Execute o seguinte comando Docker para iniciar o MinIO:

```bash
docker run -d \
  --name minio \
  -e MINIO_ROOT_USER=minioadmin \
  -e MINIO_ROOT_PASSWORD=minioadmin \
  -e MINIO_ENCRYPT_ALL_BUCKETS_KEY="NWkyiBo4+Tosv0nEsUj+eutfs2YS3+BmtqbbFPnKeUs=" \
  -p 9000:9000 \
  -p 9001:9001 \
  quay.io/minio/minio server /data --console-address ":9001"
```

### Acesso ao MinIO:
- **Console Web**: http://localhost:9001
- **Usuário**: minioadmin
- **Senha**: minioadmin
- **API**: http://localhost:9000

## 🗄️ Configuração do Banco de Dados

1. Crie um banco de dados MySQL chamado `escola`:

```sql
CREATE DATABASE escola;
```

2. Configure as credenciais no arquivo `backend/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/escola
    username: root
    password: password
```

## 🛠️ Instalação e Execução

### 1. Backend (Spring Boot)

```bash
cd backend
./gradlew bootRun
```

O backend estará disponível em: http://localhost:8080

### 2. Frontend (React)

```bash
cd frontend
npm install
npm run dev
```

O frontend estará disponível em: http://localhost:5173

### 3. Serviço de Reconhecimento Facial

```bash
cd recognition-service
pip install -r requirements.txt  # Se houver arquivo requirements.txt
python main.py
```

O serviço estará disponível em: http://localhost:5001

## 📱 Funcionalidades

### 🔐 Autenticação
- Sistema de login com JWT
- Controle de acesso baseado em roles (ADMIN, DBA, FUNCIONARIO)
- Redirecionamento automático para login

### 👨‍🎓 Gerenciamento de Alunos
- Cadastro de alunos com foto, matrícula e série
- Busca avançada por nome, matrícula, série ou status
- Edição de informações do aluno
- Atualização de fotos
- Desativação e exclusão de registros
- Suporte completo para séries (1º ao 9º ano e 1º ao 3º EM)

### 👨‍💼 Gerenciamento de Funcionários
- Cadastro de funcionários com foto, CPF, cargo e descrição
- Busca avançada por nome, CPF, cargo ou status
- Edição de informações do funcionário
- Atualização de fotos
- Desativação e exclusão de registros
- Sistema de registro de novos usuários funcionários

### 📊 Consulta de Presenças
- Visualização do histórico de presenças
- Filtros por período (data início e fim)
- Filtros por nome, série ou cargo
- Alternância entre busca de alunos e funcionários
- Exibição formatada de data e hora

### 🤖 Reconhecimento Facial
- Detecção automática de faces em tempo real
- Reconhecimento usando modelo Facenet
- Registro automático de presenças
- Sistema de alertas para faces não reconhecidas
- Otimizações de performance com threading

## 🔧 Configurações Técnicas

### Backend
- **Framework**: Spring Boot 3.x
- **Database**: MySQL com Flyway para migrações
- **Segurança**: Spring Security com JWT
- **Storage**: MinIO para armazenamento de imagens
- **API**: REST com documentação automática

### Frontend
- **Framework**: React 18 com Vite
- **Styling**: Tailwind CSS
- **HTTP Client**: Axios com interceptors
- **Routing**: React Router DOM
- **State Management**: Context API

### Reconhecimento Facial
- **Framework**: Flask
- **IA**: DeepFace com modelo Facenet
- **Detecção**: MTCNN para detecção de faces
- **Processamento**: OpenCV para manipulação de imagens
- **Storage**: MinIO para galeria de faces

## 📊 Controle de Acesso

### Roles Disponíveis:
- **ADMIN**: Acesso total ao sistema
- **DBA**: Acesso total ao sistema
- **FUNCIONARIO**: Acesso restrito - não pode adicionar ou registrar funcionários

## 🔒 Segurança

- Tokens JWT para autenticação
- Criptografia de dados sensíveis
- Validação de entrada em todos os endpoints
- Controle de acesso baseado em roles
- Armazenamento seguro de imagens no MinIO

## 📈 Performance

- Processamento assíncrono de reconhecimento facial
- Threading para operações pesadas
- Cache de embeddings de faces
- Otimizações de banco de dados
- Compressão de imagens

## 🐛 Solução de Problemas

### MinIO não inicia
- Verifique se a porta 9000/9001 não está em uso
- Confirme se o Docker está rodando
- Verifique os logs do container: `docker logs minio`

### Reconhecimento facial não funciona
- Verifique se a webcam está conectada
- Confirme se as dependências Python estão instaladas
- Verifique se o MinIO está acessível

### Backend não conecta ao banco
- Verifique se o MySQL está rodando
- Confirme as credenciais no application.yml
- Verifique se o banco `escola` existe

## 📝 Logs e Monitoramento

- Logs do backend em console
- Logs do reconhecimento facial com timestamps
- Sistema de alertas para faces não reconhecidas
- Monitoramento de performance em tempo real

## 🤝 Contribuição

Para contribuir com o projeto:

1. Faça um fork do repositório
2. Crie uma branch para sua feature
3. Commit suas mudanças
4. Push para a branch
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob licença MIT. Veja o arquivo LICENSE para mais detalhes.

## 📞 Suporte

Para suporte técnico ou dúvidas sobre o sistema, entre em contato através dos issues do repositório.
