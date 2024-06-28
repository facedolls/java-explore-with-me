package ru.practicum.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CategoryDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryMapper mapper;
    private final CategoryRepository repository;


    public CategoryDto create(CategoryDto categoryDto) {
        Category category = mapper.fromDtoToCategory(categoryDto);
        Category createdCategory = repository.save(category);
        return mapper.fromCategoryToDto(createdCategory);
    }

    public CategoryDto update(CategoryDto categoryDto) {
        Long catId = categoryDto.getId();
        Category categoryInRepo = getOrElseThrow(catId);
        if (categoryDto.getName() != null) {
            categoryInRepo.setName(categoryDto.getName());
        }
        Category updatedCategory = repository.save(categoryInRepo);
        return mapper.fromCategoryToDto(updatedCategory);
    }

    public CategoryDto get(Long catId) {
        Category category = getOrElseThrow(catId);
        return mapper.fromCategoryToDto(category);
    }

    public List<CategoryDto> getAll(Pageable pageable) {
        List<Category> categories = repository.findAll(pageable).getContent();
        return mapper.fromCategoryToDtoList(categories);
    }

    public void deleteById(Long catId) {
        getOrElseThrow(catId);
        repository.deleteById(catId);
    }

    private Category getOrElseThrow(Long catId) {
        return repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category id: " + catId + " not found"));
    }
}
