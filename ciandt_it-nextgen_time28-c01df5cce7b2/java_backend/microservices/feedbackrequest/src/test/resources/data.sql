-- schema.sql

-- Removendo tabelas existentes (use com cuidado em produção)
DROP TABLE IF EXISTS feedback_request_answers;
DROP TABLE IF EXISTS feedback_request_appraisers;
DROP TABLE IF EXISTS feedback_request_questions;
DROP TABLE IF EXISTS feedback_requests;

-- Criando tabela principal de solicitações de feedback
CREATE TABLE feedback_requests (
    id VARCHAR(36) PRIMARY KEY,
    requester_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    approved_at TIMESTAMP,
    edited_at TIMESTAMP,
    rejected_at TIMESTAMP,
    reject_message VARCHAR(500)
);

-- Tabela para armazenar as perguntas de cada solicitação de feedback
CREATE TABLE feedback_request_questions (
    feedback_request_id VARCHAR(36) NOT NULL,
    text VARCHAR(255) NOT NULL,
    -- JPA/Hibernate adiciona automaticamente este campo para gerenciar a ordem dos elementos
    questions_order INT NOT NULL,
    FOREIGN KEY (feedback_request_id) REFERENCES feedback_requests(id),
    PRIMARY KEY (feedback_request_id, questions_order)
);

-- Tabela para armazenar os avaliadores de cada solicitação de feedback
CREATE TABLE feedback_request_appraisers (
    feedback_request_id VARCHAR(36) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    -- JPA/Hibernate adiciona automaticamente este campo para gerenciar a ordem dos elementos
    appraisers_order INT NOT NULL,
    FOREIGN KEY (feedback_request_id) REFERENCES feedback_requests(id),
    PRIMARY KEY (feedback_request_id, appraisers_order)
);

-- Tabela para armazenar as respostas às perguntas
CREATE TABLE feedback_request_answers (
    feedback_request_id VARCHAR(36) NOT NULL,
    appraiser_email VARCHAR(100) NOT NULL,
    text TEXT NOT NULL,
    -- JPA/Hibernate adiciona automaticamente este campo para gerenciar a ordem dos elementos
    answers_order INT NOT NULL,
    FOREIGN KEY (feedback_request_id) REFERENCES feedback_requests(id),
    PRIMARY KEY (feedback_request_id, answers_order)
);

-- Inserindo Feedback Requests
INSERT INTO feedback_requests (id, requester_id, created_at, approved_at)
VALUES
('550e8400-e29b-41d4-a716-446655440000', 101, '2023-01-10T14:00:00', '2023-01-11T10:00:00'), -- Feedback aprovado/completo
('550e8400-e29b-41d4-a716-446655440001', 101, '2023-01-15T10:30:00', '2023-01-16T11:00:00'), -- Feedback aprovado/pendente
('550e8400-e29b-41d4-a716-446655440002', 102, '2023-01-20T09:15:00', NULL); -- Feedback não aprovado

-- Inserindo Questions para o primeiro Feedback Request
INSERT INTO feedback_request_questions (feedback_request_id, text, questions_order)
VALUES
  ('550e8400-e29b-41d4-a716-446655440000', 'Como você avalia o desempenho técnico?', 0),
  ('550e8400-e29b-41d4-a716-446655440000', 'Como é a comunicação com o time?', 1),
  ('550e8400-e29b-41d4-a716-446655440000', 'Sugestões de melhoria?', 2);

-- Inserindo Questions para o segundo Feedback Request
INSERT INTO feedback_request_questions (feedback_request_id, text, questions_order)
VALUES
  ('550e8400-e29b-41d4-a716-446655440001', 'Pontos fortes do colaborador?', 0),
  ('550e8400-e29b-41d4-a716-446655440001', 'Pontos de desenvolvimento?', 1);

-- Inserindo Questions para o terceiro Feedback Request
INSERT INTO feedback_request_questions (feedback_request_id, text, questions_order)
VALUES
  ('550e8400-e29b-41d4-a716-446655440002', 'Feedback geral sobre o último trimestre?', 0),
  ('550e8400-e29b-41d4-a716-446655440002', 'Expectativas para o próximo trimestre?', 1);

-- Inserindo Appraisers para o primeiro Feedback Request
INSERT INTO feedback_request_appraisers (feedback_request_id, name, email, appraisers_order)
VALUES
  ('550e8400-e29b-41d4-a716-446655440000', 'João Silva', 'joao.silva@example.com', 0),
  ('550e8400-e29b-41d4-a716-446655440000', 'Maria Souza', 'maria.souza@example.com', 1);

-- Inserindo Appraisers para o segundo Feedback Request
INSERT INTO feedback_request_appraisers (feedback_request_id, name, email, appraisers_order)
VALUES
  ('550e8400-e29b-41d4-a716-446655440001', 'Carlos Pereira', 'carlos.pereira@example.com', 0),
  ('550e8400-e29b-41d4-a716-446655440001', 'Ana Costa', 'ana.costa@example.com', 1);

-- Inserindo Appraisers para o terceiro Feedback Request
INSERT INTO feedback_request_appraisers (feedback_request_id, name, email, appraisers_order)
VALUES
  ('550e8400-e29b-41d4-a716-446655440002', 'Pedro Santos', 'pedro.santos@example.com', 0),
  ('550e8400-e29b-41d4-a716-446655440002', 'Luiza Mendes', 'luiza.mendes@example.com', 1);

-- Inserindo Answers para o primeiro Feedback Request
INSERT INTO feedback_request_answers (feedback_request_id, appraiser_email, text, answers_order)
VALUES
  ('550e8400-e29b-41d4-a716-446655440000', 'joao.silva@example.com', 'O desempenho técnico é excelente.', 0),
  ('550e8400-e29b-41d4-a716-446655440000', 'joao.silva@example.com', 'Comunicação clara e eficiente.', 1),
  ('550e8400-e29b-41d4-a716-446655440000', 'joao.silva@example.com', 'Pode melhorar a documentação do código.', 2),
  ('550e8400-e29b-41d4-a716-446655440000', 'maria.souza@example.com', 'Bom conhecimento técnico.', 3),
  ('550e8400-e29b-41d4-a716-446655440000', 'maria.souza@example.com', 'Poderia participar mais das reuniões.', 4),
  ('550e8400-e29b-41d4-a716-446655440000', 'maria.souza@example.com', 'Sugestão: fazer mais code reviews.', 5);

-- Inserindo Answers para o segundo Feedback Request (apenas do appraiser Carlos)
INSERT INTO feedback_request_answers (feedback_request_id, appraiser_email, text, answers_order)
VALUES
  ('550e8400-e29b-41d4-a716-446655440001', 'carlos.pereira@example.com', 'Pontos fortes: liderança e iniciativa.', 0),
  ('550e8400-e29b-41d4-a716-446655440001', 'carlos.pereira@example.com', 'Desenvolvimento: gestão de tempo.', 1);
