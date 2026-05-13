package com.marianodefea.servicios_web.dto.mapper;

import com.marianodefea.servicios_web.dto.ActualizarAgenteDTO;
import com.marianodefea.servicios_web.model.Agente;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AgenteMapper {
    ActualizarAgenteDTO aDto(Agente agente);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cargosAsignados", ignore = true)
    @Mapping(target = "fichadas", ignore = true)
    void actualizarAgenteDesdeDto(ActualizarAgenteDTO dto, @MappingTarget Agente agenteReal);
}