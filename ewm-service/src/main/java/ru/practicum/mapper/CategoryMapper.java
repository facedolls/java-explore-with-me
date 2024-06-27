package ru.practicum.mapper;


import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.practicum.dto.CategoryDto;
import ru.practicum.model.Category;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CategoryMapper {
    Category fromDtoToCategory(CategoryDto categoryDto);

    CategoryDto fromCategoryToDto(Category category);

    List<CategoryDto> fromCategoryToDtoList(List<Category> categories);
}
