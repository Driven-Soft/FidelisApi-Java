package br.com.fiap.java.FidelisApi.exception;

public class PetAnalysisException extends RuntimeException {

    public PetAnalysisException(String message) {
        super(message);
    }

    public PetAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}
