package br.ufc.quixada.npi.sinutri.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.quixada.npi.sinutri.model.InqueritoAlimentar;
import br.ufc.quixada.npi.sinutri.model.Mensagem;
import br.ufc.quixada.npi.sinutri.model.Paciente;
import br.ufc.quixada.npi.sinutri.model.Servidor;
import br.ufc.quixada.npi.sinutri.model.enuns.FrequenciaSemanal;
import br.ufc.quixada.npi.sinutri.service.ConsultaService;
import br.ufc.quixada.npi.sinutri.service.PacienteService;
import br.ufc.quixada.npi.sinutri.service.PessoaService;

@Controller
@RequestMapping(value = "/Paciente")
public class PacienteController {

	@Inject
	private ConsultaService consultaService;
	
	@Inject
	private PessoaService pessoaService;
	
	@Inject
	private PacienteService pacienteService;
	
	@RequestMapping(value= "/{idPaciente}/InqueritoAlimentar", method = RequestMethod.GET)
	public String formAdicionarInqueritoAlimentar(Model model, @PathVariable("idPaciente") Long idPaciente, RedirectAttributes redirectAttributes){
		Paciente paciente =  pacienteService.buscarPacientePorId(idPaciente);
		List<Mensagem> mensagens = new ArrayList<Mensagem>();
		if(isInvalido(paciente)) {
			mensagens.add(new Mensagem("Paciente inexistente", Mensagem.Tipo.ERRO, Mensagem.Prioridade.ALTA));
			redirectAttributes.addFlashAttribute("mensagens", mensagens);
			return "redirect:/Nutricao/Buscar";
		}
			
		InqueritoAlimentar inqueritoAlimentar = new InqueritoAlimentar();
		inqueritoAlimentar.setCriadoEm(new Date());
		
		inqueritoAlimentar.setPaciente(paciente);
		model.addAttribute("inqueritoAlimentar", inqueritoAlimentar);
		model.addAttribute("frequenciasSemanais", FrequenciaSemanal.values());

		return "inquerito-alimentar/cadastrar";
	}
	
	@RequestMapping(value = "/{idPaciente}/InqueritoAlimentar", method = RequestMethod.POST)
	public String adicionarInqueritoAlimentar(Model model, @PathVariable("idPaciente") Long idPaciente, @Valid @ModelAttribute("inqueritoAlimentar") InqueritoAlimentar inqueritoAlimentar, BindingResult result, RedirectAttributes redirectAttributes){
		Paciente paciente = pacienteService.buscarPacientePorId(idPaciente);
		List<Mensagem> mensagens = new ArrayList<Mensagem>();
		if(isInvalido(paciente)){
			mensagens.add(new Mensagem("Paciente inexistente", Mensagem.Tipo.ERRO, Mensagem.Prioridade.ALTA));
			redirectAttributes.addFlashAttribute("mensagens", mensagens);
			return "redirect:/Nutricao/Buscar";
		}
		if (result.hasErrors()) {
			return "inquerito-alimentar/cadastrar";
		}
		String cpfPessoaLogada = getCpfPessoaLogada();
		Servidor nutricionista = pessoaService.buscarServidorPorCpf(cpfPessoaLogada);
		if(nutricionista == null){
			mensagens.add(new Mensagem("Nutricionista inexistente", Mensagem.Tipo.ERRO, Mensagem.Prioridade.ALTA));
			redirectAttributes.addFlashAttribute("mensagens", mensagens);
			return "redirect:/Nutricao/Buscar";
		}
		inqueritoAlimentar.setNutricionista(nutricionista);
		consultaService.adicionarInqueritoAlimentar(inqueritoAlimentar, paciente);
		return "redirect:/Paciente/"+idPaciente+"/InqueritoAlimentar/"+inqueritoAlimentar.getId();
	}
	
	@RequestMapping(value = "/{idPaciente}/InqueritoAlimentar/{idInqueritoAlimentar}/Editar", method = RequestMethod.GET)
	public String formEditarInqueritoAlimentar(@PathVariable("idInqueritoAlimentar") Long idInqueritoAlimentar, @PathVariable("idPaciente") Long idPaciente, Model model, RedirectAttributes redirectAttributes ){
		InqueritoAlimentar inqueritoAlimentar = consultaService.buscarInqueritoAlimentarPorId(idInqueritoAlimentar);
		List<Mensagem> mensagens = new ArrayList<Mensagem>();
		if(inqueritoAlimentar == null){
			mensagens.add(new Mensagem("Inquérito Alimentar não encontrado.", Mensagem.Tipo.ERRO, Mensagem.Prioridade.MEDIA));
			redirectAttributes.addFlashAttribute("mensagens", mensagens);
			return "redirect:/Paciente/"+idPaciente;
		}
		model.addAttribute("inqueritoAlimentar", inqueritoAlimentar);
		model.addAttribute("frequenciasSemanais", FrequenciaSemanal.values());
		return "inquerito-alimentar/editar";
	}
	
	@RequestMapping(value = "/{idPaciente}/InqueritoAlimentar/{idInqueritoAlimentar}/Editar", method = RequestMethod.POST)
	public String editarInqueritoAlimentar(Model model, @PathVariable("idInqueritoAlimentar") Long idInqueritoAlimentar, @PathVariable("idPaciente") Long idPaciente, @Valid InqueritoAlimentar inqueritoAlimentar, BindingResult result, RedirectAttributes redirectAttributes){
		if (result.hasErrors()) {
			return "inquerito-alimentar/editar";
		}
		consultaService.editarInqueritoAlimentar(inqueritoAlimentar);
		return "redirect:/Paciente/"+inqueritoAlimentar.getPaciente().getId()+"/InqueritoAlimentar/"+idInqueritoAlimentar;
	}
	
	@RequestMapping(value = "/{idPaciente}/InqueritoAlimentar/{idInqueritoAlimentar}", method = RequestMethod.GET)
	public String visualizarInqueritoAlimentar(@PathVariable("idInqueritoAlimentar") Long idInqueritoAlimentar, @PathVariable("idPaciente") Long idPaciente, Model model, RedirectAttributes redirectAttributes ){
		InqueritoAlimentar inqueritoAlimentar = consultaService.buscarInqueritoAlimentarPorId(idInqueritoAlimentar);
		List<Mensagem> mensagens = new ArrayList<Mensagem>();
		if(inqueritoAlimentar == null){
			mensagens.add(new Mensagem("Inquérito Alimentar não encontrado.", Mensagem.Tipo.ERRO, Mensagem.Prioridade.MEDIA));
			redirectAttributes.addFlashAttribute("mensagens", mensagens);
			return "redirect:/Paciente/"+idPaciente;
		}
		model.addAttribute("inqueritoAlimentar", inqueritoAlimentar);
		return "inquerito-alimentar/visualizar";
	}
	
	
	@RequestMapping(value = "/{idPaciente}/InqueritoAlimentar/{idInqueritoAlimentar}/Excluir", method = RequestMethod.GET)
	public String excluirInqueritoAlimentar(@PathVariable("idPaciente") Long idPaciente, @PathVariable("idInqueritoAlimentar") Long idInqueritoAlimentar, RedirectAttributes redirectAttributes){
		InqueritoAlimentar inqueritoAlimentar = consultaService.buscarInqueritoAlimentarPorId(idInqueritoAlimentar);
		List<Mensagem> mensagens = new ArrayList<Mensagem>();
		if(inqueritoAlimentar == null){
			mensagens.add(new Mensagem("Inquérito Alimentar não encontrado.", Mensagem.Tipo.ERRO, Mensagem.Prioridade.MEDIA));
			redirectAttributes.addFlashAttribute("mensagens", mensagens);
			return "redirect:/Paciente/"+idPaciente;
		}
		consultaService.excluirInqueritoAlimentar(inqueritoAlimentar);
		return "redirect:/Paciente/"+idPaciente;
	}
	
	
	private boolean isInvalido(Paciente paciente){
		return paciente == null;
	}

	private String getCpfPessoaLogada() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
}