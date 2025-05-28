package com.maxeagan.restaurant.mappers;

import com.maxeagan.restaurant.domain.dtos.PhotoDto;
import com.maxeagan.restaurant.domain.entities.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * MapStruct mapper for converting {@link Photo} entities to {@link PhotoDto} objects.
 *
 * Ignores unmapped target properties to avoid warnings during build.
 * Uses Spring's component model for dependency injection.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhotoMapper {

    /**
     * Converts a {@link Photo} entity to a {@link PhotoDto}.
     *
     * @param photo the Photo entity to convert
     * @return the corresponding PhotoDto
     */
    PhotoDto toDto(Photo photo);
}
