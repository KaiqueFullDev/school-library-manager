package br.edu.ifba.ed;

import java.util.NoSuchElementException;


public class ListaDinamica<T> implements Listavel<T> {
    private NoDuplo<T> ponteiroInicio;
    private NoDuplo<T> ponteiroFim;
    private int tamanhoMaximo;
    private int quantidade;

    public ListaDinamica(int tamanhoMaximo) {
        if (tamanhoMaximo <= 0) {
            throw new IllegalArgumentException("capacidade deve ser maior que zero.");
        }
        ponteiroInicio = null;
        ponteiroFim = null;
        this.tamanhoMaximo = tamanhoMaximo;
        quantidade = 0;
    }

    public ListaDinamica() {
        this(100000);
    }

    @Override
    public void  inserir(T objeto, int posicao) {
        if (estaCheia() || posicao < 0 || posicao > quantidade) {
            throw new IndexOutOfBoundsException("Posição inválida");
        }
        if (posicao == quantidade) {
            anexar(objeto);
            return;
        }
        NoDuplo<T> novo = new NoDuplo<>(objeto);
        if (posicao == 0) {
            if (ponteiroInicio == null) {
                ponteiroInicio = ponteiroFim = novo;
            } else {
                novo.setProximo(ponteiroInicio);
                ponteiroInicio.setAnterior(novo);
                ponteiroInicio = novo;
            }
        } else {
            NoDuplo<T> ponteiroAtual = noNaPosicao(posicao);
            NoDuplo<T> ponteiroAnterior = ponteiroAtual.getAnterior();
            ponteiroAnterior.setProximo(novo);
            novo.setAnterior(ponteiroAnterior);
            novo.setProximo(ponteiroAtual);
            ponteiroAtual.setAnterior(novo);
        }
        quantidade++;
    }

    @Override
    public void anexar(T objeto) {
        if (estaCheia()) {
            throw new NoSuchElementException("Lista cheia");
        }
        NoDuplo<T> novoNo = new NoDuplo<>(objeto);
        if (estaVazia()) {
            ponteiroInicio = ponteiroFim = novoNo;
        } else {
            ponteiroFim.setProximo(novoNo);
            novoNo.setAnterior(ponteiroFim);
            ponteiroFim = novoNo;
        }
        quantidade++;
    }

    @Override
    public T selecionar(int posicao) {
        if (posicao < 0 || posicao >= quantidade) {
            throw new IndexOutOfBoundsException("Posição inválida");
        }
        NoDuplo<T> ponteiroAuxiliar = ponteiroInicio;
        for (int i = 0; i < posicao; i++) {
            ponteiroAuxiliar = ponteiroAuxiliar.getProximo();
        }
        return ponteiroAuxiliar.getDado();
    }

    @Override
    public T[] selecionarTodos() {
        T[] arrayRetorno = (T[]) new Object[quantidade];

        NoDuplo<T> ponteiroAuxiliar = ponteiroInicio;

        for (int i = 0; i < quantidade; i++) {
            arrayRetorno[i] = ponteiroAuxiliar.getDado();
            ponteiroAuxiliar = ponteiroAuxiliar.getProximo();
        }

        return arrayRetorno;
    }

    @Override
    public void atualizar(T objeto, int posicao) {
        if (posicao < 0 || posicao >= quantidade) {
            throw new IndexOutOfBoundsException("Posição inválida");
        }
        NoDuplo<T> ponteiroAuxiliar = ponteiroInicio;
        for (int i = 0; i < posicao; i++) {
            ponteiroAuxiliar = ponteiroAuxiliar.getProximo();
        }
        ponteiroAuxiliar.setDado(objeto);
    }

    @Override
    public T apagar(int posicao) {
        if (posicao < 0 || posicao >= quantidade) {
            throw new IndexOutOfBoundsException("Posição inválida");
        }
        NoDuplo<T> ponteiroAuxiliar = ponteiroInicio;
        for (int i = 0; i < posicao; i++) {
            ponteiroAuxiliar = ponteiroAuxiliar.getProximo();
        }
        T retorno = ponteiroAuxiliar.getDado();
        if (quantidade == 1) {
            ponteiroInicio = ponteiroFim = null;
        } else if (ponteiroAuxiliar == ponteiroInicio) {
            ponteiroInicio = ponteiroInicio.getProximo();
            ponteiroInicio.setAnterior(null);
        } else if (ponteiroAuxiliar == ponteiroFim) {
            ponteiroFim = ponteiroFim.getAnterior();
            ponteiroFim.setProximo(null);
        } else {
            ponteiroAuxiliar.getAnterior().setProximo(ponteiroAuxiliar.getProximo());
            ponteiroAuxiliar.getProximo().setAnterior(ponteiroAuxiliar.getAnterior());
        }
        quantidade--;
        return retorno;
    }

    @Override
    public void limpar() {
        ponteiroInicio = ponteiroFim = null;
        quantidade = 0;
    }

    @Override
    public int tamanho() {
        return quantidade;
    }

    @Override
    public boolean estaVazia() {
        return quantidade == 0;
    }

    @Override
    public boolean estaCheia() {
        return quantidade == tamanhoMaximo;
    }

    @Override
    public String imprimir() {
        String resultado = "";
        NoDuplo<T> ponteiroAuxiliar = ponteiroInicio;
        for (int i = 0; i < quantidade; i++) {
            resultado += ponteiroAuxiliar.getDado();
            if (i != quantidade - 1) {
                resultado += ",";
            }
            ponteiroAuxiliar = ponteiroAuxiliar.getProximo();
        }
        return "[ " + resultado + " ]";
    }

    private NoDuplo<T> noNaPosicao(int posicao) {
        NoDuplo<T> p = ponteiroInicio;
        for (int i = 0; i < posicao; i++) {
            p = p.getProximo();
        }
        return p;
    }
}
