#Guilherme Luiz Pavan Vial RA: 1139397

# BiblioDigital

O BiblioDigital é um projeto feito em Java para gerenciar uma pequena biblioteca pelo terminal.

Nele, é possível cadastrar livros físicos, digitais e audiolivros, pesquisar por título ou autor, alterar as informações de um livro e excluir um cadastro. O programa também verifica se o título já existe e se os dados informados são válidos.

## Como executar

1. Abra a pasta do projeto no IntelliJ IDEA.
2. Use o Java 25, que já está configurado no `pom.xml`.
3. Execute a classe `BiblioDigitalApplication`, dentro da pasta `views`. Também é possível executar o arquivo `BiblioDigital.java`.
4. Escolha as opções do menu para usar o programa.

## Organização do projeto

- `entities`: contém as classes dos livros.
- `repositories`: guarda os livros em uma lista.
- `services`: faz as validações e controla as operações.
- `views`: mostra o menu e recebe as informações do usuário.

O projeto usa Java e Maven, sem banco de dados. Os livros ficam salvos apenas enquanto o programa está aberto.
