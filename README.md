# ProjetoBiblioteca

API REST de uma biblioteca integrada ao front React.

## Configuração

O banco é MySQL e a API exige login (HTTP Basic) de um bibliotecário cadastrado na tabela `bibliotecarios` para as rotas protegidas.
Nenhuma senha fica no código: a do MySQL vem de variável de ambiente e as dos bibliotecários ficam no banco como hash BCrypt.

| Variável | Obrigatória | Padrão | Para que serve |
|---|---|---|---|
| `MYSQL_PASSWORD` | sim | - | senha do MySQL |
| `MYSQL_USER` | não | `root` | usuário do MySQL |
| `MYSQL_HOST` | não | `localhost` | host do MySQL |
| `MYSQL_DATABASE` | não | `biblioteca` | nome do banco (é criado se não existir) |
| `BIBLIOTECA_SENHA` | só na 1ª execução | `123456` | senha do bibliotecário inicial |
| `BIBLIOTECA_USUARIO` | não | `bibliotecario` | usuário do bibliotecário inicial |
| `BIBLIOTECA_EMAIL` | não | - | e-mail do bibliotecário inicial (usado no "Esqueci a senha") |
| `BIBLIOTECA_CORS_ORIGINS` | não | `http://localhost:5173,...` | origens do front liberadas no CORS |
| `BREVO_SMTP_PASSWORD` | para enviar e-mail | - | senha/chave SMTP do Brevo |

O primeiro bibliotecário é criado na inicialização, a partir das variáveis acima, **apenas se a tabela `bibliotecarios` estiver vazia**.
Depois que existir um bibliotecário, `BIBLIOTECA_SENHA` não é mais necessária (e mudá-la não altera a senha de ninguém).
Também é possível criar novos bibliotecários pela tela de cadastro do front ou por `POST /bibliotecarios` (logado).

## Executar

    export MYSQL_PASSWORD='...'
    export BIBLIOTECA_SENHA='...'
    export BIBLIOTECA_EMAIL='seu-email@exemplo.com'
    ./mvnw spring-boot:run      # http://localhost:8081

## Requisitos e endpoints

As rotas públicas de autenticação não exigem credenciais. As demais exigem login; sem credenciais ou com senha errada a resposta é `401`.

| Acesso | Endpoint |
|---|---|
| Público: cadastro de bibliotecário (usado pela tela de cadastro) | `POST /auth/cadastro` |
| Público: enviar código de recuperação para o e-mail cadastrado | `POST /auth/esqueci-senha` |
| Público: trocar a senha com o código recebido | `POST /auth/redefinir-senha` |
| Login (confere usuário e senha e devolve os dados de quem entrou) | `POST /auth/login` |
| Cadastrar bibliotecário (só logado; usuário 3–50 caracteres `A-Z a-z 0-9 . _ -`, senha de 8 a 72) | `POST /bibliotecarios` |

| Requisito | Endpoint |
|---|---|
| RF1 Cadastrar livro (título, autor, ISBN único, exemplares ≥ 1; gênero, editora e ano são opcionais) | `POST /livros` |
| RF2 Buscar por título ou autor, com exemplares disponíveis | `GET /livros?q=termo` |
| RF3 Emprestar (exemplar disponível, leitor com < 2 ativos, prazo +14 dias) | `POST /emprestimos` |
| RF4 Devolver (registra a data da devolução) | `PUT /emprestimos/{id}/devolucao` |
| RF5 Listar empréstimos atrasados | `GET /emprestimos/atrasados` |
| Listar todos os empréstimos (tela de empréstimos do front) | `GET /emprestimos` |
| Apoio: consultar os leitores carregados pelo script | `GET /leitores` |

Exemplos (`-u usuario:senha` envia o login):

    curl -X POST localhost:8081/auth/cadastro -H 'Content-Type: application/json' \
      -d '{"nome":"Maria Silva","usuario":"maria.silva","email":"maria@exemplo.com","senha":"senha-forte-123"}'

    curl -u bibliotecario:$BIBLIOTECA_SENHA -X POST localhost:8081/auth/login

    curl -u bibliotecario:$BIBLIOTECA_SENHA -X POST localhost:8081/bibliotecarios -H 'Content-Type: application/json' \
      -d '{"nome":"Maria Silva","usuario":"maria.silva","email":"maria@exemplo.com","senha":"senha-forte-123"}'

    curl -X POST localhost:8081/auth/esqueci-senha -H 'Content-Type: application/json' \
      -d '{"email":"maria@exemplo.com"}'

    curl -X POST localhost:8081/auth/redefinir-senha -H 'Content-Type: application/json' \
      -d '{"email":"maria@exemplo.com","codigo":"123456","novaSenha":"nova-senha-123"}'

    curl -u bibliotecario:$BIBLIOTECA_SENHA -X POST localhost:8081/livros -H 'Content-Type: application/json' \
      -d '{"titulo":"Dom Casmurro","autor":"Machado de Assis","isbn":"978-0-000-00001-1","quantidadeExemplares":2}'

    curl -u bibliotecario:$BIBLIOTECA_SENHA 'localhost:8081/livros?q=machado'

    curl -u bibliotecario:$BIBLIOTECA_SENHA -X POST localhost:8081/emprestimos -H 'Content-Type: application/json' \
      -d '{"livroId":1,"leitorId":1}'

    curl -u bibliotecario:$BIBLIOTECA_SENHA -X PUT localhost:8081/emprestimos/1/devolucao
    curl -u bibliotecario:$BIBLIOTECA_SENHA localhost:8081/emprestimos
    curl -u bibliotecario:$BIBLIOTECA_SENHA localhost:8081/emprestimos/atrasados

Outros códigos: `409` (usuário/e-mail ou ISBN repetido, sem exemplar, limite de 2 empréstimos, devolução repetida),
`404` (livro, leitor ou empréstimo inexistente) e `400` (dados inválidos, código de recuperação inválido/expirado).

O código de recuperação tem 6 dígitos e expira em 15 minutos. Se o SMTP ainda não estiver configurado, o código
também aparece no console do servidor para facilitar os testes locais.

O login usa HTTP Basic: usuário e senha seguem em todas as requisições, então use HTTPS fora do ambiente local.
