package pe.edu.vallegrande.vgmsusersauthentication.infrastructure.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ResponseDto;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(CustomException.class)
        public Mono<ResponseEntity<ResponseDto<Object>>> handleCustomException(CustomException ex) {
                log.error("Custom exception: {}", ex.getMessage(), ex);

                ErrorMessage errorMessage = new ErrorMessage(
                                ex.getMessage(),
                                ex.getErrorCode(),
                                ex.getHttpStatus());

                return Mono.just(ResponseEntity
                                .status(ex.getHttpStatus())
                                .body(new ResponseDto<>(false, errorMessage)));
        }

        @ExceptionHandler(WebExchangeBindException.class)
        public Mono<ResponseEntity<ResponseDto<Object>>> handleValidationException(WebExchangeBindException ex) {
                log.error("Validation exception: {}", ex.getMessage(), ex);

                String details = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(FieldError::getDefaultMessage)
                                .collect(Collectors.joining(", "));

                ErrorMessage errorMessage = new ErrorMessage(
                                "Errores de validación: " + details,
                                "VALIDATION_ERROR",
                                HttpStatus.BAD_REQUEST.value());

                return Mono.just(ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(new ResponseDto<>(false, errorMessage)));
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public Mono<ResponseEntity<ResponseDto<Object>>> handleIllegalArgumentException(IllegalArgumentException ex) {
                log.error("Illegal argument exception: {}", ex.getMessage(), ex);

                ErrorMessage errorMessage = new ErrorMessage(
                                ex.getMessage(),
                                "INVALID_ARGUMENT",
                                HttpStatus.BAD_REQUEST.value());

                return Mono.just(ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(new ResponseDto<>(false, errorMessage)));
        }

        @ExceptionHandler(Exception.class)
        public Mono<ResponseEntity<ResponseDto<Object>>> handleGlobalException(Exception ex) {
                log.error("Unexpected exception: {}", ex.getMessage(), ex);

                ErrorMessage errorMessage = new ErrorMessage(
                                "Error interno del servidor",
                                "INTERNAL_SERVER_ERROR",
                                HttpStatus.INTERNAL_SERVER_ERROR.value());

                return Mono.just(ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ResponseDto<>(false, errorMessage)));
        }
}
