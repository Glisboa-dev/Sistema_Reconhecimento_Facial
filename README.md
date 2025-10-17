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


