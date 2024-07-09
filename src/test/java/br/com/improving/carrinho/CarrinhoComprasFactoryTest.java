package br.com.improving.carrinho;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

class CarrinhoComprasFactoryTest
{

	CarrinhoComprasFactory 	factory;

	public CarrinhoComprasFactoryTest()
	{
		this.factory = new CarrinhoComprasFactory();
	}

	@Test
	@Order(1)
	@DisplayName("Cria e retorna um novo carrinho de compras para o cliente passado como parâmetro.")
	void criarNovoCarrinho()
	{
		String	identificacaoCarrinho;

		identificacaoCarrinho = UUID.randomUUID().toString();
		factory.criar(identificacaoCarrinho);
		Assertions.assertTrue(factory.conjuntoCarrinhoCompras.containsKey(identificacaoCarrinho));
	}

	@Test
	@Order(2)
	@DisplayName("Caso já exista um carrinho de compras para o cliente passado como parâmetro, este carrinho deverá ser retornado.")
	void criarNovoCarrinhoETentarCriarNovamenteComOMesmoId()
	{
		String			identificacaoCarrinho;
		CarrinhoCompras	carrinhoComprasCriado;
		CarrinhoCompras carrinhoComprasCriadoNovamenteComOMesmoId;

		identificacaoCarrinho = UUID.randomUUID().toString();
		carrinhoComprasCriado = factory.criar(identificacaoCarrinho);
		carrinhoComprasCriadoNovamenteComOMesmoId = factory.criar(identificacaoCarrinho);

		Assertions.assertTrue(carrinhoComprasCriado == carrinhoComprasCriadoNovamenteComOMesmoId);
	}

	/**
	 *      * Retorna o valor do ticket médio no momento da chamada ao método.
	 *      * O valor do ticket médio é a soma do valor total de todos os carrinhos de compra dividido
	 *      * pela quantidade de carrinhos de compra.
	 *      * O valor retornado deverá ser arredondado com duas casas decimais, seguindo a regra:
	 *      * 0-4 deve ser arredondado para baixo e 5-9 deve ser arredondado para cima.
	 */
	@Test
	@Order(3)
	@DisplayName("Testar se o ticket medio traz o formato esperado e arredondamento para baixo")
	void getValorTicketMedioEArredondamentoParaBaixo()
	{
		CarrinhoCompras	carrinhoComprasCriado1;
		CarrinhoCompras	carrinhoComprasCriado2;
		CarrinhoCompras	carrinhoComprasCriado3;
		Produto[]		produtos;
		Item[]			items;
		Item 			itemAtual;
		BigDecimal		somaValorTicket;
		BigDecimal		mediaValorTicket;

		carrinhoComprasCriado1 = factory.criar(UUID.randomUUID().toString());
		carrinhoComprasCriado2 = factory.criar(UUID.randomUUID().toString());
		carrinhoComprasCriado3 = factory.criar(UUID.randomUUID().toString());
		produtos = CarrinhoComprasTest.geradorDeProdutos(3);
		items = CarrinhoComprasTest.geradorDeItems(produtos);
		somaValorTicket = BigDecimal.ZERO;
			carrinhoComprasCriado1.adicionarItensAoCarrinho(items);
			carrinhoComprasCriado2.adicionarItensAoCarrinho(items);
		for (int i = 0; i < items.length; i++)
		{
			itemAtual = items[i];
			somaValorTicket = somaValorTicket.add(itemAtual.getValorTotal().multiply(new BigDecimal(2)));
		}
		mediaValorTicket = somaValorTicket.divide(
				BigDecimal.valueOf(factory.conjuntoCarrinhoCompras.size()),
				2,
				RoundingMode.HALF_UP);
		Assertions.assertEquals(factory.getValorTicketMedio(), mediaValorTicket);
	}

	/**
	 * Invalida um carrinho de compras quando o cliente faz um checkout ou sua sessão expirar.
	 * Deve ser efetuada a remoção do carrinho do cliente passado como parâmetro da listagem de carrinhos de compras.
	 */
	@Test
	@Order(4)
	@DisplayName("Testar a remoçao do carrinho do cliente")
	void invalidar()
	{
		String			identificaçãoCarrinho;
		CarrinhoCompras	carrinhoComprasCriado1;

		identificaçãoCarrinho = UUID.randomUUID().toString();
		carrinhoComprasCriado1 = factory.criar(identificaçãoCarrinho);
		factory.invalidar(identificaçãoCarrinho);
		Assertions.assertEquals(factory.conjuntoCarrinhoCompras.size(), 0);
	}
}