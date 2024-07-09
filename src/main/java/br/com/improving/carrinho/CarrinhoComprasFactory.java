package br.com.improving.carrinho;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import javax.swing.text.html.Option;

/**
 * Classe responsável pela criação e recuperação dos carrinhos de compras.
 * 
 * As instâncias de CarrinhoComprasFactory são independentes entre si, ou seja, quando um carrinho 
 * para um cliente é criado em uma instância, a outra pode criar um novo carrinho para o mesmo 
 * cliente. Isso é verdade para todos os métodos.
 */
public class CarrinhoComprasFactory {

	Map<String,CarrinhoCompras> conjuntoCarrinhoCompras;

	public CarrinhoComprasFactory()
	{
		conjuntoCarrinhoCompras = new LinkedHashMap<>();
	}

    /**
     * Cria e retorna um novo carrinho de compras para o cliente passado como parâmetro.
     *
     * Caso já exista um carrinho de compras para o cliente passado como parâmetro, este carrinho deverá ser retornado.
     *
     * @param identificacaoCliente
     * @return CarrinhoCompras
     */
    public CarrinhoCompras criar(String identificacaoCliente)
	{
//		Optional<CarrinhoCompras> carrinhoCompras;
		try
		{

			return buscarCarrinhoPorIdentificacao(identificacaoCliente);
////		carrinhoCompras = conjuntoCarrinhoCompras.get(identificacaoCliente);
//		if (carrinhoCompras == null)
//		{
//			carrinhoCompras = gerarCarrinhoEAdicionarAoConjunto(identificacaoCliente);
//		}
//		return carrinhoCompras;
		}
		catch (NoSuchElementException e)
		{
			return gerarCarrinhoEAdicionarAoConjunto(identificacaoCliente);
		}
	}

	/**
     * Retorna o valor do ticket médio no momento da chamada ao método.
     * O valor do ticket médio é a soma do valor total de todos os carrinhos de compra dividido
     * pela quantidade de carrinhos de compra.
     * O valor retornado deverá ser arredondado com duas casas decimais, seguindo a regra:
     * 0-4 deve ser arredondado para baixo e 5-9 deve ser arredondado para cima.
     *
     * @return BigDecimal
     */
    public BigDecimal getValorTicketMedio()
	{
		return obterMediaTickets();
    }

    /**
     * Invalida um carrinho de compras quando o cliente faz um checkout ou sua sessão expirar.
     * Deve ser efetuada a remoção do carrinho do cliente passado como parâmetro da listagem de carrinhos de compras.
     *
     * @param identificacaoCliente
     * @return Retorna um boolean, tendo o valor true caso o cliente passado como parämetro tenha um carrinho de compras e
     * e false caso o cliente não possua um carrinho.
     */
    public boolean invalidar(String identificacaoCliente)
	{
		try
		{
			buscarCarrinhoPorIdentificacao(identificacaoCliente);
			conjuntoCarrinhoCompras.remove(identificacaoCliente);
			return true;
		}
		catch (NoSuchElementException e)
		{
			return false;
		}
	}

	private CarrinhoCompras gerarCarrinhoEAdicionarAoConjunto(String identificacaoCliente)
	{
		CarrinhoCompras carrinhoCompras;

		carrinhoCompras = new CarrinhoCompras();
		conjuntoCarrinhoCompras.put(identificacaoCliente, carrinhoCompras);

		return carrinhoCompras;
	};

	private Iterator obterIteradorCarrinhos()
	{
		return conjuntoCarrinhoCompras.entrySet().iterator();
	}

	private BigDecimal obterMediaTickets()
	{
		BigDecimal	valorTotalDeTodosOsCarrinhosDoConjunto;
		int			tamanhoDoConjunto;
		BigDecimal 	media;
		Iterator	iterator;

		valorTotalDeTodosOsCarrinhosDoConjunto = new BigDecimal(0);
		tamanhoDoConjunto = 0;
		iterator = obterIteradorCarrinhos();
		while(iterator.hasNext())
		{
			Entry<String, CarrinhoCompras> registroAtual = (Entry<String, CarrinhoCompras>) iterator.next();
			CarrinhoCompras carrinhoAtual = registroAtual.getValue();
			valorTotalDeTodosOsCarrinhosDoConjunto = valorTotalDeTodosOsCarrinhosDoConjunto.add(carrinhoAtual.getValorTotal());
			tamanhoDoConjunto++;
		}
		media = valorTotalDeTodosOsCarrinhosDoConjunto.divide(
				new BigDecimal(tamanhoDoConjunto),
				2,
				RoundingMode.HALF_UP);
		return media;
	}

	private CarrinhoCompras buscarCarrinhoPorIdentificacao(String identificacaoCliente)
	{
		CarrinhoCompras	carrinhoCompras;

		carrinhoCompras = conjuntoCarrinhoCompras.get(identificacaoCliente);
		if (carrinhoCompras != null)
			return carrinhoCompras;
		throw new NoSuchElementException();
	}


//	private void arredondarValor(BigDecimal valorAArredondar)
//	{
//		DecimalFormat	df;
//
//		df = new DecimalFormat();
//		df.setRoundingMode(RoundingMode.HALF_UP);
//		df.setMaximumFractionDigits(2);
//		df.setMinimumFractionDigits(2);
//		df.format(valorAArredondar);
//	}
}
