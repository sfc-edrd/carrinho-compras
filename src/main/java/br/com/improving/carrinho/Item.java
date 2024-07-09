package br.com.improving.carrinho;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Classe que representa um item no carrinho de compras.
 */
public class Item
{
    private Produto produto;
    private BigDecimal valorUnitario;
    private int quantidade;

    /**
     * Construtor da classe Item.
     * 
     * @param produto
     * @param valorUnitario
     * @param quantidade
     */
    public Item(Produto produto, BigDecimal valorUnitario, int quantidade)
	{
		this.produto = produto;
		setValorUnitario(valorUnitario);
		setQuantidade(quantidade);
    }

    /**
     * Retorna o produto.
     *
     * @return Produto
     */
    public Produto getProduto()
	{
		return produto;
    }

    /**
     * Retorna o valor unitário do item.
     *
     * @return BigDecimal
     */
    public BigDecimal getValorUnitario()
	{
		return  valorUnitario;
    }

    /**
     * Retorna a quantidade dos item.
     *
     * @return int
     */
    public int getQuantidade()
	{
		return quantidade;
    }

    /**
     * Retorna o valor total do item.
     *
     * @return BigDecimal
     */
    public BigDecimal getValorTotal()
	{
		return getValorUnitario().multiply(BigDecimal.valueOf(getQuantidade()));
    }

	/**
	 * Define o valorUnitario do Item.
	 *
	 * @param valorUnitario
	 */
	public void setValorUnitario(BigDecimal valorUnitario)
	{
		this.valorUnitario = valorUnitario;
	}

	/**
	 * Define a quantidade do Item.
	 * @param quantidade
	 */
	public void setQuantidade(int quantidade)
	{
		this.quantidade = quantidade;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final Item item = (Item) o;

		if (quantidade != item.quantidade) {
			return false;
		}
		if (!Objects.equals(produto, item.produto)) {
			return false;
		}
		return Objects.equals(valorUnitario, item.valorUnitario);
	}

	@Override
	public int hashCode() {
		int result = produto != null ? produto.hashCode() : 0;
		result = 31 * result + (valorUnitario != null ? valorUnitario.hashCode() : 0);
		result = 31 * result + quantidade;
		return result;
	}

	@Override
	public String toString()
	{
		return "Item{" +
				"produto=" + produto +
				", valorUnitario=" + valorUnitario +
				", quantidade=" + quantidade +
				'}';
	}
}
