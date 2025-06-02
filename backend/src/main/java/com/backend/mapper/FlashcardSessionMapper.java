package com.backend.mapper;

import com.backend.dto.FlashcardSessionDTO;
import com.backend.model.FlashcardSession;
import com.backend.repository.CourseRepository;
import com.backend.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class FlashcardSessionMapper implements EntityMapper<FlashcardSession, FlashcardSessionDTO> {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public FlashcardSessionMapper(UserRepository userRepository, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public FlashcardSessionDTO toDTO(FlashcardSession session) {
        if (session == null) return null;

        FlashcardSessionDTO dto = new FlashcardSessionDTO();
        dto.setId(session.getId());
        dto.setTimestamp(session.getTimestamp());
        dto.setFlashcardCount(session.getFlashcardCount());

        if (session.getUser() != null) {
            dto.setUserId(session.getUser().getId());
        }

        if (session.getCourse() != null) {
            dto.setCourseId(session.getCourse().getId());
        }

        return dto;
    }

    @Override
    public FlashcardSession toEntity(FlashcardSessionDTO dto) {
        if (dto == null) return null;

        FlashcardSession session = new FlashcardSession();
        session.setId(dto.getId());
        session.setTimestamp(dto.getTimestamp());
        session.setFlashcardCount(dto.getFlashcardCount());

        if (dto.getUserId() != null) {
            userRepository.findById(dto.getUserId())
                    .ifPresent(session::setUser);
        }

        if (dto.getCourseId() != null) {
            courseRepository.findById(dto.getCourseId())
                    .ifPresent(session::setCourse);
        }

        return session;
    }
}