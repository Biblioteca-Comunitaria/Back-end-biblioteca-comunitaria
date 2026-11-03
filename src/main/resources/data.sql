-- Leitores fictícios (RF1: os leitores vêm de um script de dados iniciais).
-- Cada INSERT só grava se o leitor ainda não existir.

INSERT INTO leitores (nome)
SELECT N'Maria Souza'
WHERE NOT EXISTS (
    SELECT 1 FROM leitores WHERE nome = N'Maria Souza'
);

INSERT INTO leitores (nome)
SELECT N'João Pereira'
WHERE NOT EXISTS (
    SELECT 1 FROM leitores WHERE nome = N'João Pereira'
);

INSERT INTO leitores (nome)
SELECT N'Ana Lima'
WHERE NOT EXISTS (
    SELECT 1 FROM leitores WHERE nome = N'Ana Lima'
);

INSERT INTO leitores (nome)
SELECT N'Carlos Mendes'
WHERE NOT EXISTS (
    SELECT 1 FROM leitores WHERE nome = N'Carlos Mendes'
);
