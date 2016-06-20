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

import br.ufc.quixada.npi.sinutri.model.AvaliacaoLaboratorial;
import br.ufc.quixada.npi.sinutri.model.InqueritoAlimentar;
import br.ufc.quixada.npi.sinutri.model.Mensagem;
import br.ufc.quixada.npi.sinutri.model.Paciente;
import br.ufc.quixada.npi.sinutri.model.enuns.Exame;
import br.ufc.quixada.npi.sinutri.model.enuns.FrequenciaSemanal;
import br.ufc.quixada.npi.sinutri.service.ConsultaService;
import br.ufc.quixada.npi.sinutri.service.PacienteService;

@Controller
@RequestMapping(value = "/Paciente")
public class PacienteController {
	
	@Inject
	private ConsultaService consultaService;
	
	@Inject
	private PacienteService pacienteService;
	
	@RequestMapping(value = "/{idPaciente}/AvaliacaoLaboratorial", method = RequestMethod.GET)
	public String formAdicionarAvaliacaoLaboratorial(@PathVariable("idPaciente") Long idPaciente, Model model, RedirectAttributes redirectAttributes){

		Paciente paciente = pacienteService.buscarPacientePorId(idPaciente);
		
		if(isInvalido(paciente)){	
			redirectAttributes.addFlashAttribute("erro", "Paciente não encontrado.");
			return "redirect:/Nutricao/Buscar";
		}
		
		AvaliacaoLaboratorial avaliacaoLaboratorial = new AvaliacaoLaboratorial();
		avaliacaoLaboratorial.setPaciente(paciente);
		avaliacaoLaboratorial.setCriadoEm(new Date());
		model.addAttribute("avaliacaoLaboratorial", avaliacaoLaboratorial);

		return "avaliacao-laboratorial/cadastrar";
	}
	
	@ModelAttribute("exames")
	public Exame[] getExames(){
		return Exame.values();
	}
	
	@RequestMapping(value = "/{idPaciente}/AvaliacaoLaboratorial", method = RequestMethod.POST)
	public String adicionarAvaliacaoLaboratorial(@PathVariable("idPaciente") Long idPaciente, @Valid AvaliacaoLaboratorial avaliacaoLaboratorial, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){

		List<Mensagem> mensagens = new ArrayList<Mensagem>();
		
		Paciente paciente = pacienteService.buscarPacientePorId(idPaciente);
		
		if(isInvalido(paciente)){
			// Erro
			mensagens.add(new Mensagem("Erro ao adicionar Avaliação Laboratorial!", Mensagem.Tipo.ERRO, Mensagem.Prioridade.MEDIA));
			mensagens.add(new Mensagem("Paciente não encontrado!", Mensagem.Tipo.ERRO, Mensagem.Prioridade.MEDIA));
			redirectAttributes.addFlashAttribute("mensagens", mensagens);
			return "redirect:/Nutricao/Buscar";
		}
		
		if (bindingResult.hasErrors()) {
			// Erro
			avaliacaoLaboratorial.setPaciente(paciente);
			model.addAttribute("avaliacaoLaboratorial", avaliacaoLaboratorial);
			
			mensagens.add(new Mensagem("Erro ao adicionar Avaliação Laboratorial!", Mensagem.Tipo.ERRO, Mensagem.Prioridade.MEDIA));
			redirectAttributes.addFlashAttribute("mensagens", mensagens);
			return "avaliacao-laboratorial/cadastrar";
		}
		
		consultaService.adicionarAvaliacaoLaboratorial(avaliacaoLaboratorial, paciente);
		
		mensagens.add(new Mensagem("Avaliação Laboratorial adicionada!", Mensagem.Tipo.SUCESSO, Mensagem.Prioridade.MEDIA));
		redirectAttributes.addFlashAttribute("mensagens", mensagens);
		
		return "redirect:/Paciente/"+idPaciente+"/AvaliacaoLaboratorial/"+avaliacaoLaboratorial.getId()+"/Visualizar";
	}
	
	@RequestMapping(value = "/{idPaciente}/AvaliacaoLaboratorial/{idAvaliacaoLaboratorial}/Visualizar", method = RequestMethod.GET)
	public String visualizarAvaliacaoLaboratorial(@PathVariable("idPaciente") Long idPaciente, @PathVariable("idAvaliacaoLaboratorial") Long idAvaliacaoLaboratorial, RedirectAttributes redirectAttributes, Model model){
		
		AvaliacaoLaboratorial avaliacaoLaboratorial = consultaService.buscarAvaliacaoLaboratorialPorId(idAvaliacaoLaboratorial);
		
		if(avaliacaoLaboratorial == null){
			redirectAttributes.addFlashAttribute("erro", "Avaliacao Laboratorial não encontrada.");
			return "redirect:/Nutricao/Buscar";
		}
		
		model.addAttribute("avaliacaoLaboratorial", avaliacaoLaboratorial);
		return "avaliacao-laboratorial/visualizar";
	}
	
	@RequestMapping(value = "/{idPaciente}/AvaliacaoLaboratorial/{idAvaliacaoLaboratorial}/Editar", method = RequestMethod.GET)
	public String formEditarAvaliacaoLaboratorial(@PathVariable("idPaciente") Long idPaciente, @PathVariable("idAvaliacaoLaboratorial") Long idAvaliacaoLaboratorial, Model model, RedirectAttributes redirectAttributes){
		
		Paciente paciente = pacienteService.buscarPacientePorId(idPaciente);
		
		if(isInvalido(paciente)){
			redirectAttributes.addFlashAttribute("erro", "Paciente não encontrado.");
			return "redirect:/Nutricao/Buscar";
		}
		
		AvaliacaoLaboratorial avaliacaoLaboratorial = consultaService.buscarAvaliacaoLaboratorialPorId(idAvaliacaoLaboratorial);
		
		if(avaliacaoLaboratorial == null){
			redirectAttributes.addFlashAttribute("erro", "Avaliação Laboratorial não encontrada.");
			return "redirect:/Nutricao/Buscar";
		}
		
		model.addAttribute("avaliacaoLaboratorial", avaliacaoLaboratorial);
	
		return "avaliacao-laboratorial/editar";
	}
	
	@RequestMapping(value = "/{idPaciente}/AvaliacaoLaboratorial/{idAvaliacaoLaboratorial}/Editar", method = RequestMethod.POST)
	public String editarAvaliacaoLaboratorial(@PathVariable("idPaciente") Long idPaciente, @PathVariable("idAvaliacaoLaboratorial") Long idAvaliacaoLaboratorial, @Valid AvaliacaoLaboratorial avaliacaoLaboratorial, Model model, BindingResult bindingResult, RedirectAttributes redirectAttributes){
		
		Paciente paciente = pacienteService.buscarPacientePorId(idPaciente);
		
		if(isInvalido(paciente)){
			redirectAttributes.addFlashAttribute("erro", "Paciente não encontrado.");
			return "redirect:/Nutricao/Buscar";
		}
		
		if (bindingResult.hasErrors()) {
			avaliacaoLaboratorial.setPaciente(paciente);
			model.addAttribute("avaliacaoLaboratorial", avaliacaoLaboratorial);
			return "avaliacao-laboratorial/editar";
		}
				
		consultaService.editarAvaliacaoLaboratorial(avaliacaoLaboratorial);
		return "redirect:/Paciente/"+idPaciente+"/AvaliacaoLaboratorial/"+avaliacaoLaboratorial.getId()+"/Visualizar";
	}
	
	@RequestMapping(value = "/{idPaciente}/AvaliacaoLaboratorial/{idAvaliacaoLaboratorial}/Excluir/", method = RequestMethod.GET)
	public String excluirAvaliacaoLaboratorial(@PathVariable("idPaciente") Long idPaciente, @PathVariable("idAvaliacaoLaboratorial") Long idAvaliacaoLaboratorial, RedirectAttributes redirectAttributes){
		
		AvaliacaoLaboratorial avaliacaoLaboratorial = consultaService.buscarAvaliacaoLaboratorialPorId(idAvaliacaoLaboratorial);
		
		if(avaliacaoLaboratorial == null){
			redirectAttributes.addFlashAttribute("erro", "Avaliacao Laboratorial não encontrado. Faça uma nova pesquisa");
			return "redirect:/Nutricao/Buscar";
		}
		
		consultaService.excluirAvaliacaoLaboratorial(avaliacaoLaboratorial);
		return "redirect:/Paciente/"+idPaciente;
	}
	
	@RequestMapping(value= "/{idPaciente}/InqueritoAlimentar/", method = RequestMethod.GET)
	public String formAdicionarInqueritoAlimentar(Model model, @PathVariable("idPaciente") Long idPaciente, RedirectAttributes redirectAttributes){
		Paciente paciente =  pacienteService.buscarPacientePorId(idPaciente);
		
		if(isInvalido(paciente)) {
			redirectAttributes.addFlashAttribute("erro", "Paciente não encontrado. Faça uma nova pesquisa");
			return "redirect:/Nutricao/Buscar";
		}
		
		InqueritoAlimentar inqueritoAlimentar = new InqueritoAlimentar();
		inqueritoAlimentar.setPaciente(paciente);
		model.addAttribute("inqueritoAlimentar", inqueritoAlimentar);
		model.addAttribute("FrequenciasSemanais", FrequenciaSemanal.values());

		return "/inquerito-alimentar/cadastrar";
	}
	
	@RequestMapping(value = "/{idPaciente}/InqueritoAlimentar/", method = RequestMethod.POST)
	public String adicionarInqueritoAlimentar(Model model, @PathVariable("idPaciente") Long idPaciente, @Valid @ModelAttribute("inqueritoAlimentar") InqueritoAlimentar inqueritoAlimentar, BindingResult result, RedirectAttributes redirectAttributes){
		Paciente paciente = pacienteService.buscarPacientePorId(idPaciente);
		if(isInvalido(paciente)){
			redirectAttributes.addFlashAttribute("erro", "Paciente não encontrado. Faça uma nova pesquisa");
			return "redirect:/nutricao/buscar";
		}

		if (result.hasErrors()) {
			inqueritoAlimentar.setPaciente(paciente);
			model.addAttribute("frequenciasSemanais", FrequenciaSemanal.values());
			model.addAttribute("inqueritoAlimentar", inqueritoAlimentar);
			return "nutricao/inquerito/cadastrar";
		}
		
		consultaService.adicionarInqueritoAlimentar(inqueritoAlimentar, paciente);
		return "redirect:/paciente/"+idPaciente+"/InqueritoAlimentar"+inqueritoAlimentar.getId()+"/";
	}
	
	@RequestMapping(value = "/{idPaciente}/InqueritoAlimentar/{idInqueritoAlimentar}/Editar/", method = RequestMethod.GET)
	public String formEditarInqueritoAlimentar(@PathVariable("idInqueritoAlimentar") Long idInqueritoAlimentar, @PathVariable("idPaciente") Long idPaciente, Model model, RedirectAttributes redirectAttributes ){
		Paciente paciente = pacienteService.buscarPacientePorId(idPaciente);
		if(isInvalido(paciente)){
			redirectAttributes.addFlashAttribute("erro", "Paciente não encontrado. Faça uma nova pesquisa");
			return "redirect:/nutricao/buscar";
		}
		InqueritoAlimentar inqueritoAlimentar = consultaService.buscarInqueritoAlimentarPorId(idInqueritoAlimentar);
		if(inqueritoAlimentar == null){
			redirectAttributes.addFlashAttribute("erro", "Inquérito Alimentar não encontrado. Faça uma nova pesquisa");
			return "redirect:/nutricao/buscar";
		}
		model.addAttribute("inqueritoAlimenar", inqueritoAlimentar);
		model.addAttribute("FrequenciasSemanais", FrequenciaSemanal.values());
		return "nutricao/inqueritoAlimentar/editar";
	}
	
	@RequestMapping(value = "/{idPaciente}/InqueritoAlimentar/{idInqueritoAlimentar}/Editar/", method = RequestMethod.POST)
	public String editarInqueritoAlimentar(Model model, @PathVariable("idInqueritoAlimentar") Long idInqueritoAlimentar, @PathVariable("idPaciente") Long idPaciente, @Valid InqueritoAlimentar inqueritoAlimentar, BindingResult result, RedirectAttributes redirectAttributes){
		Paciente paciente = pacienteService.buscarPacientePorId(idPaciente);
		if(isInvalido(paciente)){
			redirectAttributes.addFlashAttribute("erro", "Paciente não encontrado. Faça uma nova pesquisa");
			return "redirect:/nutricao/buscar";
		}
		if (result.hasErrors()) {
			inqueritoAlimentar.setPaciente(paciente);
			model.addAttribute("frequenciasSemanais", FrequenciaSemanal.values());
			model.addAttribute("inqueritoAlimentar", inqueritoAlimentar);
			return "nutricao/inquerito/editar";
		}
		consultaService.editarInqueritoAlimentar(inqueritoAlimentar);
		return "redirect:/paciente/"+idPaciente+"/Inquerito/"+idInqueritoAlimentar+"/";
	}
	
	@RequestMapping(value = "/{idPaciente}/InqueritoAlimentar/{idInqueritoAlimentar}/Excluir/", method = RequestMethod.GET)
	public String excluirInqueritoAlimentar(@PathVariable("idPaciente") Long idPaciente, @PathVariable("idInqueritoAlimentar") Long idInqueritoAlimentar, RedirectAttributes redirectAttributes){
		InqueritoAlimentar inqueritoAlimentar = consultaService.buscarInqueritoAlimentarPorId(idInqueritoAlimentar);
		if(inqueritoAlimentar == null){
			redirectAttributes.addFlashAttribute("erro", "Inquérito Alimentar não encontrado. Faça uma nova pesquisa");
			return "redirect:/nutricao/buscar";
		}
		consultaService.excluirInqueritoAlimentar(inqueritoAlimentar);
		return "redirect:/Paciente/"+idPaciente+"/";
	}
	
	
	private boolean isInvalido(Paciente paciente){
		return paciente == null;
	}

	private String getCpfPessoaLogada() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

}
