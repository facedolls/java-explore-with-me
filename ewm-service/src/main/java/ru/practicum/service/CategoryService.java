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
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public List<CategoryDto> getAll(Pageable pageable) {
        List<Category> categories = categoryRepository.findAll(pageable).getContent();
        return categoryMapper.fromListCategoriesToDto(categories);
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategory(Long catId) {
        Category category = getCategoryOrElseThrow(catId);
        return categoryMapper.fromCategoryToDto(category);
    }

    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.fromDtoToCategory(categoryDto);
        Category createdCategory = categoryRepository.save(category);
        return categoryMapper.fromCategoryToDto(createdCategory);
    }

    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Long catId = categoryDto.getId();
        Category categoryInRepo = getCategoryOrElseThrow(catId);
        if (categoryDto.getName() != null) {
            categoryInRepo.setName(categoryDto.getName());
        }
        Category updatedCategory = categoryRepository.save(categoryInRepo);
        return categoryMapper.fromCategoryToDto(updatedCategory);
    }

    public void deleteCategoryById(Long catId) {
        getCategoryOrElseThrow(catId);
        categoryRepository.deleteById(catId);
    }

    private Category getCategoryOrElseThrow(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
    }
}
