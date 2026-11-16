package br.edu.atitus.bibliodigital.services;

import br.edu.atitus.bibliodigital.entities.AudioLivro;
import br.edu.atitus.bibliodigital.entities.Livro;
import br.edu.atitus.bibliodigital.entities.LivroDigital;
import br.edu.atitus.bibliodigital.entities.LivroFisico;
import br.edu.atitus.bibliodigital.repositories.LivroRepository;

import java.util.List;

public class LivroService {
    private final LivroRepository repository = new LivroRepository();

    public void inserir(Livro livro) {
        validarLivro(livro, null);
        repository.inserir(livro);
    }

    public List<Livro> findAll() {
        return repository.findAll();
    }

    public List<Livro> findByTitulo(String parametro) {
        if (parametro == null || parametro.isBlank())
            throw new RuntimeException("Informe um título para pesquisar.");
        return repository.findByTitulo(parametro.trim());
    }

    public List<Livro> findByAutor(String parametro) {
        if (parametro == null || parametro.isBlank())
            throw new RuntimeException("Informe um autor para pesquisar.");
        return repository.findByAutor(parametro.trim());
    }

    public Livro buscarPorIndice(int indice) {
        if (repository.findAll().isEmpty())
            throw new RuntimeException("Não existem livros cadastrados.");
        if (indice < 1 || indice > repository.findAll().size())
            throw new RuntimeException("Índice de livro inválido.");
        return repository.buscarPorIndice(indice - 1);
    }

    public void alterar(int indice, Livro livro) {
        Livro livroAtual = buscarPorIndice(indice);
        validarLivro(livro, livroAtual);
        repository.alterar(indice - 1, livro);
    }

    public void excluir(int indice) {
        buscarPorIndice(indice);
        repository.excluir(indice - 1);
    }

    private void validarLivro(Livro livro, Livro livroAtual) {
        if (livro == null)
            throw new RuntimeException("Livro inválido.");
        if (livro.getTitulo() == null || livro.getTitulo().isBlank())
            throw new RuntimeException("O título não pode ficar vazio.");
        if (livro.getAutor() == null || livro.getAutor().isBlank())
            throw new RuntimeException("O autor não pode ficar vazio.");
        if (livro.getAnoPublicacao() < 1900)
            throw new RuntimeException("O ano de publicação deve ser igual ou superior a 1900.");

        livro.setTitulo(livro.getTitulo().trim());
        livro.setAutor(livro.getAutor().trim());

        for (Livro cadastrado : repository.findAll()) {
            if (cadastrado != livroAtual && cadastrado.getTitulo().equalsIgnoreCase(livro.getTitulo()))
                throw new RuntimeException("Já existe um livro cadastrado com esse título.");
        }

        if (livro instanceof LivroFisico) {
            LivroFisico fisico = (LivroFisico) livro;
            if (fisico.getLocalizacao() != null)
                fisico.setLocalizacao(fisico.getLocalizacao().trim());
        } else if (livro instanceof LivroDigital) {
            LivroDigital digital = (LivroDigital) livro;
            if (digital.getFormato() != null)
                digital.setFormato(digital.getFormato().trim());
        } else if (livro instanceof AudioLivro) {
            AudioLivro audio = (AudioLivro) livro;
            if (audio.getNarrador() != null)
                audio.setNarrador(audio.getNarrador().trim());
        }
    }
}
