package com.marianodefea.servicios_web.dto.mapper;
import com.marianodefea.servicios_web.dto.ActualizarAgenteCargoDTO;
import com.marianodefea.servicios_web.model.AgenteCargo;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AgenteCargoMapper {

    // Mappear de la base de datos a la vista
    @Mapping(target = "agenteId", source = "agente.id")
    @Mapping(target = "nombreCargo", source = "cargo.cargoTipo.nombre")
    @Mapping(target = "horaInicio", source = "horario.horaInicio")
    @Mapping(target = "horaFin", source = "horario.horaFin")
    ActualizarAgenteCargoDTO aDto(AgenteCargo agenteCargo);

    // Mappear de la vista a la base de datos
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "agente", ignore = true)
    @Mapping(target = "cargo", ignore = true)
    @Mapping(target = "horario.horaInicio", source = "horaInicio")
    @Mapping(target = "horario.horaFin", source = "horaFin")
    void actualizarDesdeDto(ActualizarAgenteCargoDTO dto, @MappingTarget AgenteCargo agenteCargoReal);
}
