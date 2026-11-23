package com.talosi.superfight.service;

import com.talosi.superfight.exception.HttpRequestException;
import com.talosi.superfight.exception.NotFoundException;
import com.talosi.superfight.model.Player;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayerServiceTest {

    private final PlayerService service = new PlayerService();

    @Test
    void missingIdsAreNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(999));
        assertThrows(NotFoundException.class, () -> service.delete(999));
        assertThrows(NotFoundException.class, () -> service.update(999, player(999, "Missing")));
        assertThrows(NotFoundException.class, () -> service.get(-1));
    }

    @Test
    void updateUsesRouteIdAndDoesNotOverwriteBodyIdPlayer() {
        service.create(player(1, "First"));
        Player second = service.create(player(2, "Second"));

        Player result = service.update(1, player(2, "Updated"));

        assertEquals(1, result.getId());
        assertEquals("Updated", service.get(1).getName());
        assertSame(second, service.get(2));
        assertEquals("Second", service.get(2).getName());
    }

    @Test
    void updateCannotCreateAnAbsentRouteIdEvenWhenBodyIdExists() {
        Player second = service.create(player(2, "Second"));

        assertThrows(NotFoundException.class, () -> service.update(999, player(2, "Changed")));
        assertSame(second, service.get(2));
        assertThrows(NotFoundException.class, () -> service.get(999));
    }

    @Test
    void duplicateIdIsConflictAndDoesNotReplacePlayer() {
        Player first = service.create(player(1, "First"));

        HttpRequestException exception = assertThrows(HttpRequestException.class,
                () -> service.create(player(1, "Duplicate")));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertSame(first, service.get(1));
        assertEquals("First", service.get(1).getName());
    }

    private Player player(int id, String name) {
        return new Player(id, name, null, null, null, null, 0);
    }
}
