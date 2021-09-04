package com.israelmessias.minhasfinancas.controller;

import com.israelmessias.minhasfinancas.api.dto.LancamentoDTO;
import com.israelmessias.minhasfinancas.exception.RegraNegocioException;
import com.israelmessias.minhasfinancas.model.Entity.Lancamento;
import com.israelmessias.minhasfinancas.model.enums.StatusLancamento;
import com.israelmessias.minhasfinancas.model.enums.TipoLancamento;
import com.israelmessias.minhasfinancas.model.Entity.Usuario;
import com.israelmessias.minhasfinancas.service.LancamentoService;
import com.israelmessias.minhasfinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController
{
    /*
    * @RequiredArgsConstructor  faz um contrutor para atributos com final*/
   private final LancamentoService service;
   private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity salvar( @RequestBody LancamentoDTO dto )
    {
        try
        {
            Lancamento entidade = converter(dto);

            entidade = service.salvar(entidade);

            return new ResponseEntity(entidade, HttpStatus.CREATED);
        }
        catch (RegraNegocioException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto)
    {
        return service.obterPorId(id).map(entity -> {
            try {
                Lancamento lancamento = converter(dto);
                lancamento.setId(entity.getId());
                service.atualizar(lancamento);
                return ResponseEntity.ok(lancamento);
            }
            catch (RegraNegocioException e)
            {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).
                orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id){
        return service.obterPorId(id).map(entidade -> {
            service.deletar(entidade);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).
                orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
    }

    @GetMapping
    public ResponseEntity buscar(
            @RequestParam(value="descricao", required = false) String descricao,
            @RequestParam(value="mes", required = false) Integer mes,
            @RequestParam(value="ano", required = false) Integer ano,
            @RequestParam(value="Usuario") Long idUsuario
    ) {
        Lancamento lancamentoFiltro = new Lancamento();
        lancamentoFiltro.setDescricao(descricao);
        lancamentoFiltro.setMes(mes);
        lancamentoFiltro.setAno(ano);
        Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
        if(!usuario.isPresent())
        {
            return ResponseEntity.badRequest().body("Não foi possivel realizar consulta!");
        }else
        {
            lancamentoFiltro.setUsuario(usuario.get());
        }

        List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
        return ResponseEntity.ok(lancamentos);
    }


    private Lancamento converter(LancamentoDTO dto)
    {
        Lancamento lancamento = new Lancamento();
        lancamento.setId(dto.getId());
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setAno(dto.getAno());
        lancamento.setMes(dto.getMes());
        lancamento.setValor(dto.getValor());

        Usuario usuario = usuarioService
                .obterPorId(dto.getUsuario())
                .orElseThrow( () -> new RegraNegocioException("Usuário não encontrado para o Id informado.") );

        lancamento.setUsuario(usuario);

        if(dto.getTipo() != null) {
            lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
        }

        if(dto.getStatus() != null) {
            lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
        }

        return lancamento;
    }
}
