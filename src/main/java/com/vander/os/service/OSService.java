package com.vander.os.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vander.os.domain.Cliente;
import com.vander.os.domain.OS;
import com.vander.os.domain.Tecnico;
import com.vander.os.dtos.OSDTO;
import com.vander.os.repositories.OSRepository;
import com.vander.os.service.exceptions.ObjectNotFoundException;

@Service
public class OSService {
	
	@Autowired
	private TecnicoService tecnicoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private OSRepository repository;
	
	public OS findById(Integer id) {
		Optional<OS> obj = repository.findById(id);
		return obj.orElseThrow(()-> new ObjectNotFoundException
				("Objeto não encontrado! ID: " + id + " Tipo: " + OS.class.getName()));
	}
	
	public List<OS> findAll(){
		return repository.findAll();
	}
	
	public OS create(@Valid OSDTO objDTO) {
		return fromDTO(objDTO);		
	}
	
	public OS update(@Valid OSDTO obj) {
		findById(obj.getId());
		return fromDTO(obj);
	}
	
	private OS fromDTO(OSDTO obj) {
		OS newObj = new OS();
		newObj.setId(obj.getId());
		newObj.setObservacoes(obj.getObservacoes());
		newObj.setPrioridade(obj.getPrioridade());
		newObj.setStatus(obj.getStatus());
		
		Tecnico tec = tecnicoService.findById(obj.getTecnico());
		Cliente cli = clienteService.findById(obj.getCliente());
		
		newObj.setTecnico(tec);
		newObj.setCliente(cli);
		
		if(newObj.getStatus().getCod().equals(2)) {
			newObj.setDataFechamento(LocalDateTime.now());
		}
		
		return repository.save(newObj);
	}	
}
