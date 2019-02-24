package com.saba.cursomc.services;

import java.util.List;
import java.util.Optional;

import com.saba.cursomc.domain.Categoria;
import com.saba.cursomc.dto.ClienteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.saba.cursomc.domain.Cliente;
import com.saba.cursomc.repository.ClienteRepository;
import com.saba.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	public Cliente find(Integer id) {
		
		Optional<Cliente> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + " Tipo: " + Cliente.class.getName()));
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	public Cliente insert(Cliente cliente){
		cliente.setId(null);
		return repo.save(cliente);
	}

	public Cliente update(ClienteDTO clienteDTO){

		Cliente cliente = find(clienteDTO.getId());
		cliente.setNome(clienteDTO.getNome());
		cliente.setEmail(clienteDTO.getEmail());
		return repo.save(cliente);
	}

	public List<Cliente> getAll(){
		return repo.findAll();
	}

	public void delete(Integer id){

		repo.deleteById(id);
	}

	public Cliente fromDto(ClienteDTO clienteDTO){

		Cliente cliente = new Cliente();

		cliente.setId(clienteDTO.getId());
		cliente.setNome(clienteDTO.getNome());
		cliente.setEmail(clienteDTO.getEmail());

		return cliente;
	}
}
