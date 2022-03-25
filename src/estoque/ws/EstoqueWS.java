package estoque.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import estoque.modelo.item.Filtro;
import estoque.modelo.item.Filtros;
import estoque.modelo.item.Item;
import estoque.modelo.item.ItemDao;
import estoque.modelo.item.ItemValidador;
import estoque.modelo.item.ListaItens;
import estoque.modelo.usuario.AutorizacaoException;
import estoque.modelo.usuario.TokenDao;
import estoque.modelo.usuario.TokenUsuario;

@WebService
public class EstoqueWS {
	
	private ItemDao dao = new ItemDao();
	
	@WebMethod(operationName = "TodosOsItens")
	@WebResult(name = "items")
	public ListaItens getItems(@WebParam(name="filtros") Filtros filtros){
		List<Filtro> listaFiltros = filtros.getLista();
		
		List<Item> todosItens = dao.todosItens(listaFiltros);
		return new ListaItens(todosItens);
	}
	
	@WebMethod(operationName = "CadastrarItem")
	@WebResult(name = "item")
	public Item cadastrarItem(
			@WebParam(name = "tokenUsuario", header = true) TokenUsuario token,
			@WebParam(name="item") @XmlElement(required = true) Item item)
			throws AutorizacaoException {
		
		TokenDao tokenDao = new TokenDao();
		boolean ehValido = tokenDao.ehValido(token);
		
		
		
		if (!ehValido) {
			throw new AutorizacaoException("Autozicacao falhou");
		}
		
		new ItemValidador(item).validate();
		
		this.dao.cadastrar(item);
		return item;
		
	}

}
