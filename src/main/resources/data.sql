-- Leitores ficticios (RF1: os leitores vem de um script de dados iniciais).
-- Cada INSERT so grava se o leitor ainda nao existir, entao reiniciar a aplicacao nao duplica nada.
INSERT INTO leitores (nome) SELECT 'Maria Souza' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM leitores WHERE nome = 'Maria Souza');
INSERT INTO leitores (nome) SELECT 'João Pereira' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM leitores WHERE nome = 'João Pereira');
INSERT INTO leitores (nome) SELECT 'Ana Lima' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM leitores WHERE nome = 'Ana Lima');
INSERT INTO leitores (nome) SELECT 'Carlos Mendes' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM leitores WHERE nome = 'Carlos Mendes');
