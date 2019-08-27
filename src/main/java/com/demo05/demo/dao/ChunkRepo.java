package com.demo05.demo.dao;

import com.demo05.demo.model.Chunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ChunkRepo extends JpaRepository<Chunk,Long>, JpaSpecificationExecutor<Chunk> {
}
