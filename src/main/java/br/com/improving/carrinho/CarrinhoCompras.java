package br.com.improving.carrinho;


import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Classe que representa o carrinho de compras de um cliente.
 */
public class CarrinhoCompras
{
	Set<Item> conjuntoItens = new LinkedHashSet<>();

    /**
     * Permite a adição de um novo item no carrinho de compras.
     *
     * Caso o item já exista no carrinho para este mesmo produto, as seguintes regras deverão ser seguidas:
     * - A quantidade do item deverá ser a soma da quantidade atual com a quantidade passada como parâmetro.
     * - Se o valor unitário informado for diferente do valor unitário atual do item, o novo valor unitário do item deverá ser
     * o passado como parâmetro.
     *
     * Devem ser lançadas subclasses de RuntimeException caso não seja possível adicionar o item ao carrinho de compras.
     *
     * @param produto
     * @param valorUnitario
     * @param quantidade
     */
    public void adicionarItem(Produto produto, BigDecimal valorUnitario, int quantidade)
	{
		Item	itemAtual;
		Item	itemAAdicionar;

		try
		{
			itemAtual = obterItemPorProduto(produto);
			checarSeItemPossuiValoresMenoresQueZero(valorUnitario, quantidade);
			if (Objects.nonNull(itemAtual))
			{
				itemAtual.setQuantidade(quantidade + itemAtual.getQuantidade());
				itemAtual.setValorUnitario(valorUnitario);
				return;
			}
			itemAAdicionar = new Item(produto, valorUnitario, quantidade);
			conjuntoItens.add(itemAAdicionar);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Algo deu errado ao adicionar item: "
					+ e.getMessage());
		}
    }

	/**
     * Permite a remoção do item que representa este produto do carrinho de compras.
     *
     * @param produto
     * @return Retorna um boolean, tendo o valor true caso o produto exista no carrinho de compras e false
     * caso o produto não exista no carrinho.
     */
    public boolean removerItem(Produto produto)
	{
		Item	itemAtual;

		itemAtual = obterItemPorProduto(produto);
		if (Objects.nonNull(itemAtual))
		{
			conjuntoItens.remove(itemAtual);
			return true;
		}
		return false;
    }

    /**
     * Permite a remoção do item de acordo com a posição.
     * Essa posição deve ser determinada pela ordem de inclusão do produto na
     * coleção, em que zero representa o primeiro item.
     *
     * @param posicaoItem
     * @return Retorna um boolean, tendo o valor true caso o produto exista no carrinho de compras e false
     * caso o produto não exista no carrinho.
     */
    public boolean removerItem(int posicaoItem)
	{
		Iterator<Item>	iteradorConjuntoItens;
		int				indiceAtual;
		Item			itemAtual;

		iteradorConjuntoItens = obterIteradorConjuntoItens();
		indiceAtual = 0;
		while (iteradorConjuntoItens.hasNext())
		{
			itemAtual = iteradorConjuntoItens.next();
			if (indiceAtual == posicaoItem)
			{
				conjuntoItens.remove(itemAtual);
				return true;
			}
			indiceAtual++;
		}
		return false;
    }

    /**
     * Retorna o valor total do carrinho de compras, que deve ser a soma dos valores totais
     * de todos os itens que compõem o carrinho.
     *
     * @return BigDecimal
     */
    public BigDecimal getValorTotal()
	{
		BigDecimal		total;
		Iterator<Item>	iteradorConjuntoItens;
		Item			itemAtual;

		iteradorConjuntoItens = obterIteradorConjuntoItens();
		total = new BigDecimal(0);
		while (iteradorConjuntoItens.hasNext())
		{
			itemAtual = iteradorConjuntoItens.next();
			total = total.add(itemAtual.getValorTotal());
		}
		return total;
    }

    /**
     * Retorna a lista de itens do carrinho de compras.
     *
     * @return itens
     */
    public Collection<Item> getItens()
	{
		return conjuntoItens;
    }

	public void adicionarItensAoCarrinho(Item[] itens)
	{
		for (int i = 0; i < itens.length; i++)
		{
			this.adicionarItem(
					itens[i].getProduto(),
					itens[i].getValorUnitario(),
					itens[i].getQuantidade()
			);
		}
	}

	private Iterator<Item> obterIteradorConjuntoItens()
	{
		return conjuntoItens.iterator();
	}

	private Item obterItemPorProduto(Produto produto)
	{
		Iterator<Item>	iteradorConjuntoItens;
		Item 			itemAtual;

		iteradorConjuntoItens = obterIteradorConjuntoItens();
		while (iteradorConjuntoItens.hasNext())
		{
			itemAtual = iteradorConjuntoItens.next();
			if (itemAtual.getProduto().equals(produto))
				return itemAtual;
		}
		return null;
	}

	private void checarSeItemPossuiValoresMenoresQueZero(BigDecimal valorUnitario, int quantidade)
	{
		if (valorUnitario.compareTo(BigDecimal.valueOf(0)) < 0)
			throw new RuntimeException("Valor unitario menor que zero.");
		if (quantidade < 0)
			throw new RuntimeException("Quantidade menor que zero.");
	}
}