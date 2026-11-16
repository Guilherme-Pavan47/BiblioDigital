package br.edu.atitus.bibliodigital.views;

import br.edu.atitus.bibliodigital.entities.AudioLivro;
import br.edu.atitus.bibliodigital.entities.Livro;
import br.edu.atitus.bibliodigital.entities.LivroDigital;
import br.edu.atitus.bibliodigital.entities.LivroFisico;
import br.edu.atitus.bibliodigital.services.LivroService;

import java.util.List;

public class BiblioDigitalApplication {
    private final LivroService service = new LivroService();

    public void main() {
        int opcao = -1;
        do {
            mostrarMenu();
            try {
                opcao = Integer.parseInt(IO.readln("Escolha uma das opções acima: "));
                switch (opcao) {
                    case 1 -> cadastrar();
                    case 2 -> pesquisarPorTitulo();
                    case 3 -> pesquisarPorAutor();
                    case 4 -> alterar();
                    case 5 -> excluir();
                    case 0 -> IO.println("Até logo!");
                    default -> IO.println("Opção inválida. Tente novamente.");
                }
            } catch (NumberFormatException ex) {
                IO.println("Erro: informe um número válido.");
            } catch (RuntimeException ex) {
                IO.println("Erro: " + ex.getMessage());
            }
        } while (opcao != 0);
    }

    private void mostrarMenu() {
        IO.println("""
                ===== Bem vindo ao BiblioDigital =====

                Menu:
                1 - Cadastrar novo Livro
                2 - Pesquisar Livro por título
                3 - Pesquisar Livro por autor
                4 - Alterar dados do Livro
                5 - Excluir Livro
                0 - Sair
                """);
    }

    private void cadastrar() {
        IO.println("""
                Qual tipo de livro deseja cadastrar?
                1 - Livro físico
                2 - Livro digital
                3 - Audiolivro
                """);
        int tipo = Integer.parseInt(IO.readln("Escolha uma opção: "));
        if (tipo < 1 || tipo > 3)
            throw new RuntimeException("Tipo de livro inválido.");

        Livro livro = lerLivro(tipo);
        service.inserir(livro);
        IO.println("Livro cadastrado com sucesso!");
    }

    private Livro lerLivro(int tipo) {
        String titulo = IO.readln("Digite o título do livro: ");
        String autor = IO.readln("Digite o autor do livro: ");
        int ano = Integer.parseInt(IO.readln("Digite o ano de publicação: "));

        if (tipo == 1) {
            int exemplares = Integer.parseInt(IO.readln("Digite o número de exemplares: "));
            String localizacao = IO.readln("Digite a localização: ");
            return new LivroFisico(titulo, autor, ano, exemplares, localizacao);
        } else if (tipo == 2) {
            String formato = IO.readln("Digite o formato do arquivo: ");
            double tamanho = Double.parseDouble(IO.readln("Digite o tamanho do arquivo em MB: "));
            return new LivroDigital(titulo, autor, ano, formato, tamanho);
        } else {
            int duracao = Integer.parseInt(IO.readln("Digite a duração em minutos: "));
            String narrador = IO.readln("Digite o nome do narrador: ");
            return new AudioLivro(titulo, autor, ano, duracao, narrador);
        }
    }

    private void pesquisarPorTitulo() {
        String pesquisa = IO.readln("Informe o título ou parte dele: ");
        mostrarResultados(service.findByTitulo(pesquisa));
    }

    private void pesquisarPorAutor() {
        String pesquisa = IO.readln("Informe o autor ou parte dele: ");
        mostrarResultados(service.findByAutor(pesquisa));
    }

    private void mostrarResultados(List<Livro> livros) {
        if (livros.isEmpty()) {
            IO.println("Nenhum livro encontrado para essa pesquisa.");
        } else {
            for (Livro livro : livros) {
                IO.println(livro);
                IO.println("Tipo: " + livro.getTipo());
            }
        }
    }

    private boolean listarLivrosCadastrados() {
        List<Livro> livros = service.findAll();
        if (livros.isEmpty()) {
            IO.println("Não existem livros cadastrados.");
            return false;
        }

        IO.println("===== Livros cadastrados =====");
        for (int i = 0; i < livros.size(); i++) {
            IO.println((i + 1) + " - " + livros.get(i).getTitulo());
        }
        return true;
    }

    private void alterar() {
        if (!listarLivrosCadastrados())
            return;

        int indice = Integer.parseInt(IO.readln("Informe o índice do livro que deseja alterar: "));
        Livro atual = service.buscarPorIndice(indice);
        IO.println("Dados atuais: " + atual);
        IO.println("Tipo: " + atual.getTipo());
        IO.println("Digite os novos dados do livro:");

        int tipo;
        if (atual instanceof LivroFisico)
            tipo = 1;
        else if (atual instanceof LivroDigital)
            tipo = 2;
        else
            tipo = 3;

        Livro alterado = lerLivro(tipo);
        service.alterar(indice, alterado);
        IO.println("Livro alterado com sucesso!");
    }

    private void excluir() {
        if (!listarLivrosCadastrados())
            return;

        int indice = Integer.parseInt(IO.readln("Informe o índice do livro que deseja excluir: "));
        Livro livro = service.buscarPorIndice(indice);
        String resposta = IO.readln("Deseja realmente excluir '" + livro.getTitulo() + "'? (S/N): ");

        if (resposta != null && resposta.trim().equalsIgnoreCase("S")) {
            service.excluir(indice);
            IO.println("Livro excluído com sucesso!");
        } else {
            IO.println("Exclusão cancelada.");
        }
    }
}
