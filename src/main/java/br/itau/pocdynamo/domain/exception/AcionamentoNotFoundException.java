package br.itau.pocdynamo.domain.exception;

public class AcionamentoNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public AcionamentoNotFoundException(String message){
        super(message);
    }
}
