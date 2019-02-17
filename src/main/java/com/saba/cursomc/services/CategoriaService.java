package com.saba.cursomc.services;

import java.util.Optional;

import com.saba.cursomc.services.exception.DataIntegrityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.saba.cursomc.domain.Categoria;
import com.saba.cursomc.repository.CategoriaRepository;
import com.saba.cursomc.services.exception.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	public Categoria find(Integer id) {
		
		Optional<Categoria> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + " Tipo: " + Categoria.class.getName()));
	}
	
	public Categoria insert(Categoria novaCategoria) {
		novaCategoria.setId(null);
		return repo.save(novaCategoria);
	}

	public Categoria update(Categoria categoria){
		find(categoria.getId());
		return repo.save(categoria);
	}

	public void delete(Integer id){
		find(id);
		try{
			repo.deleteById(id);
		}catch (DataIntegrityViolationException e){
			throw new DataIntegrityException("Não é possível excluir uma categoria que possua produtos");
		}
	}
}
