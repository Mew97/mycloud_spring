package com.demo05.demo.service.impl;

import com.demo05.demo.dao.ChunkRepo;
import com.demo05.demo.model.Chunk;
import com.demo05.demo.service.ChunkService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ChunkServiceImp implements ChunkService {
    @Resource(name = "redisTem")
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private ChunkRepo chunkRepo;



    @Override
    public void rSaveChunk(Chunk chunk) {
        redisTemplate.opsForValue().set("api:chunk:" + chunk.getIdentifier() + "-" + chunk.getChunkNumber(), new Date().getTime() + "");
    }

    @Override
    public boolean rCheckChunk(String identifier, Integer chunkNumber) {
        Set<String> keys = redisTemplate.keys("api:chunk:" + identifier + "-" + chunkNumber);
        assert keys != null;
        return keys.size() == 0;
    }

//    @Override
//    public void saveChunk(Chunk chunk) {
//        chunkRepo.save(chunk);
//    }
//
//    @Override
//    public boolean checkChunk(String identifier, Integer chunkNumber) {
//        Specification<Chunk> specification = (Specification<Chunk>) (root, criteriaQuery, criteriaBuilder) -> {
//            List<Predicate> predicates = new ArrayList<>();
//            predicates.add(criteriaBuilder.equal(root.get("identifier"), identifier));
//            predicates.add(criteriaBuilder.equal(root.get("chunkNumber"), chunkNumber));
//
//            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
//        };
//
//        return chunkRepo.findOne(specification).orElse(null) == null;
//    }
}


