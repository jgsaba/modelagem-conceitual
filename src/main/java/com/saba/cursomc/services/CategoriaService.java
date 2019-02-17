package com.saba.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saba.cursomc.domain.Categoria;
import com.saba.cursomc.repository.CategoriaRepository;
import com.saba.cursomc.services.exception.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	public Categoria buscar(Integer id) {
		
		Optional<Categoria> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + " Tipo: " + Categoria.class.getName()));
	}
	
	public Categoria insert(Categoria novaCategoria) {
		novaCategoria.setId(null);
		return repo.save(novaCategoria);
	}
}
