package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompilationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", source = "events")
    Compilation fromNewDtoToCompilation(NewCompilationDto dto, List<Event> events);

    CompilationDto fromCompilationToDto(Compilation compilation);

    List<CompilationDto> fromCompilationListToDto(List<Compilation> compilations);
}
