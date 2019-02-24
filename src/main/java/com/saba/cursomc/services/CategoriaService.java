package com.saba.cursomc.services;

import java.util.List;
import java.util.Optional;

import com.saba.cursomc.dto.CategoriaDTO;
import com.saba.cursomc.services.exception.DataIntegrityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.saba.cursomc.domain.Categoria;
import com.saba.cursomc.repository.CategoriaRepository;
import com.saba.cursomc.services.exception.ObjectNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;

	public List<Categoria> findAll(){
		return repo.findAll();
	}

	
	public Categoria find(Integer id) {
		
		Optional<Categoria> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + " Tipo: " + Categoria.class.getName()));
	}

	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
        PageRequest pageRequest = new PageRequest(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return repo.findAll(pageRequest);
    }
	
	public Categoria insert(Categoria novaCategoria) {
		novaCategoria.setId(null);
		return repo.save(novaCategoria);
	}

	public Categoria update(Categoria categoria){
		Categoria categoriaDoBanco = find(categoria.getId());
		categoriaDoBanco.setNome(categoria.getNome());
		return repo.save(categoriaDoBanco);
	}

	public void delete(Integer id){
		find(id);
		try{
			repo.deleteById(id);
		}catch (DataIntegrityViolationException e){
			throw new DataIntegrityException("Não é possível excluir uma categoria que possua produtos");
		}
	}

	public Categoria fromDto(CategoriaDTO categoriaDTO){

		return new Categoria(categoriaDTO.getId(), categoriaDTO.getNome());
	}
}
