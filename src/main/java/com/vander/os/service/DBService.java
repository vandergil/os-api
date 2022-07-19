package com.vander.os.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vander.os.domain.Cliente;
import com.vander.os.domain.OS;
import com.vander.os.domain.Tecnico;
import com.vander.os.domain.enuns.Prioridade;
import com.vander.os.domain.enuns.Status;
import com.vander.os.repositories.ClienteRepository;
import com.vander.os.repositories.OSRepository;
import com.vander.os.repositories.TecnicoRepository;

@Service
public class DBService {

	@Autowired
	private TecnicoRepository tecnicoRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private OSRepository osRepository;

	public void instanciaDB() {

		Tecnico t1 = new Tecnico(null, "Vander Gil", "250.336.450-05", "(11)97444-0825");
		Cliente c1 = new Cliente(null, "Betina Campos", "540.792.090-53", "(11)97445-0825");
		OS os1 = new OS(null, Prioridade.ALTA, "Teste Create OS", Status.ANDAMENTO, t1, c1);

		tecnicoRepository.saveAll(Arrays.asList(t1));
		clienteRepository.saveAll(Arrays.asList(c1));
		osRepository.saveAll(Arrays.asList(os1));

	}

}
