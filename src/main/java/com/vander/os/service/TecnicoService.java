package com.vander.os.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vander.os.domain.Pessoa;
import com.vander.os.domain.Tecnico;
import com.vander.os.dtos.TecnicoDTO;
import com.vander.os.repositories.PessoaRepository;
import com.vander.os.repositories.TecnicoRepository;
import com.vander.os.service.exceptions.DataIntegrityViolationException;
import com.vander.os.service.exceptions.ObjectNotFoundException;

@Service
public class TecnicoService {
	
	@Autowired
	private TecnicoRepository repository;
	
	@Autowired
	private PessoaRepository pessoaRepository;

	public Tecnico findById(Integer id) {
		Optional<Tecnico> obj = repository.findById(id);
		return obj.orElseThrow(()-> new ObjectNotFoundException(
				"Objeto não encontrado! ID: " + id + " Tipo: " + Tecnico.class.getName()));
	}

	public List<Tecnico> findAll() {
		return repository.findAll();
	}
	
	public Tecnico create (TecnicoDTO objDTO) {
		if(findByCpf(objDTO) != null) {
			throw new DataIntegrityViolationException("CPF já cadastrado na base de dados!");
		}
		
		return repository.save(new Tecnico(null, objDTO.getNome(), objDTO.getCpf(), objDTO.getTelefone()));
	}
	
	public Tecnico update(Integer id, @Valid TecnicoDTO objDTO) {
		
		Tecnico oldObj = findById(id);
		
		if(findByCpf(objDTO) != null && findByCpf(objDTO).getId() != id) {
			throw new DataIntegrityViolationException("CPF já cadastrado na base de dados!");
		}
		
		oldObj.setNome(objDTO.getNome());
		oldObj.setCpf(objDTO.getCpf());
		oldObj.setTelefone(objDTO.getTelefone());
		
		return repository.save(oldObj);
	}
	
	public void delete(Integer id) {		
		Tecnico obj = findById(id);
		
		if(obj.getList().size() > 0) {
			throw new DataIntegrityViolationException("Técnico possui ordens de serviço e não pode ser excluído!");
		}
		
		repository.deleteById(id);		
	}
	
	private Pessoa findByCpf(TecnicoDTO objDTO){
		Pessoa obj = pessoaRepository.findByCpf(objDTO.getCpf());
		
		if(obj != null)
			return obj;
		
		return null;
	}

	

	

}
