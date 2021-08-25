package com.israelmessias.minhasfinancas.controller;

import com.israelmessias.minhasfinancas.api.dto.LancamentoDTO;
import com.israelmessias.minhasfinancas.model.Entity.Lancamento;
import com.israelmessias.minhasfinancas.service.LancamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoController
{
   private LancamentoService service;

    public LancamentoController(LancamentoService service)
    {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentoDTO dto)
    {
        return null;
    }
}
