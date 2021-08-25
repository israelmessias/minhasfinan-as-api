package com.israelmessias.minhasfinancas.controller;

import com.israelmessias.minhasfinancas.api.dto.UsuarioDTO;
import com.israelmessias.minhasfinancas.exception.ErroAutenticacao;
import com.israelmessias.minhasfinancas.exception.RegraNegocioException;
import com.israelmessias.minhasfinancas.model.Entity.Usuario;
import com.israelmessias.minhasfinancas.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService service;

    public UsuarioController(UsuarioService service)
    {
        this.service = service;
    }

    @GetMapping("/hello")
    public String HelloWorld()
    {
        return "Salve pivete!!!";
    }

    @PostMapping("/salvar")
    public ResponseEntity salvar(@RequestBody UsuarioDTO usuarioDTO)
    {
       Usuario usuario = Usuario.builder().nome(usuarioDTO.getNome()).
       email(usuarioDTO.getEmail()).senha(usuarioDTO.getSenha()).build();

       try 
       {
           Usuario usuarioSalvo = service.salvarUsuario(usuario);
           return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
       } catch (RegraNegocioException e) 
       {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    @PostMapping("/autenticar")
    public ResponseEntity autenticar(@RequestBody UsuarioDTO dto)
    {
        try 
        {
            Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha()); 
            return ResponseEntity.ok(usuarioAutenticado);
        } catch (ErroAutenticacao e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
