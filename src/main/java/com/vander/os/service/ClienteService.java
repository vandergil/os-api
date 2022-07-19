package com.vander.os.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vander.os.domain.Cliente;
import com.vander.os.domain.Pessoa;
import com.vander.os.dtos.ClienteDTO;
import com.vander.os.repositories.ClienteRepository;
import com.vander.os.repositories.PessoaRepository;
import com.vander.os.service.exceptions.DataIntegrityViolationException;
import com.vander.os.service.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private PessoaRepository pessoaRepository;

	public Cliente findById(Integer id) {
		Optional<Cliente> obj = repository.findById(id);
		return obj.orElseThrow(()-> new ObjectNotFoundException
				("Objeto não encontrado! ID: " + id + " Tipo: " + Cliente.class.getName()));
	}
	
	public List<Cliente> findAll(){
		return repository.findAll();
	}
	
	public Cliente create(ClienteDTO objDTO) {
		
		if(findByCpf(objDTO) != null) {
			throw new DataIntegrityViolationException("CPF já cadastrado na base de dados!");
		}
		
		return repository.save(new Cliente(null, objDTO.getNome(), objDTO.getCpf(), objDTO.getTelefone()));
		
	}
	
	public Cliente update(Integer id, @Valid ClienteDTO objDTO) {
		
		Cliente oldObj = findById(id);
		
		if(findByCpf(objDTO) != null && findByCpf(objDTO).getId() != id) {
			throw new DataIntegrityViolationException("CPF já cadastrado na base de dados!");
		}
		
		oldObj.setCpf(objDTO.getCpf());
		oldObj.setNome(objDTO.getNome());
		oldObj.setTelefone(objDTO.getTelefone());
		
		return repository.save(oldObj);
	}
	
	public void delte(Integer id) {
		Cliente obj = findById(id);
		
		if(obj.getList().size() > 0) {
			throw new DataIntegrityViolationException("Técnico possui ordens de serviço e não pode ser excluído!");
		}
		
		repository.deleteById(id);
	}
	
	private Pessoa findByCpf(ClienteDTO objDTO) {
		Pessoa obj = pessoaRepository.findByCpf(objDTO.getCpf());
		
		if(obj != null)
			return obj;
		
		return null;
	}
}
