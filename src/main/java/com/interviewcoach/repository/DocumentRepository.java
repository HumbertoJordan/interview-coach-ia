package com.interviewcoach.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.interviewcoach.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {

}