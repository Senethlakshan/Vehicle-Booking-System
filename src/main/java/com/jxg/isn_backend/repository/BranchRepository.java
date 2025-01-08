package com.jxg.isn_backend.repository;
import com.jxg.isn_backend.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {
}