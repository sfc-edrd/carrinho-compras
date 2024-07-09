package br.com.improving.carrinho;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import javax.management.AttributeNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


@TestMethodOrder(OrderAnnotation.class)
class CarrinhoComprasTest
{

	/**
	 * Testes de adição / localização / remocao de novo(s) item(ns) no carrinho de compras.
	 */

	CarrinhoComprasFactory	factory;
	CarrinhoCompras			carrinhoCompras;


	public CarrinhoComprasTest()
	{
		factory = new CarrinhoComprasFactory();
		this.carrinhoCompras = factory.criar("0001");
	}

	/**
	 * Cenario 1: Testa a criação de novo(s) item(ns) com carrinho vazio
	 *
	 * Validação:
	 * 	Compara os elementos do array de itens adiocionados com o dos existentes no carrinho
	 */
	@Test
	@Order(1)
	@DisplayName("CENARIO #1: Testa a criação de novo(s) item(ns) com carrinho vazio")
	void adicionarItens()
	{
		int		numeroDeItens;
		Item[]	itensGeradosEAdicionados;

		numeroDeItens = 3;
		itensGeradosEAdicionados = gerarItensEAdicionarAoCarrinho(numeroDeItens);
		Assertions.assertArrayEquals(
				carrinhoCompras.getItens().toArray(),
				itensGeradosEAdicionados);
	}


	/**
	 * Cenario 2: Testa a criação com item já existente no carrinho para este mesmo produto se retorna incremento na quantidade
	 *
	 * Caso o item já exista no carrinho para este mesmo produto, as seguintes regras deverão ser seguidas:
	 * - A quantidade do item deverá ser a soma da quantidade atual com a quantidade passada como parâmetro.
	 *
	 * Validação:
	 * 	Compara os elementos do array de quantidades de itens para um no carrinho com o array de quantidades de itens adicionados
	 */
	@Test
	@Order(2)
	@DisplayName("Testa a criação com item já existente no carrinho para este mesmo produto se retorna incremento na quantidade")
	void adicionarItemComProdutoJaExistente()
	{
		Item[] 		itensAAdicionar;
		Object[] 	itensExistentes;
		int[] 		quantidadesItensExistentes;
		int 		numeroDeItens;

		itensAAdicionar = gerarItensEAdicionarAoCarrinho(3);
		incrementarPropriedadeOperacaoMatematica(itensAAdicionar, "quantidade", 100,'*', Integer.class);
		carrinhoCompras.adicionarItensAoCarrinho(itensAAdicionar);
		itensExistentes = carrinhoCompras.getItens().toArray();
		numeroDeItens = itensExistentes.length;
		quantidadesItensExistentes = new int[numeroDeItens];
		for (int i = 0; i < numeroDeItens; i++)
			quantidadesItensExistentes[i] = ((Item) itensExistentes[i]).getQuantidade();
		Assertions
				.assertArrayEquals(quantidadesItensExistentes, new int[]{101, 202, 303});
	}

	/**
	 * Cenario 3: Testa a criação com item já existente no carrinho para este mesmo produto com valor unitario
	 * diferente e avaliar se retorna incremento na quantidade e alteração no valor.
	 *
	 * Caso o item já exista no carrinho para este mesmo produto, as seguintes regras deverão ser seguidas:
	 * - Se o valor unitário informado for diferente do valor unitário atual do item, o novo valor unitário do item deverá ser
	 * o passado como parâmetro.
	 *
	 * Validação:
	 * 	Compara os elementos do array de valores de itens para um no carrinho com o array de valores de itens adicionados
	 */
	@Test
	@Order(3)
	@DisplayName("Testa a criação com item já existente no carrinho para este mesmo produto com valor unitario diferente e avaliar se retorna incremento na quantidade e alteração no valor")
	void adicionarItemComProdutoJaExistenteENovoValor()
	{
		Item[] 			itensAAdicionar;
		Object[] 		itensExistentes;
		BigDecimal[]	valoresItensExistentes;
		int 			numeroDeItens;

		itensAAdicionar = gerarItensEAdicionarAoCarrinho(3);
		incrementarPropriedadeOperacaoMatematica(itensAAdicionar, "valorunitario", 1000, '+', BigDecimal.class);
		carrinhoCompras.adicionarItensAoCarrinho(itensAAdicionar);
		itensExistentes = carrinhoCompras.getItens().toArray();
		numeroDeItens = itensExistentes.length;
		valoresItensExistentes = new BigDecimal[numeroDeItens];
		for (int i = 0; i < numeroDeItens; i++)
			valoresItensExistentes[i] = ((Item) itensExistentes[i]).getValorUnitario();

		Assertions
				.assertArrayEquals(
						valoresItensExistentes,
						new BigDecimal[]{new BigDecimal(1010),new BigDecimal(1020),new BigDecimal(1030)});
	}

	/**
	 * Cenario 4: Testa a criação do item no carrinho de compras e o lançamento de subclasses de RuntimeException caso não seja possível adicionar o item ao carrinho de compras
	 *
	 * Devem ser lançadas subclasses de RuntimeException caso não seja possível adicionar o item ao carrinho de compras.
	 *
	 * Validação:
	 * 	Compara se a exceção lançada é a IllegalAgumentException ao tentar criar elemnto com quantidade inferior a zero.
	 */
	@Test
	@Order(4)
	@DisplayName("Testa a criação do item no carrinho de compras e o lançamento de subclasses de RuntimeException caso não seja possível adicionar o item ao carrinho de compras")
	void adicionarItemComRetornoDeExcecao()
	{
		Item[]	items;

		items = geradorDeItems(geradorDeProdutos(1));
		items[0].setQuantidade(-1);
		Assertions
				.assertThrows(IllegalArgumentException.class,
						() -> carrinhoCompras.adicionarItem(
								items[0].getProduto(),
								items[0].getValorUnitario(),
								items[0].getQuantidade()));
	}

	/**
	 * Cenario 5: Testa a remoção do item no carrinho de compras produto
	 *
	 * Validação:
	 * 	Compara se quantidade de itens no carrinho é iferior a de itens adicionados anteriormente.
	 */
	@Test
	@Order(5)
	@DisplayName("Testa a remoção do item no carrinho de compras por produto")
	void removerItem()
	{
		Item[] 		itensAAdicionar;

		itensAAdicionar = gerarItensEAdicionarAoCarrinho(3);
		carrinhoCompras.removerItem(itensAAdicionar[0].getProduto());
		Assertions
				.assertTrue(
						itensAAdicionar.length > carrinhoCompras.getItens().size());
	}

	/**
	 * Cenario 6: Testa a remoção do item no carrinho de compras por posicao do item
	 *
	 * Validação:
	 * 	Compara apos a remocao do item na posicao 1 se os items no carrinho correspondem aos itens
	 * 	no inserios anteriormente.
	 */
	@Test
	@Order(6)
	@DisplayName("Testa a remoção do item no carrinho de compras por posicao do item")
	void testRemoverItem()
	{
		Item[] 		itensAAdicionar;

		itensAAdicionar = gerarItensEAdicionarAoCarrinho(3);
		carrinhoCompras.removerItem(1);
		Assertions
				.assertTrue(itensAAdicionar[0].equals(carrinhoCompras.getItens().toArray()[0])
						&& itensAAdicionar[2].equals(carrinhoCompras.getItens().toArray()[1]));
	}

	/**
	 * Cenario 7: Testa obtenção do valor total do carrinho
	 *
	 * Validação:
	 * 	Compara o valor dos itens presentes no carrinho com o valor obtido com a função getValorTotal().
	 */
	@Test
	@Order(7)
	@DisplayName("Testa obtencao do valor total do carrinho")
	void getValorTotal()
	{
		BigDecimal	valorTotalDoCarrinho;

		gerarItensEAdicionarAoCarrinho(3);
		valorTotalDoCarrinho = carrinhoCompras.getValorTotal();
		Assertions
				.assertTrue(
						valorTotalDoCarrinho
								.equals(BigDecimal.valueOf(10 + 40 + 90)));
	}

	/**
	 * Cenario 8: Testa obtenção dos itens do carrinho
	 *
	 * Validação:
	 * 	Compara os items gerados e inseridos ao carrinho com os obtidoes com o metodo getItens().
	 */
	@Test
	@Order(8)
	@DisplayName("Testa obtencao do valor total do carrinho")
	void getItens()
	{
		Item[]	itensAdicionados;

		itensAdicionados = gerarItensEAdicionarAoCarrinho(3);
		Assertions
				.assertArrayEquals(
						itensAdicionados,
						carrinhoCompras.getItens().toArray());
	}

	/**
	 * Função para gerar novos produtos com valores padrão.
	 * ex:
	 * 	{ codigo: 1, descrição: "Produto-1" }
	 * 	{ codigo: 2, descrição: "Produto-2" }
	 * 	{ codigo: 3, descrição: "Produto-3" }
	 *	...
	 *
	 * @param numeroDeProdutos Numero de produtos a ser criado e retornada.
	 * @return Array de Produto contendo os produtos gerados
	 */
	public static Produto[] geradorDeProdutos(int numeroDeProdutos)
	{
		Produto[] 	arrayDeProdutos;
		Produto 	novoProduto;
		int 		numeroDeIdentificacao;

		arrayDeProdutos = new Produto[numeroDeProdutos];
		for (int i = 0; i < numeroDeProdutos; i++)
		{
			numeroDeIdentificacao = i + 1;
			novoProduto = new Produto((long) numeroDeIdentificacao, "Produto-" + numeroDeIdentificacao);
			arrayDeProdutos[i] = novoProduto;
		}
		return arrayDeProdutos;
	}

	/**
	 /**
	 * Função para gerar array de itens com os produtos passados como parametro.
	 * ex:
	 * Item[] {
	 *  	{
	 *  		produto: { codigo: 1, descrição: "Produto-1" },
	 *  		valorUnitario: 10,
	 *  		quantidade: 1
	 * 		},
	 * 		{
	 * 	    	produto: { codigo: 2, descrição: "Produto-2" },
	 * 			valorUnitario: 20,
	 * 			quantidade: 2
	 * 		},
	 * 		{
	 *  		produto: { codigo: 2, descrição: "Produto-2" },
	 * 			valorUnitario: 20,
	 * 			quantidade: 2
	 * 		}
	 * };
	 *
	 * @param produtos Arrays de Produtos a ser usado parar criar Array de Item.
	 * @return Array de Item contendo os produtos passados como parametro.
	 *
	 */
	public static Item[] geradorDeItems(Produto[] produtos)
	{
		int 	quantidadeDeItens;
		Item[]	arrayDeItens;
		Produto	produtoAtual;
		Item	novoItem;

		quantidadeDeItens = produtos.length;
		arrayDeItens = new Item[quantidadeDeItens];
		for (int i = 0; i < quantidadeDeItens; i++)
		{
			produtoAtual = produtos[i];
			novoItem = new Item(
					produtoAtual,
					new BigDecimal(produtoAtual.getCodigo() * 10),
					produtoAtual.getCodigo().intValue());
			arrayDeItens[i] = novoItem;
		}
		return arrayDeItens;
	}

	private Item[] gerarItensEAdicionarAoCarrinho(
			int quantidadeDeItens)
	{
		Produto[]	produtos;
		Item[]		itensAAdicionar;

		produtos = geradorDeProdutos(quantidadeDeItens);
		itensAAdicionar = geradorDeItems(produtos);
		carrinhoCompras.adicionarItensAoCarrinho(itensAAdicionar);

		return itensAAdicionar;
	}

	private void incrementarPropriedadeOperacaoMatematica(
		Item[]	itensAAdicionar,
		String 	propriedade,
		int 	valorDaOperacao,
		char	sinalDaOperacao,
		Class	classeRetornadaNoMetodo)
	{
		int 		numeroDeItens;
		Object		respostaDoMetodo;
		BigDecimal	valorMultiplicado;

		try
		{
			numeroDeItens = itensAAdicionar.length;
			for (int i = 0; i < numeroDeItens; i++)
			{
				respostaDoMetodo = invocarOMetodoAposobterMetodoDaPropriedade(
						itensAAdicionar[i],
						propriedade,
						ETipoDeAcao.GET,
						null
				);
				valorMultiplicado = obterOValorAposOperacao(respostaDoMetodo, classeRetornadaNoMetodo, valorDaOperacao, sinalDaOperacao);
				invocarOMetodoAposobterMetodoDaPropriedade(itensAAdicionar[i], propriedade, ETipoDeAcao.SET, valorMultiplicado);
			}
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}

	private Object invocarOMetodoAposobterMetodoDaPropriedade(
			Item item,
			String propriedade,
			ETipoDeAcao verbo,
			BigDecimal arg)
	{
		Object respostaAposInvocacaoDoMetodo;
		Method metodo;

		propriedade = ETipoDeAcao.obterValor(verbo) + propriedade;
		try
		{
			metodo = obterNomeDoMetodo(item, propriedade);
			respostaAposInvocacaoDoMetodo = invocarOMetodo(metodo, item, arg);
			return respostaAposInvocacaoDoMetodo;
		}
		catch (AttributeNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}

	private Object invocarOMetodo(Method metodo, Item itensAAdicionar, BigDecimal args)
	{
		try
		{
			if (args == null)
				return metodo.invoke(itensAAdicionar);
			if (metodo.getReturnType().getName().equals("void") && metodo.getParameterTypes()[0].getName().contains("int"))
				return metodo.invoke(itensAAdicionar,args.intValue());
			return metodo.invoke(itensAAdicionar,args);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	private Method obterNomeDoMetodo(Item item, String propriedade)
			throws AttributeNotFoundException
	{
		Method[] 	metodosDeclarados;
		Method 		metodo;

		metodosDeclarados = item.getClass().getDeclaredMethods();
		for (int i = 0; i < metodosDeclarados.length; i++)
		{
			metodo = metodosDeclarados[i];
			if (metodo.getName().toLowerCase().contains(propriedade.toLowerCase()))
				return metodo;
		}
		throw new AttributeNotFoundException();
	}

	private BigDecimal obterOValorAposOperacao(Object respostaDoMetodo, Class classeRetornadaNoMetodo, int valorDaOperacao, char sinalDaOperacao)
	{
		BigDecimal numeroAposOperacao;

		numeroAposOperacao = new BigDecimal(0);
		if (respostaDoMetodo instanceof Integer)
		{
			Integer valor = (Integer) classeRetornadaNoMetodo.cast(respostaDoMetodo);
			numeroAposOperacao = numeroAposOperacao(valorDaOperacao, sinalDaOperacao, valor);
		}
		else if (respostaDoMetodo instanceof BigDecimal)
		{
			BigDecimal valor = (BigDecimal) classeRetornadaNoMetodo.cast(respostaDoMetodo);
			numeroAposOperacao = numeroAposOperacao(valorDaOperacao, sinalDaOperacao, valor);
		}
		return numeroAposOperacao;
	}

	private BigDecimal numeroAposOperacao(Integer valorDaOperacao, char sinalDaOperacao, Integer valorAtual)
	{
		switch (sinalDaOperacao)
		{
			case ('+'):
				return new BigDecimal(valorAtual + valorDaOperacao);
			case ('-'):
				return new BigDecimal(valorAtual - valorDaOperacao);
			case ('/'):
				return new BigDecimal(valorAtual / valorDaOperacao);
			case ('*'):
				return new BigDecimal(valorAtual * valorDaOperacao);
			default:
				return new BigDecimal(0);
		}
	}

	private BigDecimal numeroAposOperacao(Integer valorDaOperacao, char sinalDaOperacao, BigDecimal valorAtual)
	{
		BigDecimal	valorDaOperacaoBig;

		valorDaOperacaoBig = new BigDecimal(valorDaOperacao);
		switch (sinalDaOperacao)
		{
			case ('+'):
				return valorAtual.add(valorDaOperacaoBig);
			case ('-'):
				return valorAtual.subtract(valorDaOperacaoBig);
			case ('/'):
				return valorAtual.divide(valorDaOperacaoBig);
			case ('*'):
				return valorAtual.multiply(valorDaOperacaoBig);
			default:
				return new BigDecimal(0);
		}
	}
}

enum ETipoDeAcao
{
	GET,
	SET;

	public static String obterValor(ETipoDeAcao action)
	{
		return action.name().toLowerCase();
	}
}