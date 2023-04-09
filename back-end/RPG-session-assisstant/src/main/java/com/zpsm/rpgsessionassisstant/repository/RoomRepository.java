package com.zpsm.rpgsessionassisstant.repository;

import com.zpsm.rpgsessionassisstant.model.Character;
import com.zpsm.rpgsessionassisstant.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("select r.character from Room r where r.id = :roomId")
    Collection<Character> findAllByRoomId(long roomId);

    @Query("select r.password from Room r where r.id = :roomId")
    Optional<String> getPasswordOfRoom(long roomId);

    Optional<Room> findByName(String name);

}
