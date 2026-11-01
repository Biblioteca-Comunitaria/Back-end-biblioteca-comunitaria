# Biblioteca Luiz Brasil

Aplicação React (Vite) de biblioteca comunitária, somente versão admin.

## Como rodar

```bash
npm install
npm run dev      # desenvolvimento
npm run build    # build de produção em dist/
```

## Estrutura

- `src/utils.js` – datas, busca sem acento e dados iniciais (leitores fictícios, livros, empréstimos)
- `src/App.jsx` – estado, regras de negócio (cadastrar, emprestar, devolver) e navegação
- `src/components/Books.jsx` – lista, detalhe/empréstimo e cadastro de livros
- `src/components/Loans.jsx` – lista e detalhe de empréstimos (Todos/Ativos/Atrasados/Devolvidos)
- `src/styles.css` – estilos e tema claro/escuro

Os dados ficam no localStorage do navegador.
