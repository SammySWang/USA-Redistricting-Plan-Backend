package com.national.national.repository;

import com.national.national.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Integer> {
    Optional<Job> findById(int id);
    //Optional<Job> findbyIdAndState(int id, String state);
    List<Job> findAll();

}
