package com.VentaOnline.GatewayService.config;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Order(-1)
@Configuration
public class GatewayErrorHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Error interno del servidor";

        if (ex instanceof ResponseStatusException rse) {
            status = HttpStatus.valueOf(rse.getStatusCode().value());
            message = rse.getReason();
            if (status == HttpStatus.NOT_FOUND) {
                message = "La ruta solicitada no existe en el API Gateway. Verifique el endpoint.";
            }
        }

        String json = String.format(
            "{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"%s\",\"message\":\"%s\",\"path\":\"%s\"}",
            LocalDateTime.now(), status.value(), status.getReasonPhrase(), message,
            exchange.getRequest().getPath().value()
        );

        response.setStatusCode(status);
        DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}
