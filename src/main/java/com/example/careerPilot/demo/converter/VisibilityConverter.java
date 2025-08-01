package com.example.careerPilot.demo.converter;

import com.example.careerPilot.demo.entity.Post;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class VisibilityConverter implements AttributeConverter<Post.Visibility, String> {
    @Override
    public String convertToDatabaseColumn(Post.Visibility visibility) {
        return visibility.name().toLowerCase();
    }

    @Override
    public Post.Visibility convertToEntityAttribute(String dbData) {
        return Post.Visibility.valueOf(dbData.toUpperCase());
    }
}