package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.CategoryDto;
import ru.practicum.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto fromCategoryToDto(Category category);

    Category fromDtoToCategory(CategoryDto categoryDto);

    List<CategoryDto> fromListCategoriesToDto(List<Category> categories);
}
