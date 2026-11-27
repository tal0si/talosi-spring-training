package com.talosi.superfight.arena;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.talosi.superfight.arena.ArenaDtos.PingResult;
import com.talosi.superfight.arena.ArenaDtos.Register;
import com.talosi.superfight.arena.ArenaDtos.Registered;
import com.talosi.superfight.dto.PlayerDto;
import com.talosi.superfight.service.PlayerService;

@Component
@ConditionalOnProperty(name = "arena.enabled", havingValue = "true")
public class ArenaRegistration {
    private static final Logger LOG = LoggerFactory.getLogger(ArenaRegistration.class);
    private final PlayerService players;
    private final ArenaCredentials credentials;
    private final RestClient client;
    private final String playerId;
    private final String playerAlias;
    private final String callbackUrl;
    private final String joinCode;
    private volatile String bearerToken;

    public ArenaRegistration(PlayerService players, ArenaCredentials credentials,
            @Value("${arena.url}") String arenaUrl,
            @Value("${arena.player-id:}") String playerId,
            @Value("${arena.player-alias:}") String playerAlias,
            @Value("${arena.callback-url}") String callbackUrl,
            @Value("${arena.join-code:}") String joinCode) {
        this.players = players;
        this.credentials = credentials;
        this.playerId = playerId;
        this.playerAlias = playerAlias;
        this.callbackUrl = callbackUrl;
        this.joinCode = joinCode;
        HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2))
                .followRedirects(HttpClient.Redirect.NEVER).build();
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(http);
        factory.setReadTimeout(Duration.ofSeconds(15));
        this.client = RestClient.builder().baseUrl(arenaUrl).requestFactory(factory).build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void registerThenPing() {
        try {
            PlayerDto player = playerId.isBlank()
                    ? players.getByAlias(playerAlias) : players.get(UUID.fromString(playerId));
            Register request = new Register(player.getAlias(), player.getLastname(),
                    player.getName(), player.getPower(), callbackUrl);
            Registered response = client.post().uri("/api/participants")
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(h -> {
                        if (!joinCode.isBlank()) h.set("X-Join-Code", joinCode);
                    })
                    .body(request).retrieve().body(Registered.class);
            if (response == null || response.token() == null || response.token().isBlank()
                    || response.arenaCallToken() == null || response.arenaCallToken().isBlank()) {
                throw new IllegalStateException("Réponse d'inscription incomplète");
            }
            bearerToken = response.token();
            credentials.install(response.arenaCallToken());
            LOG.info("Inscription à l'arène réussie pour {}.", player.getAlias());
        } catch (RuntimeException e) {
            LOG.error("Inscription échouée ou incertaine ({}) ; contactez le formateur.",
                    e.getClass().getSimpleName());
            return;
        }
        try {
            PingResult ping = client.post().uri("/api/me/ping")
                    .headers(h -> h.setBearerAuth(bearerToken)).retrieve().body(PingResult.class);
            if (ping != null && ping.reachable()) {
                LOG.info("Ping arène réussi.");
            } else {
                LOG.warn("Ping arène échoué ; vérifiez réseau et callback.");
            }
        } catch (RuntimeException e) {
            LOG.warn("Ping arène impossible ({}) ; demandez un contrôle au formateur.",
                    e.getClass().getSimpleName());
        }
    }
}
