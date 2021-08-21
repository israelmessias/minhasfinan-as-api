package com.israelmessias.minhasfinancas.exception;

public class ErroAutenticacao extends RuntimeException{
    public ErroAutenticacao(String msg){
        super(msg);
    }
}
