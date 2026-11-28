package com.talosi.superfight.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sun.net.httpserver.HttpServer;
import com.talosi.superfight.configuration.ArenaCredentials;
import com.talosi.superfight.dto.PlayerDto;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

class ArenaRegistrationTest {
    @Test
    void registersPlayerAndInstallsCallbackTokenBeforePing() throws Exception {
        UUID id = UUID.randomUUID();
        PlayerService players = mock(PlayerService.class);
        when(players.get(id)).thenReturn(PlayerDto.builder()
                .alias("Storm").name("Ororo").lastname("Munroe").power(60).build());
        ArenaCredentials credentials = new ArenaCredentials();
        AtomicReference<String> requestBody = new AtomicReference<>();
        AtomicReference<String> authorization = new AtomicReference<>();
        AtomicBoolean installedAtPing = new AtomicBoolean();
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/api/participants", exchange -> {
            requestBody.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
            byte[] body = """
                    {"participantId":"p1","alias":"Storm","power":60,"maxHp":140,
                     "baseDamage":17,"callbackUrl":"http://127.0.0.1:9090",
                     "token":"sf_test","tokenType":"Bearer","arenaCallToken":"cb_test"}
                    """.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(201, body.length);
            try (var out = exchange.getResponseBody()) {
                out.write(body);
            }
        });
        server.createContext("/api/me/ping", exchange -> {
            authorization.set(exchange.getRequestHeaders().getFirst("Authorization"));
            installedAtPing.set(credentials.accepts("cb_test"));
            byte[] body = """
                    {"reachable":true,"httpStatus":200,"latencyMs":1,"error":null}
                    """.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, body.length);
            try (var out = exchange.getResponseBody()) {
                out.write(body);
            }
        });
        server.start();
        try {
            String url = "http://127.0.0.1:" + server.getAddress().getPort();
            new ArenaRegistration(players, credentials, url, id.toString(), "",
                    "http://127.0.0.1:9090", "join-test").registerThenPing();
            assertThat(requestBody.get()).contains("\"alias\":\"Storm\"", "\"nom\":\"Munroe\"",
                    "\"prenom\":\"Ororo\"", "\"power\":60",
                    "\"callbackUrl\":\"http://127.0.0.1:9090\"");
            assertThat(authorization.get()).isEqualTo("Bearer sf_test");
            assertThat(installedAtPing.get()).isTrue();
        } finally {
            server.stop(0);
        }
    }
}
