package com.HNG.Stage5_First_PersonalTask.repository;

import com.HNG.Stage5_First_PersonalTask.entity.Videos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepo extends JpaRepository<Videos,Long> {
    Videos findByName(String videoName);
}

