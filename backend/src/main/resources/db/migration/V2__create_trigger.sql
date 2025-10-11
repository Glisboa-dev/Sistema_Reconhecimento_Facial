-- CADASTRO
DROP TRIGGER IF EXISTS TRG_INSERT_CADASTRO;
CREATE TRIGGER TRG_INSERT_CADASTRO
    AFTER INSERT ON CADASTRO
    FOR EACH ROW
BEGIN
    INSERT INTO AUDITORIA (
        tabela_afetada,
        data_hora,
        registro_novo,
        tipo_operacao,
        registro_anterior,
        id_presenca,
        id_cadastro
    )
    VALUES (
               'CADASTRO',
               CURRENT_TIMESTAMP(6),
               CONCAT('NOME: ', NEW.nome, ', STATUS: ', NEW.status, ', FOTO: ', NEW.foto, ', TYPE: ', NEW.`type`),
               'INSERT',
               NULL,
               NULL,
               NEW.id
           );
END;

DROP TRIGGER IF EXISTS TRG_UPDATE_CADASTRO;
CREATE TRIGGER TRG_UPDATE_CADASTRO
    AFTER UPDATE ON CADASTRO
    FOR EACH ROW
BEGIN
    INSERT INTO AUDITORIA (
        tabela_afetada,
        data_hora,
        registro_novo,
        tipo_operacao,
        registro_anterior,
        id_presenca,
        id_cadastro
    )
    VALUES (
               'CADASTRO',
               CURRENT_TIMESTAMP(6),
               CONCAT('NOME: ', NEW.nome, ', STATUS: ', NEW.status, ', FOTO: ', NEW.foto, ', TYPE: ', NEW.`type`),
               'UPDATE',
               CONCAT('NOME: ', OLD.nome, ', STATUS: ', OLD.status, ', FOTO: ', OLD.foto, ', TYPE: ', OLD.`type`),
               NULL,
               NEW.id
           );
END;

DROP TRIGGER IF EXISTS TRG_DELETE_CADASTRO;
CREATE TRIGGER TRG_DELETE_CADASTRO
    AFTER DELETE ON CADASTRO
    FOR EACH ROW
BEGIN
    INSERT INTO AUDITORIA (
        tabela_afetada,
        data_hora,
        registro_novo,
        tipo_operacao,
        registro_anterior,
        id_presenca,
        id_cadastro
    )
    VALUES (
               'CADASTRO',
               CURRENT_TIMESTAMP(6),
               NULL,
               'DELETE',
               CONCAT('NOME: ', OLD.nome, ', STATUS: ', OLD.status, ', FOTO: ', OLD.foto, ', TYPE: ', OLD.`type`),
               NULL,
               OLD.id
           );
END;

-- ALUNO
DROP TRIGGER IF EXISTS TRG_INSERT_ALUNO;
CREATE TRIGGER TRG_INSERT_ALUNO
    AFTER INSERT ON ALUNO
    FOR EACH ROW
BEGIN
    INSERT INTO AUDITORIA (
        tabela_afetada,
        data_hora,
        registro_novo,
        tipo_operacao,
        registro_anterior,
        id_presenca,
        id_cadastro
    )
    VALUES (
               'ALUNO',
               CURRENT_TIMESTAMP(6),
               CONCAT('MATRICULA: ', NEW.matricula, ', SERIE: ', NEW.serie),
               'INSERT',
               NULL,
               NULL,
               NEW.id_cadastro
           );
END;

DROP TRIGGER IF EXISTS TRG_UPDATE_ALUNO;
CREATE TRIGGER TRG_UPDATE_ALUNO
    AFTER UPDATE ON ALUNO
    FOR EACH ROW
BEGIN
    INSERT INTO AUDITORIA (
        tabela_afetada,
        data_hora,
        registro_novo,
        tipo_operacao,
        registro_anterior,
        id_presenca,
        id_cadastro
    )
    VALUES (
               'ALUNO',
               CURRENT_TIMESTAMP(6),
               CONCAT('MATRICULA: ', NEW.matricula, ', SERIE: ', NEW.serie),
               'UPDATE',
               CONCAT('MATRICULA: ', OLD.matricula, ', SERIE: ', OLD.serie),
               NULL,
               NEW.id_cadastro
           );
END;

DROP TRIGGER IF EXISTS TRG_DELETE_ALUNO;
CREATE TRIGGER TRG_DELETE_ALUNO
    AFTER DELETE ON ALUNO
    FOR EACH ROW
BEGIN
    INSERT INTO AUDITORIA (
        tabela_afetada,
        data_hora,
        registro_novo,
        tipo_operacao,
        registro_anterior,
        id_presenca,
        id_cadastro
    )
    VALUES (
               'ALUNO',
               CURRENT_TIMESTAMP(6),
               NULL,
               'DELETE',
               CONCAT('MATRICULA: ', OLD.matricula, ', SERIE: ', OLD.serie),
               NULL,
               OLD.id_cadastro
           );
END;

-- FUNCIONARIO
DROP TRIGGER IF EXISTS TRG_INSERT_FUNCIONARIO;
CREATE TRIGGER TRG_INSERT_FUNCIONARIO
    AFTER INSERT ON FUNCIONARIO
    FOR EACH ROW
BEGIN
    INSERT INTO AUDITORIA (
        tabela_afetada,
        data_hora,
        registro_novo,
        tipo_operacao,
        registro_anterior,
        id_presenca,
        id_cadastro
    )
    VALUES (
               'FUNCIONARIO',
               CURRENT_TIMESTAMP(6),
               CONCAT('CPF: ', NEW.cpf, ', CARGO: ', NEW.cargo, ', DESCRICAO: ', NEW.descricao),
               'INSERT',
               NULL,
               NULL,
               NEW.id_cadastro
           );
END;

DROP TRIGGER IF EXISTS TRG_UPDATE_FUNCIONARIO;
CREATE TRIGGER TRG_UPDATE_FUNCIONARIO
    AFTER UPDATE ON FUNCIONARIO
    FOR EACH ROW
BEGIN
    INSERT INTO AUDITORIA (
        tabela_afetada,
        data_hora,
        registro_novo,
        tipo_operacao,
        registro_anterior,
        id_presenca,
        id_cadastro
    )
    VALUES (
               'FUNCIONARIO',
               CURRENT_TIMESTAMP(6),
               CONCAT('CPF: ', NEW.cpf, ', CARGO: ', NEW.cargo, ', DESCRICAO: ', NEW.descricao),
               'UPDATE',
               CONCAT('CPF: ', OLD.cpf, ', CARGO: ', OLD.cargo, ', DESCRICAO: ', OLD.descricao),
               NULL,
               NEW.id_cadastro
           );
END;

DROP TRIGGER IF EXISTS TRG_DELETE_FUNCIONARIO;
CREATE TRIGGER TRG_DELETE_FUNCIONARIO
    AFTER DELETE ON FUNCIONARIO
    FOR EACH ROW
BEGIN
    INSERT INTO AUDITORIA (
        tabela_afetada,
        data_hora,
        registro_novo,
        tipo_operacao,
        registro_anterior,
        id_presenca,
        id_cadastro
    )
    VALUES (
               'FUNCIONARIO',
               CURRENT_TIMESTAMP(6),
               NULL,
               'DELETE',
               CONCAT('CPF: ', OLD.cpf, ', CARGO: ', OLD.cargo, ', DESCRICAO: ', OLD.descricao),
               NULL,
               OLD.id_cadastro
           );
END;

-- PRESENCA
DROP TRIGGER IF EXISTS TRG_INSERT_PRESENCA;
CREATE TRIGGER TRG_INSERT_PRESENCA
    AFTER INSERT ON PRESENCA
    FOR EACH ROW
BEGIN
    INSERT INTO AUDITORIA (
        tabela_afetada,
        data_hora,
        registro_novo,
        tipo_operacao,
        registro_anterior,
        id_presenca,
        id_cadastro
    )
    VALUES (
               'PRESENCA',
               CURRENT_TIMESTAMP(6),
               CONCAT('DATA_HORA: ', NEW.data_hora),
               'INSERT',
               NULL,
               NEW.id,
               NEW.id_cadastro
           );
END;

DROP TRIGGER IF EXISTS TRG_UPDATE_PRESENCA;
CREATE TRIGGER TRG_UPDATE_PRESENCA
    AFTER UPDATE ON PRESENCA
    FOR EACH ROW
BEGIN
    INSERT INTO AUDITORIA (
        tabela_afetada,
        data_hora,
        registro_novo,
        tipo_operacao,
        registro_anterior,
        id_presenca,
        id_cadastro
    )
    VALUES (
               'PRESENCA',
               CURRENT_TIMESTAMP(6),
               CONCAT('DATA_HORA: ', NEW.data_hora),
               'UPDATE',
               CONCAT('DATA_HORA: ', OLD.data_hora),
               NEW.id,
               NEW.id_cadastro
           );
END;

DROP TRIGGER IF EXISTS TRG_DELETE_PRESENCA;
CREATE TRIGGER TRG_DELETE_PRESENCA
    AFTER DELETE ON PRESENCA
    FOR EACH ROW
BEGIN
    INSERT INTO AUDITORIA (
        tabela_afetada,
        data_hora,
        registro_novo,
        tipo_operacao,
        registro_anterior,
        id_presenca,
        id_cadastro
    )
    VALUES (
               'PRESENCA',
               CURRENT_TIMESTAMP(6),
               NULL,
               'DELETE',
               CONCAT('DATA_HORA: ', OLD.data_hora),
               OLD.id,
               OLD.id_cadastro
           );
END;
