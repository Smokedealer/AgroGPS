package dev.agrogps.server.core.utils;

import dev.agrogps.server.core.posts.domain.Position;
import dev.agrogps.server.core.posts.dto.PositionDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * Date: 6.12.16
 *
 * @author Jakub Danek
 */
public class Mapper {

    public static List<PositionDTO> map(List<Position> posts) {
        List<PositionDTO> toRet = new LinkedList<>();
        posts.forEach(p -> toRet.add(new PositionDTO(p)));

        return toRet;
    }

}
