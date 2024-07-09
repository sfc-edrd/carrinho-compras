package br.com.improving.carrinho;

/**
 * Classe que representa um produto que pode ser adicionado
 * como item ao carrinho de compras.
 *
 * Importante: Dois produtos são considerados iguais quando ambos possuem o
 * mesmo código.
 */
public class Produto
{

    private Long	codigo;
    private String	descricao;

    /**
     * Construtor da classe Produto.
     *
     * @param codigo
     * @param descricao
     */
    public Produto(Long codigo, String descricao)
	{
		this.codigo = codigo;
		this.descricao = descricao.trim();
    }

    /**
     * Retorna o código da produto.
     *
     * @return Long
     */
    public Long getCodigo()
	{
		return codigo;
    }

    /**
     * Retorna a descrição do produto.
     *
     * @return String
     */
    public String getDescricao()
	{
		return descricao;
    }

	@Override
	public boolean equals(Object o)
	{
		final Produto 	produto;

		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		produto = (Produto) o;

		return codigo.equals(produto.codigo);
	}

	@Override
	public int hashCode()
	{
		return codigo.hashCode();
	}
}