package com.saba.cursomc.services;

import java.util.List;
import java.util.Optional;

import com.saba.cursomc.domain.Categoria;
import com.saba.cursomc.domain.Cidade;
import com.saba.cursomc.domain.Endereco;
import com.saba.cursomc.domain.enums.TipoCliente;
import com.saba.cursomc.dto.ClienteDTO;
import com.saba.cursomc.dto.ClienteNewDTO;
import com.saba.cursomc.repository.CidadeRepository;
import com.saba.cursomc.repository.EnderecoRepository;
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

	@Autowired
	private CidadeRepository cidadeRepo;

	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public Cliente find(Integer id) {
		
		Optional<Cliente> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + " Tipo: " + Cliente.class.getName()));
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	public Cliente insert(Cliente cliente){
		cliente.setId(null);
        repo.save(cliente);
		enderecoRepository.saveAll(cliente.getEnderecos());
		return cliente;
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

	public Cliente fromDto(ClienteNewDTO clienteNewDTO){
	    Cliente cliente = new Cliente(null, clienteNewDTO.getNome(), clienteNewDTO.getEmail(),
                clienteNewDTO.getCpfOuCnpj(), TipoCliente.toEnum(clienteNewDTO.getTipo()));

	    Cidade cidade = cidadeRepo.getOne(clienteNewDTO.getCidadeId());

        Endereco endereco = new Endereco(null, clienteNewDTO.getLogradouro(), clienteNewDTO.getNumero(),
                clienteNewDTO.getComplemento(), clienteNewDTO.getBairro(), clienteNewDTO.getCep(), cliente, cidade);

        cliente.getEnderecos().add(endereco);
        cliente.getTelefones().add(clienteNewDTO.getTelefone1());

        if(clienteNewDTO.getTelefone2() != null)
            cliente.getTelefones().add(clienteNewDTO.getTelefone2());

        if(clienteNewDTO.getTelefone3() != null)
            cliente.getTelefones().add(clienteNewDTO.getTelefone3());

        return cliente;
    }

	public Cliente fromDto(ClienteDTO clienteDTO){

		Cliente cliente = new Cliente();

		cliente.setId(clienteDTO.getId());
		cliente.setNome(clienteDTO.getNome());
		cliente.setEmail(clienteDTO.getEmail());

		return cliente;
	}
}
