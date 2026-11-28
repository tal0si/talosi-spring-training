#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"
arena_url="${ARENA_URL:-http://127.0.0.1:8090}"

if ! curl --max-time 3 --fail --silent "$arena_url/api/participants" >/dev/null; then
    echo "Arène inaccessible sur $arena_url. Démarrez-la avant les bots." >&2
    exit 1
fi

for port in 9090 9091; do
    if lsof -nP -iTCP:"$port" -sTCP:LISTEN >/dev/null 2>&1; then
        echo "Le port $port est déjà utilisé. Arrêtez le service qui l'occupe avant de continuer." >&2
        exit 1
    fi
done

if [[ ! ${ARENA_JOIN_CODE+x} ]]; then
    printf "Code d'inscription (Entrée si aucun) : "
    IFS= read -r -s ARENA_JOIN_CODE
    printf '\n'
fi
export ARENA_JOIN_CODE
export APIKEY="$(openssl rand -hex 32)"

echo "Compilation des bots..."
mvn -q -Djava.version=23 -DskipTests package
jar="target/spring-0.0.1-SNAPSHOT.jar"
log_dir="$(mktemp -d "${TMPDIR:-/tmp}/superfight-bots.XXXXXXXX")"
pids=()

cleanup() {
    for pid in "${pids[@]}"; do
        kill "$pid" 2>/dev/null || true
    done
    for pid in "${pids[@]}"; do
        wait "$pid" 2>/dev/null || true
    done
}
trap cleanup EXIT
trap 'exit 130' INT
trap 'exit 143' TERM

for port in 9090 9091; do
    if [[ "$port" == 9090 ]]; then alias="ArenaBot"; else alias="ArenaBot2"; fi
    ARENA_URL="$arena_url" ARENA_CALLBACK_URL="http://127.0.0.1:$port" \
        ARENA_PLAYER_ALIAS="$alias" SERVER_PORT="$port" SPRING_PROFILES_ACTIVE=arena-demo \
        java -jar "$jar" >"$log_dir/$alias.log" 2>&1 &
    pids+=("$!")
done

echo "Démarrage des deux bots..."
for attempt in {1..60}; do
    if grep -q "Ping arène réussi" "$log_dir/ArenaBot.log" \
            && grep -q "Ping arène réussi" "$log_dir/ArenaBot2.log"; then
        echo "ArenaBot et ArenaBot2 sont inscrits et joignables."
        echo "Ouvrez $arena_url/ pour lancer leur combat."
        echo "Logs : $log_dir"
        echo "Laissez ce terminal ouvert ; Ctrl+C arrête les deux bots."
        while kill -0 "${pids[0]}" 2>/dev/null && kill -0 "${pids[1]}" 2>/dev/null; do
            sleep 2
        done
        echo "Un bot s'est arrêté. Consultez les logs : $log_dir" >&2
        exit 1
    fi
    if grep -qE "Inscription échouée|Ping arène échoué|Ping arène impossible|APPLICATION FAILED TO START" \
            "$log_dir/ArenaBot.log" "$log_dir/ArenaBot2.log" \
            || ! kill -0 "${pids[0]}" 2>/dev/null || ! kill -0 "${pids[1]}" 2>/dev/null; then
        echo "Un bot n'a pas démarré ou son inscription/ping a échoué. Logs : $log_dir" >&2
        echo "Vérifiez le code d'inscription, ARENA_ALLOW_LOOPBACK et les anciens alias de l'arène." >&2
        exit 1
    fi
    sleep 1
done

echo "Démarrage trop long. Consultez les logs : $log_dir" >&2
exit 1
