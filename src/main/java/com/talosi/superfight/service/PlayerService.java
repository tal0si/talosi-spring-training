package com.talosi.superfight.service;

import com.talosi.superfight.model.GenderEnum;
import com.talosi.superfight.model.Player;
import com.talosi.superfight.utils.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Service
public class PlayerService {
    HashMap<Integer, Player> players = new HashMap<>();

    public Collection<Player> getAll() {
        return players.values();
    }

    public Player get(int id) {
        checkId(id);
        return players.get(id);
    }

    public Player create(Player player) {
        players.put(player.getId(), player);
        return player;
    }

    public void delete(int id) {
        checkId(id);
        players.remove(id);
    }

    private void checkId(int id) {
        if(id < 0) {
            throw new NotFoundException("player " + id + " not found");
        }
    }

    public Player update(int id, Player player) {
        checkId(player.getId());
        players.put(player.getId(), player);
        return player;
    }
}
